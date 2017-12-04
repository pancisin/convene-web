<template>
  <div>
    <div class="calendar-header">
      <a class="btn btn-link" @click="moveCursor(-1)">
        <i class="fa fa-arrow-left" aria-hidden="true"></i> {{ $t('calendar.prev') }}</a>
      <h3 class="text-center">{{ focusDate.format('MMMM YYYY') }}</h3>
      <a class="btn btn-link" @click="moveCursor(1)">{{ $t('calendar.next') }}
        <i class="fa fa-arrow-right" aria-hidden="true"></i>
      </a>
    </div>
  
    <table class="calendar-table">
      <thead>
        <tr>
          <th v-for="(weekday, index) in weekdays" :key="index">
            <span v-text="weekday"></span>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(week, row) in weeks" :key="row">
          <td v-for="(day, column) in week" 
            :key="column" 
            :class="{ 
              'current' : isCurrent(day.day, day.month), 
              'disabled' : day.month != focusDate.month() 
            }">
            <span class="monthday" v-text="day.day"></span>
            
            <ul @dragover.prevent @drop="drop(day)">
              <li v-for="(event, index) in day.events" :key="index"  @contextmenu.prevent="$refs.menu.open($event, event)">
                <router-link  :to="{ name: 'event', params: { id: event.id } }">
                  {{ event.name }}
                </router-link>
              </li>
              <li v-if="day.month === focusDate.month()">
                <a @click="selectDate(day)" class="create-event">
                  <i class="fa fa-plus"></i>
                </a>
              </li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>

    <context-menu ref="menu">
      <template slot-scope="props">
        <ul>
          <li>
            <router-link :to="{ name: 'event.public', params: { id: props.data.id } }">
              Go to event
            </router-link>
          </li>
          <li class="separator"></li>
          <li>
            <router-link :to="{ name: 'event.overview', params: { id: props.data.id } }">
              Overview
            </router-link>
          </li>
          <li>
            <router-link :to="{ name: 'event.programme', params: { id: props.data.id } }">
              Programme
            </router-link>
          </li>
          <li>
            <router-link :to="{ name: 'event.attendees', params: { id: props.data.id } }">
              Attendees
            </router-link>
          </li>
          <li class="separator"></li>
          <li :class="{ 'disabled' : !editable }">
            <router-link to="events/create">
              Create event
            </router-link>
          </li>
          <li class="separator"></li>
          <li :class="{ 'disabled' : !editable }">
            <a @click="deleteEvent(props.data)">
              Delete
            </a>
          </li>
        </ul>
      </template>
    </context-menu>
  </div>
</template>

<script>
import moment from 'moment';
import EventApi from 'api/event.api';
export default {
  props: {
    events: {
      type: Array,
      default () {
        return [];
      }
    },
    editable: Boolean
  },
  data: function () {
    return {
      weeks: [],
      focusDate: moment()
    };
  },
  computed: {
    weekdays: function () {
      var wkds = moment.weekdays();
      wkds.push(wkds.shift());
      return wkds;
    }
  },
  watch: {
    'events': 'updateCalendar'
  },
  created: function () {
    this.updateCalendar();
    const query_focus = parseInt(this.$route.query.focus, 10);
    if (query_focus) {
      this.focusDate = moment(query_focus);
    }

    this.navigate();
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

        const events = this.events.filter(e => {
          return moment(e.date).startOf('day').valueOf() === start.startOf('day').valueOf();
        });

        this.weeks[week_index].push({
          day: start.date(),
          timestamp: start.valueOf(),
          month: start.month(),
          year: start.year(),
          events
        });
        start.add(1, 'days');
      }
    },
    navigate () {
      this.$emit('navigate', {
        from: this.focusDate.clone().startOf('month').startOf('isoWeek').valueOf(),
        to: this.focusDate.clone().endOf('month').endOf('isoWeek').valueOf()
      });
    },
    moveCursor (i) {
      if (i > 0) {
        this.focusDate.add(1, 'M');
      } else {
        this.focusDate.subtract(1, 'M');
      }

      this.updateCalendar();
      this.navigate();
      this.$router.replace({
        query: {
          focus: this.focusDate.valueOf()
        }
      });
    },
    deleteEvent (event) {
      this.$prompt('notification.event.delete_prompt', () => {
        EventApi.deleteEvent(event.id, result => {
          this.paginator.content = this.paginator.content.filter(e => {
            return e.id !== event.id;
          });
          this.updateCalendar();
        });
      });
    },
    isCurrent: function (day, month) {
      return moment().date() === day && moment().month() === month;
    },
    selectDate (day) {
      this.$emit('selectDate', day.timestamp);
    }
  }
};
</script>

<style lang="less">
@import (reference) '~less/variables.less';

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
      background: rgba(31, 171, 137, 0.2);
    }

    &.disabled {
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
        margin-bottom: 1px;
        overflow: hidden;

        a {
          background-color: @color-primary;
          padding: 5px 10px;
          color: #fff;
          font-size: 12px;
          line-height: 17px;
          display: block;
          transition: background-color .3s ease;

          &:hover {
            background-color: @color-primary-active;
          }

          &.create-event {
            text-align: center;
            background-color: @color-inverse;
            opacity: 0;
            transition: opacity .5s ease, transform .2s ease, background-color .3s ease;
            transform: translateY(-100%);

            &:hover {
              background-color: @color-inverse-active;
            }
          }
        }
      }
    }

    &:hover:not(.disabled) {
      background-color: rgba(0, 0, 0, 0.1);

      ul li a.create-event {
        transform: translateY(0px);
        opacity: 1;
      }
    }
  }
}
</style>