<template>
  <div 
    class="date-time-picker-container" 
    v-click-outside="clickOutside">

    <input 
      type="text"
      ref="input"
      :name="_uid"
      class="form-control"
      :value="date.toFormat('D T')"
      v-stream:focus="focusChange$"
      v-validate="required ? 'required' : ''">

    <span 
      class="text-danger" 
      v-if="errors.has(_uid)">
      {{ errors.first(_uid) }}
    </span>

    <slide-transition>
      <div 
        class="date-time-picker"
        v-show="display">

      <date-picker 
        v-stream:input="dateUpdate$" 
        :value="date.valueOf()" 
        inline />

      <time-picker 
        v-stream:input="dateUpdate$" 
        :value="date.valueOf()"
        inline />

      <button 
        type="button" 
        class="btn btn-default btn-block"
        v-stream:click="focusChange$">
        
        <i class="fa fa-times"></i>
      </button>

      </div>
    </slide-transition>
  </div>
</template>

<script>
import DatePicker from './DatePicker';
import TimePicker from './TimePicker';
import { SlideTransition } from '../functional/transitions';
import { Subject, Observable } from 'rxjs';
import { DateTime } from 'luxon';

export default {
  name: 'date-time-picker',
  props: {
    value: String | Number,
    required: Boolean
  },
  inject: ['$validator'],
  components: {
    DatePicker,
    TimePicker,
    SlideTransition
  },
  subscriptions () {
    this.dateUpdate$ = new Subject();
    this.timeUpdate$ = new Subject();
    this.focusChange$ = new Subject();
    const onCreate$ = this.$eventToObservable('hook:created');

    const initial$ = Observable.merge(
      onCreate$.mapTo(this.value),
      this.$watchAsObservable('value')
        .pluck('newValue')
    );

    return {
      date: this.dateUpdate$
        .pluck('event', 'msg')
        .merge(initial$)
        .do(timestamp => {
          if (timestamp !== this.value) {
            this.$emit('input', timestamp);
          }
        })
        .map(value => DateTime.fromMillis(value, {
          zone: 'utc'
        })),

      display: Observable
        .merge(
          this.focusChange$,
          this.$createObservableMethod('clickOutside')
        )
        .pluck('event', 'type')
        .map(type => type === 'focus')
    };
  }
};
</script>

<style lang="less">
.date-time-picker-container { 
  position: relative;

  .date-time-picker {
    position: absolute;
    left: 0;
    background: #fff;
    z-index: 2;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    overflow: hidden;

    .date-picker-container {
      .date-picker {
        table th {
          padding: 10px;
        }
      }
    }
  }
}
</style>
