<template>
    <div>
       <div class="checkbox" v-for="day in weekDays">
        <input type="checkbox" :id="day.id + '-input'" v-model="recurrence.dw" :value="day.id" v-on:change="updateCron">
        <label :for="day.id + '-input'" v-text="day.label"></label>
      </div>
    </div>
</template>
<script>
import later from 'later';
export default {
    props: {
        value: String  
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
            recurrence: {
                dw: [],
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