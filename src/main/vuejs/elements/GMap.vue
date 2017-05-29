<template>
  <div id="map-canvas" class="map-canvas">
  </div>
</template>

<script>
import GoogleMapsApiLoader from 'google-maps-api-loader'
import gmapStyle from './gmapStyle.js'
import debounce from 'debounce'
export default {
  name: 'google-map',
  data() {
    return {
      map: null,
      google: null,
    }
  },
  props: {
    lat: Number,
    lng: Number,
    address: {
      type: Object,
      default: null
    }
  },
  computed: {
    uid() {
      return this._uid;
    }
  },
  watch: {
    address: {
      handler() {
        debounce(this.geocodeAddress, 3000)();
      },
      deep: true
    }
  },
  created() {
    GoogleMapsApiLoader({
      apiKey: 'AIzaSyBKua_eTxYYK4hJf7sRKeH666HdcH3UlAg'
    }).then(google => {
      this.google = google;

      if (this.address == null)
        this.initializeMap();
      else {
        this.geocodeAddress();
      }
    }, err => {
      console.error(err);
    });
  },
  methods: {
    initializeMap() {
      var center = {
        lat: this.lat,
        lng: this.lng
      };

      this.map = new this.google.maps.Map(document.getElementById('map-canvas'), {
        zoom: 16,
        center: center,
        styles: gmapStyle
      });
    },
    geocodeAddress() {
      var geocoder = new google.maps.Geocoder();
      console.error('geocoding');
      geocoder.geocode({
        address: this.address.street + " " + this.address.number,
      }, (result, status) => {
        var location = result[0].geometry.location;
        this.$emit('updated', {
          lat: location.lat(),
          lng: location.lng()
        })
        this.initializeMap();
      })
    }
  }
}
</script>

<style>
.map-canvas {
  height: 200px;
  width: 100%;
}
</style>
