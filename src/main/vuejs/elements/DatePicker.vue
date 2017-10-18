<template>
  <div class="date-picker-container"
    v-click-outside="outside">
    <input v-show="!inline"
      type="text"
      ref="input"
      :placeholder="placeholder"
      :name="name"
      :value="selected | moment('L')"
      class="form-control"
      @focus="focusChanged"
      @blur="focusChanged">

    <transition name="slide-down">
      <div class="date-picker"
        v-show="display || inline"
        :class="{ 'date-picker-inline' : inline }">

        <div class="date-picker-header">
          <a class="btn btn-link waves-effect waves-light"
            @click="moveCursor(-1)">
            <i class="fa fa-arrow-left"
              aria-hidden="true"></i>
          </a>
          <h4 class="text-center">{{ focusDate.format('MMMM YYYY') }}</h4>
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
                <td v-for="(day, i) in week"
                  :key="i"
                  :class="{ 
                      'current' : selected == day.timestamp, 
                      'disabled' : day.month != focusDate.month()
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
    </transition>
  </div>
</template>
<script>
import moment from 'moment';
import { mapGetters } from 'vuex';

export default {
  props: {
    value: Number | String,
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
      focusDate: moment()
    };
  },
  computed: {
    ...mapGetters(['locale']),
    weekdays: function () {
      var wkds = moment.weekdays();
      wkds.push(wkds.shift());
      return wkds;
    }
  },
  created: function () {
    moment.locale('sk');
    this.updateCalendar();
  },
  watch: {
    focus (newVal) {
      if (newVal) this.display = true;
    },
    value (newVal) {
      this.selected = newVal;
    },
    locale (newVal) {
      moment.locale(newVal.name);
      this.updateCalendar();
    }
  },
  methods: {
    updateCalendar () {
      var start = this.focusDate.clone().startOf('month').startOf('isoweek');
      var end = this.focusDate.clone().endOf('month').endOf('isoweek');

      this.weeks = [];
      var first_week = start.week();

      while (start.diff(end, 'days') < 1) {
        var week_index = start.week() - first_week;

        if (this.weeks[week_index] == null) {
          this.weeks[week_index] = [];
        }

        this.weeks[week_index].push({
          day: start.date(),
          timestamp: start.valueOf(),
          month: start.month(),
          year: start.year()
        });
        start.add(1, 'days');
      }
    },
    moveCursor (i) {
      if (i > 0) {
        this.focusDate.add(1, 'M');
      } else {
        this.focusDate.subtract(1, 'M');
      }

      this.updateCalendar();
    },
    focusChanged (e) {
      this.focus = e.type === 'focus';
    },
    select (day) {
      this.selected = day.timestamp;
      this.display = false;
      this.$emit('input', day.timestamp);
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

      &:hover {
        background-color: lighten(@header-color, 5%);
        padding: 10px 14px;
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
    border: 1px solid #ccc;
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
        padding: 10px;
        border-bottom: 1px solid #ccc;
        text-align: center;
      }

      td {
        transition: background-color 0.2s ease;
        position: relative;

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