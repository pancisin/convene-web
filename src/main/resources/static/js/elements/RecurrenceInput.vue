<template>
    <div class="row">
        <div class="col-md-4">
            <div class="checkbox" v-for="day in weekDays">
                <input type="checkbox" :id="day.id + '-input'" v-model="recurrence.dayOfWeek" :value="day.id" v-on:change="updateCron">
                <label :for="day.id + '-input'" v-text="day.label"></label>
            </div>
        </div>
        <div class="col-md-4">
            <div class="checkbox" v-for="month in months">
                <input type="checkbox" :id="'month-' + month.id + '-input'" v-model="recurrence.month" :value="month.id" v-on:change="updateCron">
                <label :for="'month-' + month.id + '-input'" v-text="month.label"></label>
            </div>
        </div>
        <div class="col-md-4">
            <ul class="list-unstyled">
                <li v-for="day in occurrence">{{ day | moment("DD.MM.YYYY") }}</li>
            </ul>
        </div>
    </div>
</template>
<script>
import later from 'later';
export default {
    props: {
        value: Object  
    },
    computed: {
        occurrence: function() {
            return later.schedule({ schedules: [this.recurrence] }).next(5, new Date());
        }
    },
    created: function() {
        if (this.value != null)
            this.recurrence = this.value;
    },
    data: function() {
        return {
            weekDays: [
                {
                    id: 1,
                    label: 'Monday'
                },
                {
                    id: 2,
                    label: 'Tuesday'
                },
                {
                    id: 3,
                    label: 'Wednesday'
                },
                {
                    id: 4,
                    label: 'Thursday'
                },
                {
                    id: 5,
                    label: 'Friday'
                },
                {
                    id: 6,
                    label: 'Satuday'
                },
                {
                    id: 7,
                    label: 'Sunday'
                }
            ],
            months: [
                {
                    id: 1,
                    label: 'January'
                },
                {
                    id: 2,
                    label: 'February'
                },
                {
                    id: 3,
                    label: 'March'
                },
                {
                    id: 4,
                    label: 'April'
                },
                {
                    id: 5,
                    label: 'May'
                },
                {
                    id: 6,
                    label: 'June'
                },
                {
                    id: 7,
                    label: 'July'
                },
                {
                    id: 8,
                    label: 'August'
                },
                {
                    id: 9,
                    label: 'September'
                },
                {
                    id: 10,
                    label: 'October'
                },
                {
                    id: 11,
                    label: 'November'
                },
                {
                    id: 12,
                    label: 'December'
                },
            ],
            recurrence: {
                minute: [0],
                hour: [0],
                // day: null,
                month: [],
                dayOfWeek: [],
            }
        }
    },
    methods: {
        updateCron: function() {
            var self = this;
            this.$emit('input', self.recurrence); 
        }
    }
}
</script>