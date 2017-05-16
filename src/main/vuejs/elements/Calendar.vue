<template>
  <div>
    <div class="calendar-header">
      <a class="btn btn-link" @click="moveCursor(-1)">
        <i class="fa fa-arrow-left" aria-hidden="true"></i> {{ $t('calendar.prev') }}</a>
      <h3 v-text="monthName"></h3>
      <a class="btn btn-link" @click="moveCursor(1)">{{ $t('calendar.next') }}
        <i class="fa fa-arrow-right" aria-hidden="true"></i>
      </a>
    </div>
  
    <table class="calendar-table">
      <thead>
        <tr>
          <th v-for="weekday in weekdays">
            <span v-text="weekday"></span>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(week, index) in weeks" :key="index">
          <td v-for="(day, index) in week" :class="{ 'current' : isCurrent(day.day, day.month), 'disabled' : day.month != month }">
            <span class="monthday" v-text="day.day"></span>
            <ul @dragover.prevent @drop="drop(day)">
              <li v-for="instance in day.instances" :class="{ 'edited' : instance.clause != null }" draggable="true" v-on:dragstart="dragstart(instance, day)">
  
                <i v-show="instance.loading" class="fa fa-spinner fa-spin fa-fw"></i> {{ instance.duty.location }}
                <router-link :to="'/instance/' + instance.duty.id + '/' + instance.date">
                  <i class="fa fa-pencil" aria-hidden="true"></i>
                </router-link>
              </li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import moment from "moment"
export default {
  data: function () {
    return {
      weeks: [],
      month: moment().month(),
      dragElem: null,
      dragSource: null,
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
    this.updateCalendar();
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
          // instances: instances.filter(elem => {
          //   return moment(elem.date).format('YYYY-MM-DD') == start.format('YYYY-MM-DD');
          // })
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
    dragstart: function (inst, source_day) {
      this.dragElem = inst;
      this.dragSource = source_day;
    },
    drop: function (day) {
      var self = this;
      if (day.instances.filter(elem => {
        return elem.duty.id == self.dragElem.duty.id;
      }) == 0) {
        day.instances.push(this.dragElem);
        this.dragSource.instances = this.dragSource.instances.filter(elem => {
          return elem.duty.id != self.dragElem.duty.id;
        });
        this.dragElem.loading = true;

        this.storeClause(this.dragElem, day.timestamp).then(clause => {
          this.dragElem.clause = clause;
          this.dragElem.loading = false;
          this.dragSource = null;
          this.dragElem = null;
          this.$forceUpdate();
        });
      }
    },
    storeClause: function (instance, alternative_date) {
      return new Promise((resolve, reject) => {
        var url = ['api/duty', instance.duty.id, 'clause'].join('/');

        this.$http.post(url, {
          id: instance.clause != null ? instance.clause.id : null,
          primaryDate: instance.clause != null ? instance.clause.primaryDate : instance.date,
          alternativeDate: alternative_date,
        }).then(response => {
          resolve(response.body);
        }, response => {
          reject();
        });
      })
    },
    editInstance: function (instance) {
      this.$emit('edit-instance', instance);
    }
  }
}
</script>

<style lang="less" scoped>
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-transform: uppercase;
}

table.calendar-table {
  width: 100%;
  table-layout: fixed;

  th {
    border: 1px solid #ccc;
    padding: 10px;
    text-transform: uppercase;
  }

  td {
    border: 1px solid #ccc;
    vertical-align: top;
    transition: background-color .3s ease;
    position: relative;
    padding-top: 40px;

    &.current {
      background: #e0ffae;
    }

    &.disabled {
      position: relative;

      &:after {
        content: '';
        background: rgba(238, 237, 237, 0.81);
        top: 0;
        left: 0;
        bottom: 0;
        width: 100%;
        position: absolute;
      }
    }

    .monthday {
      position: absolute;
      top: 10px;
      right: 10px;
    }

    ul {
      padding-left: 0;
      list-style: none;
      min-height: 50px;

      li {
        background: #51d17d;
        padding: 5px 10px;
        margin-bottom: 1px;
        color: white;

        &.edited {
          background: #ef9a1c;
        }

        .fa {
          margin-right: 10px;
        }

        a {
          color: #fff;
          float: right;
          display: none;
        }

        &:hover a {
          display: block;
        }
      }
    }

    &:hover {
      background-color: rgba(0, 0, 0, 0.1);
    }
  }
}
</style>