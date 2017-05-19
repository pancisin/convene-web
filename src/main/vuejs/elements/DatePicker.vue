<template>
  <div class="date-picker-container" v-click-outside="outside">
    <input type="text" ref="input" :placeholder="placeholder" :value="selected | moment($store.getters.locale.dateFormat)" class="form-control" @focus="focusChanged" @blur="focusChanged">
  
    <transition name="slide-down">
      <div class="date-picker" v-show="displayDatePicker">
  
        <div class="date-picker-header">
          <a class="btn btn-link" @click="moveCursor(-1)">
            <i class="fa fa-arrow-left" aria-hidden="true"></i>
          </a>
          <h4 v-text="monthName"></h4>
          <a class="btn btn-link" @click="moveCursor(1)">
            <i class="fa fa-arrow-right" aria-hidden="true"></i>
          </a>
        </div>
  
        <div class="date-picker-body">
          <table>
            <thead>
              <tr>
                <th v-for="weekday in weekdays">
                  <span>{{ weekday.substr(0, 2) }}</span>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(week, index) in weeks" :key="index">
                <td v-for="(day, index) in week" :class="{ 'current' : isCurrent(day.day, day.month), 'disabled' : day.month != month }">
                  <a class="monthday" v-text="day.day" @click="select(day)"></a>
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
import moment from "moment"
export default {
  props: ['value', 'placeholder'],
  data: function () {
    return {
      weeks: [],
      month: moment().month(),
      displayDatePicker: false,
      selected: null,
      focus: false,
    }
  },
  computed: {
    weekdays: function () {
      var wkds = moment.weekdays();
      wkds.push(wkds.shift());
      return wkds;
    },
    monthName: function () {
      return moment.months(this.month);
    }
  },
  created: function () {
    moment.locale('sk');
    this.updateCalendar();
  },
  watch: {
    focus(newVal) {
      if (newVal) this.displayDatePicker = true;
    },
    'value'(newVal) {
      this.selected = newVal;
    }
  },
  methods: {
    updateCalendar: function () {
      var start = moment().month(this.month).startOf('month').startOf('isoweek');
      var end = moment().month(this.month).endOf('month').endOf('isoweek');

      this.weeks = [];
      var first_week = start.week();

      while (start.diff(end, 'days') < 1) {
        var week_index = start.week() - first_week;

        if (this.weeks[week_index] == null)
          this.weeks[week_index] = [];

        this.weeks[week_index].push({
          day: start.date(),
          timestamp: start.valueOf(),
          month: start.month(),
        });
        start.add(1, 'days');
      }
    },
    moveCursor: function (i) {
      this.month += i;
      this.updateCalendar();
    },
    isCurrent: function (day, month) {
      return moment().date() == day && moment().month() == month;
    },
    focusChanged(e) {
      this.focus = e.type == "focus";
    },
    select(day) {
      this.selected = day.timestamp;
      this.displayDatePicker = false;
      this.$emit('input', day.timestamp);
    },
    outside: function () {
      if (focus) this.displayDatePicker = false;
    }
  }
}
</script>
<style lang="less">
.date-picker-container {
  position: relative;

  .date-picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-transform: uppercase;
    background-color: #343c49;
    padding: 10px;

    a,
    h4 {
      color: #fff;
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
    table {
      width: 100%;

      th {
        text-transform: uppercase;
        padding: 10px;
        border-bottom: 1px solid #ccc;
        text-align: center;
      }

      td {
        a {
          text-align: center;
          color: #000;
          display: block;
          padding: 10px 15px;
          transition: backgroud-color .3s ease;

          &:hover {
            background-color: #eee;
          }
        }

        &.disabled a {
          color: #ccc;
        }

        &.current {
          background-color: greenyellow;
          font-weight: bold;
        }
      }
    }
  }
}
</style>