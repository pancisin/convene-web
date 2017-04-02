<template>
  <input type="text"
         ref="input"
         v-bind:value="value"
         v-on:click="picker.open()">
</template>
<script>
import MaterialDateTimePicker from 'material-datetime-picker';

export default {
  props: {
    value: String
  },
  data: function () {
    return {
      picker: null,
    }
  },
  created: function () {
    var self = this;
    this.picker = new MaterialDateTimePicker().on('submit', (val) => self.$emit('input', val));
  },
}
</script>