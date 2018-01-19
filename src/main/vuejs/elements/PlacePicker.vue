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
    'value': 'initialize'
  },
  methods: {
    initialize () {
      this.$googleMapApi.load(context => {
        context.autocomplete(this.$refs.place_picker, location => {
          this.$emit('input', location);
        });

        if (this.value != null && this.value.lat != null && this.value.lng != null) {
          context.geocode(this.value, result => {
            this.$refs.place_picker.value = result.address;
          });
        } else {
          this.$refs.place_picker.value = '';
        }
      });
    }
  }
};
</script>
