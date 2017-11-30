<template>
  <input type="text" class="form-control" ref="place_picker">
</template>

<script>
export default {
  name: 'place-picker',
  props: {
    value: {
      type: Object,
      default () {
        return {
          lat: null,
          lng: null
        };
      }
    },
    displayMap: Boolean
  },
  created () {
    this.initialize();
  },
  watch: {
    'route': 'initialize'
  },
  methods: {
    initialize () {
      this.$googleMapApi.load(context => {
        context.autocomplete(this.$refs.place_picker, location => {
          this.$emit('input', location);
        });
      });
    }
  }
};
</script>

<style>

</style>
