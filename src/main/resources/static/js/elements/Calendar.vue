<template>
  <div>
    <div class="calendar-header">
      <a class="btn btn-primary"
         @click="moveCursor(-1)"><i class="fa fa-arrow-left" aria-hidden="true"></i> {{ $t('calendar.prev') }}</a>
      <h3 v-text="monthName"></h3>
      <a class="btn btn-primary"
         @click="moveCursor(1)">{{ $t('calendar.next') }} <i class="fa fa-arrow-right" aria-hidden="true"></i></a>
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
        <tr v-for="week in weeks">
          <td v-for="day in week" :class="{ 'current' : isCurrent(day.day, day.month) }">
            <span class="monthday"
                  v-text="day.day"></span>
            <ul>
              <li v-for="instance in day.instances"
                  v-text="instance.duty.location"
                  :class="{ 'edited' : instance.clause != null }">
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
  methods: {
    updateCalendar: function () {
      var start = moment().month(this.month).startOf('month').startOf('isoweek');
      var end = moment().month(this.month).endOf('month').endOf('isoweek');

      var url = ['api/company', this.$store.getters.company_id, 'instances', start.format('YYYY-MM-DD'), end.format('YYYY-MM-DD')].join('/');
      this.$http.get(url).then(response => {
        var instances = response.body;
        this.weeks = [];

        while (start.diff(end, 'days') < 1) {
          if (this.weeks[start.week()] == null)
            this.weeks[start.week()] = [];

          this.weeks[start.week()].push({
            day: start.date(),
            month: start.month(),
            instances: instances.filter(elem => {
              return moment(elem.date).date() == start.date();
            })
          });

          start.add(1, 'days');
        }

      });
    },
    moveCursor: function (i) {
      this.month += i;
      this.updateCalendar();
    },
    isCurrent: function(day, month) {
      return moment().date() == day && moment().month() == month;
    }
  }
}
</script>

<style lang="less">
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
    height: 140px;
    vertical-align: top;
    transition: background-color .3s ease;
    position: relative;
    padding-top: 40px;

    &.current {
      background: #e0ffae;
    }

    .monthday {
      position: absolute;
      top: 10px;
      right: 10px;
    }

    ul {
      padding-left: 0;
      list-style: none;

      li {
        background: #51d17d;
        padding: 5px 10px;
        margin-bottom: 1px;
        color: white;

        &.edited {
          background: #ef9a1c;
        }
      }
    }

    &:hover {
      background-color: rgba(0, 0, 0, 0.1);
    }
  }
}
</style>