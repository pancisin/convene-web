<template>
  <input type="text"
         ref="input"
         v-bind:value="value">
</template>
<script>
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
  },
}
</script>