<template>
  <div 
    class="date-picker-container"
    v-click-outside="outside">

    <input 
      v-show="!inline"
      type="text"
      ref="input"
      :placeholder="placeholder"
      :name="name"
      :value="selected | luxon('D')"
      class="form-control"
      @focus="focusChanged"
      @blur="focusChanged">

    <slide-transition>
      <div class="date-picker"
        v-show="display || inline"
        :class="{ 'date-picker-inline' : inline }">

        <div class="date-picker-header">
          <a 
            class="btn btn-link waves-effect waves-light"
            v-stream:click="{ subject: navigate$, data: { months: -1 } }">

            <i 
              class="fa fa-arrow-left"
              aria-hidden="true">
            </i>
          </a>
          <h4 class="text-center">{{ focusDate.toFormat('LLLL yyyy') }}</h4>
          <a 
            class="btn btn-link waves-effect waves-light"
            v-stream:click="{ subject: navigate$, data: { months: 1 } }">

            <i 
              class="fa fa-arrow-right"
              aria-hidden="true">
            </i>
          </a>
        </div>

        <div class="date-picker-body">
          <table>
            <thead>
              <tr>
                <th 
                  v-for="weekday in weekdays"
                  :key="weekday">
                  <span>{{ weekday.substr(0, 2) }}</span>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(week, index) in weeks"
                :key="index">
                <td v-for="(day, index) in week"
                  :key="index"
                  :class="{ 
                      'current' : selected == day.timestamp, 
                      'disabled' : day.month != focusDate.month
                    }">
                  <a class="monthday"
                    v-text="day.day"
                    v-stream:click="{ subject: selectDay$, data: day }"></a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </slide-transition>
  </div>
</template>
<script>
import { mapGetters } from 'vuex';
import { SlideTransition } from '../functional/transitions';
import { DateTime, Info } from 'luxon';
import { Subject, Observable } from 'rxjs';

export default {
  name: 'date-time-picker',
  props: {
    value: {
      type: [ Number, String ],
      required: false,
      default () {
        return DateTime.utc().startOf('day').valueOf();
      }
    },
    placeholder: String,
    inline: Boolean,
    name: String
  },
  data: function () {
    return {
      display: false,
      focus: false
    };
  },
  computed: {
    ...mapGetters(['locale']),
    weekdays () {
      return Info.weekdays('short');
    }
  },
  components: {
    SlideTransition
  },
  watch: {
    focus (newVal) {
      if (newVal) this.display = true;
    }
  },
  subscriptions () {
    this.navigate$ = new Subject();
    this.selectDay$ = new Subject();
    const onCreate$ = this.$eventToObservable('hook:created');

    const initial$ = Observable.merge(
      onCreate$.map(() => DateTime.utc().startOf('day')),
       this.$watchAsObservable('value')
          .pluck('newValue')
          .map(timestamp => DateTime.fromMillis(parseInt(timestamp, 10), {
            zone: 'utc'
          }).startOf('day'))
    );

    const focusDate = Observable
    .merge(initial$, this.navigate$.pluck('data'))
    .scan((acc, cur) => (cur instanceof DateTime) ? cur : acc.plus(cur), {});

    const selected = Observable
      .merge(
        initial$.map(dateTime => dateTime.valueOf()),
        this.selectDay$.pluck('data', 'timestamp')
      );

    const weeks = focusDate
      .map(focus => {
        var start = focus.startOf('day').startOf('month').startOf('week');
        var end = focus.endOf('month').endOf('week');

        const diff = Math.round(end.diff(start, 'days').days);
        const daysArray = Array.from(new Array(diff), (val, index) => start.plus({ days: index }));

        return daysArray.reduce((acc, cur) => {
          const week_index = cur.weekNumber - start.weekNumber;
          if (acc[week_index] == null) {
            acc[week_index] = [];
          }
          acc[week_index].push({
            day: cur.day,
            timestamp: cur.valueOf(),
            month: cur.month
          });
          return acc;
        }, []);
      });

    return {
      focusDate,
      weeks,
      selected
    };
  },
  methods: {
    focusChanged (e) {
      this.focus = e.type === 'focus';
    },
    select (day) {
      this.selected = day.timestamp;
      const utc_timestamp = DateTime.fromMillis(day.timestamp).valueOf();

      this.display = false;
      this.$emit('input', utc_timestamp);
    },
    outside: function () {
      if (focus) this.display = false;
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';
@header-color: @color-inverse;
@import (reference) '~less/mixins.less';

.date-picker-container {
  position: relative;

  .date-picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-transform: uppercase;
    background-color: @header-color;
    padding: 10px;

    a,
    h4 {
      color: #fff;
    }

    .btn {
      transition: background-color .3s ease;
      border-radius: 100%;
      padding: 10px 14px;

      &:hover {
        background-color: lighten(@header-color, 5%);
      }
    }
  }

  .date-picker-body {
    padding: 10px;
  }

  .date-picker {
    position: absolute;
    left: 0;
    background: #fff;
    z-index: 2;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    overflow: hidden;

    &.date-picker-inline {
      position: relative;

      table th {
        padding: 10px 0;
      }
    }

    table {
      width: 100%;

      th {
        text-transform: uppercase;
        padding: 10px 0 !important;
        border-bottom: 1px solid #ccc;
        text-align: center;
        color: #666 !important;
      }

      thead {
        background: none !important;
      }

      td {
        transition: background-color 0.2s ease;
        position: relative;
        padding: 0px !important;

        a {
          text-align: center;
          color: #000;
          display: block;
          padding: 10px 0px;
          transition: color .5s ease;
        }

        &:before {
          content: '';
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          width: 0;
          height: 0;
          border-radius: 15px;

          transition: all .2s ease-out, border-radius .2s ease-in;
        }

        &:hover:before {
          background-color: #eee;
          height: 100%;
          width: 100%;
          border-radius: 0;
          z-index: -1;
        }

        &.disabled a {
          color: #ccc;
        }

        &.current {
          a {
            font-weight: bold;
            color: #fff;
          }

          &:before {
            background-color: @color-inverse !important;
            height: 100%;
            width: 100%;
            border-radius: 0;
            z-index: -1;
          }
        }
      }
    }
  }
}
</style>