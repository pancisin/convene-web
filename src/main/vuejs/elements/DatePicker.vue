<template>
  <div 
    class="date-picker-container"
    v-click-outside="outside">

    <input v-show="!inline"
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
          <a class="btn btn-link waves-effect waves-light"
            @click="moveCursor(-1)">
            <i class="fa fa-arrow-left"
              aria-hidden="true"></i>
          </a>
          <h4 class="text-center">{{ focusDate.valueOf() | luxon('LLLL yyyy') }}</h4>
          <a class="btn btn-link waves-effect waves-light"
            @click="moveCursor(1)">
            <i class="fa fa-arrow-right"
              aria-hidden="true"></i>
          </a>
        </div>

        <div class="date-picker-body">
          <table>
            <thead>
              <tr>
                <th v-for="weekday in weekdays"
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
                    @click="select(day)"></a>
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

export default {
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
      weeks: [],
      display: false,
      selected: null,
      focus: false,
      focusDate: {}
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
  created () {
    this.updateFocusDate(this.value);
  },
  watch: {
    focus (newVal) {
      if (newVal) this.display = true;
    },
    value: 'updateFocusDate'
  },
  methods: {
    updateFocusDate (timestamp) {
      var dateTime = DateTime.utc().startOf('day');

      if (timestamp != null) {
        dateTime = DateTime.fromMillis(parseInt(timestamp, 10), {
          zone: 'utc'
        }).startOf('day');
      }

      if (dateTime.isValid) {
        this.selected = dateTime.valueOf();
        this.focusDate = dateTime;
      }

      this.updateCalendar();
    },
    updateCalendar () {
      if (this.focusDate.startOf === null || this.focusDate.endOf === null) {
        return;
      }

      var start = this.focusDate.startOf('month').startOf('week');
      var end = this.focusDate.endOf('month').endOf('week');

      this.weeks = [];
      const first_week = start.weekNumber;

      while (start.diff(end, 'days').days <= 0) {
        var week_index = start.weekNumber - first_week;

        if (this.weeks[week_index] == null) {
          this.weeks[week_index] = [];
        }

        this.weeks[week_index].push({
          day: start.day,
          timestamp: start.startOf('day').valueOf(),
          month: start.month
        });

        start = start.plus({
          days: 1
        });
      }
    },
    moveCursor (i) {
      this.focusDate = this.focusDate.plus({
        months: i
      });

      this.updateCalendar();
    },
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