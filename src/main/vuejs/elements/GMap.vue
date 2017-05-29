<template>
  <div id="map-canvas" class="map-canvas">
  </div>
</template>

<script>
import GoogleMapsApiLoader from 'google-maps-api-loader'
import gmapStyle from './gmapStyle.js'
export default {
  name: 'google-map',
  data() {
    return {
      map: null,
    }
  },
  props: {
    lat: Number,
    lng: Number
  },
  computed: {
    uid() {
      return this._uid;
    }
  },
  created() {
    GoogleMapsApiLoader({
      apiKey: 'AIzaSyBKua_eTxYYK4hJf7sRKeH666HdcH3UlAg'
    }).then(google => {
      var center = {
        lat: this.lat,
        lng: this.lng
      };

      this.map = new google.maps.Map(document.getElementById('map-canvas'), {
        zoom: 16,
        center: center,
        styles: gmapStyle
      });
    }, err => {
      console.error(err);
    });
  },
  methods: {

  }
}
</script>

<style>
.map-canvas {
  height: 200px;
  width: 100%;
}
</style>
