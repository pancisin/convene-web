<template>
  <div class="row"
       v-if="recurrence != null">
    <div class="col-md-2">
      <div class="checkbox"
           v-for="day in weekdays">
        <input type="checkbox"
               :id="day + '-input'"
               v-model="recurrence.dayOfWeek"
               :value="weekdays.indexOf(day)">
        <label :for="day + '-input'"
               v-text="day"></label>
      </div>
    </div>
    <div class="col-md-2">
      <div class="checkbox"
           v-for="n in 5">
        <input type="checkbox"
               :id="'week-' + n + '-input'"
               v-model="recurrence.weekOfMonth"
               :value="n">
        <label :for="'week-' + n + '-input'"
               v-text="n"></label>
      </div>
    </div>
    <div class="col-md-2">
      <div class="checkbox"
           v-for="month in months">
        <input type="checkbox"
               :id="'month-' + month + '-input'"
               v-model="recurrence.month"
               :value="months.indexOf(month)">
        <label :for="'month-' + month + '-input'"
               v-text="month"></label>
      </div>
    </div>
    <div class="col-md-2">
      <ul class="list-unstyled">
        <li v-for="day in occurrence">{{ day | moment("DD.MM.YYYY") }}</li>
      </ul>
    </div>
  </div>
</template>
<script>
import moment from "moment"
import later from 'later'

export default {
  props: {
    recurrence: {
      validator: value => {
        // if (value == null)
        //   value = {
        //     minute: 0,
        //     hour: 0,
        //     day: [],
        //     month: [],
        //     dayOfWeek: [],
        //     weekOfMonth: []
        //   }
        // return value != null;
        return true;
      }
    }
  },
  computed: {
    weekdays: function () {
      var wkds = moment.weekdays();
      wkds.push(wkds.shift());
      return wkds;
    },
    months: () => {
      return moment.months();
    }
  },
}
</script>

<style lang="less">
.checkbox label {
  text-transform: capitalize;
}
</style>