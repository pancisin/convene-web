<template>
  <div 
    class="time-picker-container" 
    v-click-outside="clickOutside">
    
    <input 
      v-show="!inline"
      type="text" 
      ref="input" 
      :placeholder="placeholder" 
      :value="selected.toFormat('T')" 
      class="form-control"
      v-stream:focus="focusChange$" >
  
    <slide-transition>
      <div 
        class="time-picker"
        :class="{ 'time-picker-inline' : inline }" 
        v-show="display || inline">
        
        <div class="hour-selector">
          <button 
            type="button" 
            class="btn btn-link waves-effect"
            v-stream:click="{ subject: timeOffset$, data: { hours: 1 } }">
            
            <i class="fa fa-angle-up"></i>
          </button>
          <div class="hour">
            {{ selected.hour }}
          </div>
          <button 
            type="button" 
            class="btn btn-link waves-effect"
           v-stream:click="{ subject: timeOffset$, data: { hours: -1 } }">
            
            <i class="fa fa-angle-down"></i>
          </button>
        </div>
  
        <span class="separator">:</span>
  
        <div class="minute-selector">
          <button 
            type="button" 
            class="btn btn-link waves-effect"
            v-stream:click="{ subject: timeOffset$, data: { minutes: 15 } }">
            
            <i class="fa fa-angle-up"></i>
          </button>
          <div class="minute">
            {{ selected.minute }}
          </div>
          <button 
            type="button" 
            class="btn btn-link waves-effect"
            v-stream:click="{ subject: timeOffset$, data: { minutes: -15 } }">
            
            <i class="fa fa-angle-down"></i>
          </button>
        </div>
      </div>
    </slide-transition>
  </div>
</template>

<script>
import { DateTime } from 'luxon';
import { SlideTransition } from '../functional/transitions';
import { Subject, Observable } from 'rxjs';

export default {
  name: 'time-picker',
  props: {
    value: String | Number,
    placeholder: String,
    inline: Boolean
  },
  components: {
    SlideTransition
  },
  subscriptions () {
    this.focusChange$ = new Subject();
    this.timeOffset$ = new Subject();
    const onCreate$ = this.$eventToObservable('hook:created');

    const initial$ = Observable.merge(
      onCreate$.mapTo(this.value),
      this.$watchAsObservable('value')
        .pluck('newValue')
    )
    .map(value => DateTime.fromMillis(value, {
      zone: 'utc'
    }));

    return {
      display: Observable
        .merge(
          this.focusChange$,
          this.$createObservableMethod('clickOutside')
        )
        .pluck('event', 'type')
        .map(type => type === 'focus'),
      selected: this.timeOffset$
        .pluck('data')
        .merge(initial$)
        .scan((acc, cur) => (cur instanceof DateTime) ? cur : acc.plus(cur), DateTime.utc())
        .do(dateTime => {
          if (dateTime.valueOf() !== this.value) {
            this.$emit('input', dateTime.valueOf());
          }
        })
    };
  }
};
</script>

<style lang="less">
.time-picker-container {
  position: relative;

  .time-picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-transform: uppercase;
  }

  .time-picker {
    position: absolute;
    left: 0;
    background: #fff;
    z-index: 2;
    border: 1px solid #ccc;
    padding: 10px;
    overflow: hidden;
    font-size: 25px;
    text-align: center;

    .hour-selector,
    .minute-selector {
      display: inline-block;
      vertical-align: middle;
      text-align: center;
    }

    .separator {
      font-weight: bold;
    }

    &.time-picker-inline {
      position: relative;
    }
  }
}
</style>
