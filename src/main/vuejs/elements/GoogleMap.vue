<template>
  <div class="map-canvas" ref="google_map_canvas"></div>
</template>

<script>
import gmapStyle from './gmapStyle.js';

export default {
  name: 'google-map',
  data () {
    return {
      map: null,
      loading: false
    };
  },
  props: {
    location: {
      type: Object,
      default () {
        return {
          lat: null,
          lng: null
        };
      }
    }
  },
  watch: {
    location: {
      handler (newVal) {
        if (this.map != null) {
          this.map.panTo(newVal);
        }
      },
      deep: true
    }
  },
  created () {
    this.loading = true;
    this.$googleMapApi.load(context => {
      this.map = context.map(this.$refs.google_map_canvas, {
        disableDefaultUI: true,
        styles: gmapStyle,
        draggable: false,
        center: this.location
      });

      context.marker({
        position: this.location,
        map: this.map
      });

      this.loading = false;
    });
  }
};
</script>

<style>
.map-canvas {
  height: 200px;
  width: 100%;
}
</style>
