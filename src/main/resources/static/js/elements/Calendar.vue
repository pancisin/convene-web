<template>
  <div>
    <table class="calendar-table">
      <thead>
        <tr>
          <th v-for="weekday in weekdays">
            {{ weekday }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="week in weeks">
          <td v-for="day in week">
            {{ day.day }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
import moment from "moment"
export default {
  props: ['events', 'month'],
  data: function () {
    return {
      days: [],
      weeks: [],
    }
  },
  computed: {
    weekdays: function () {
      return moment.weekdays();
    }
  },
  created: function () {
    var day = '2017-04-13';

    var start = moment(day).startOf('month').startOf('week');
    var end = moment(day).endOf('month').endOf('week');

    while (start.diff(end, 'days') < 1) {
      if (this.weeks[start.week()] == null)
        this.weeks[start.week()] = [];

      this.weeks[start.week()].push({
        day: start.date(),
        month: start.month(),
      });

      this.days.push({
        day: start.date(),
        month: start.month(),
      });
      start.add('days', 1);
    }
  },
  methods: {

  }
}
</script>
<style scoped>
table.calendar-table {
  width: 100%;
  table-layout: fixed;
}

table.calendar-table td {
  border: 1px solid #ccc;
  height: 140px;
  text-align: right;
  vertical-align: top;
  padding: 10px;
  transition: backround-color .3s ease;
}

table.calendar-table td:hover {
  background-color: rgba(0, 0, 0, 0.1);
}

table.calendar-table th {
  border: 1px solid #ccc;
  padding: 10px;
}
</style>