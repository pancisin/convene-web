<template>
  <panel type="primary">
    <label>Search places by region</label>
    <input type="text" class="form-control" id="maps_autocomplete">

    <ul v-loading="loading" class="m-t-15">
      <li v-for="(place, index) in places" :key="index">
        {{ place.name }} <a class="btn btn-success btn-xs" @click="importPage(place.id)">Import</a> 
      </li>
    </ul>
  </panel>
</template>

<script>
import ImporterApi from 'api/importer.api';
import GoogleMapsApiLoader from 'google-maps-api-loader';

export default {
  name: 'page-import',
  data () {
    return {
      places: [],
      subscription: null,
      selected: null,
      loading: false
    };
  },
  watch: {
    selected(newVal) {
      console.log(newVal);
    }
  },
  created () {
    this.connectWM('stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/user/queue/page.import', response => {
        let data = JSON.parse(response.body);

        console.warn(data);
      });
    });

    GoogleMapsApiLoader({
      apiKey: 'AIzaSyBKua_eTxYYK4hJf7sRKeH666HdcH3UlAg',
      libraries: [ 'places' ]
    }).then(google => {
      var input = document.getElementById('maps_autocomplete');
      const autocomplete = new google.maps.places.Autocomplete(input);

      google.maps.event.addListener(autocomplete, 'place_changed', () => {
        const location = autocomplete.getPlace().geometry.location;

        this.loading = true;
        ImporterApi.searchPlace(location.lat(), location.lng(), places => {
          this.places = places;
          this.loading = false;
        });
      });
    }, err => {
      console.error(err);
    });
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    importPage (place_id) {
      this.sendWM(`/app/page-import`, JSON.stringify({
        facebook_id: place_id
      }));
    }
  }
};
</script>
