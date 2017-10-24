<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="primary">
        <span slot="title">Search & filter</span>
        <div class="form-group">
          <label>Search places</label>
          <input type="text" class="form-control" id="maps_autocomplete">
        </div>
        <div class="form-group">
          <label>Radius (meters)</label>
          <input type="number" class="form-control" v-model="filters.radius">
        </div>
        <a class="btn btn-block btn-primary" @click="searchPlaces(0)">Search</a>
      </panel>
    </div>
    <div class="col-md-8">
      <panel type="table" v-loading="loading">
        <span slot="title">Results</span>
        <table class="table">
          <thead>
            <tr>
              <th>Facebook id</th>
              <th>Name</th>
              <th class="text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(place, index) in places" :key="index">
              <td>
                {{ place.id }}
              </td>
              <td>
                {{ place.name }}
              </td>
              <td class="text-center"> 
                <a class="btn btn-default btn-xs" @click="importPage(place.id)">Import</a> 
              </td>
            </tr>
          </tbody>
        </table>

        <div class="text-center" v-if="!loadMoreDisabled">
          <a class="btn btn-rounded btn-default" @click="searchPlaces(++filters.offset)">Load more</a>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import ImporterApi from 'api/importer.api';
import GoogleMapsApiLoader from 'google-maps-api-loader';
import { mapActions } from 'vuex';

export default {
  name: 'page-import',
  data () {
    return {
      places: [],
      subscription: null,
      loading: false,
      loadMoreDisabled: true,
      filters: {
        radius: 1000,
        offset: 0,
        limit: 10
      },
      coords: {
        lat: 0,
        lng: 0
      }
    };
  },
  watch: {
    '$route': 'initialize'
  },
  created () {
    this.connectWM('stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/user/queue/page.import', response => {
        let pageImport = JSON.parse(response.body);

        if (pageImport.state.name === 'SUCCESS') {
          this.initializePages();
        }
      });
    });

    this.initialize();
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    ...mapActions(['initializePages']),
    initialize () {
      GoogleMapsApiLoader({
        apiKey: 'AIzaSyBKua_eTxYYK4hJf7sRKeH666HdcH3UlAg',
        libraries: [ 'places' ]
      }).then(google => {
        var input = document.getElementById('maps_autocomplete');
        const autocomplete = new google.maps.places.Autocomplete(input);

        google.maps.event.addListener(autocomplete, 'place_changed', () => {
          const location = autocomplete.getPlace().geometry.location;

          this.searchPlaces(0, {
            lat: location.lat(),
            lng: location.lng()
          });
        });
      });
    },
    searchPlaces (offset, coords) {
      this.loading = true;

      if (offset != null) {
        this.filters.offset = offset;
      }

      if (coords != null) {
        this.coords = coords;
      }

      ImporterApi.searchPlace(this.coords.lat, this.coords.lng, places => {
        this.loadMoreDisabled = places.length === 0;

        if (this.filters.offset === 0) {
          this.places = places;
        } else this.places.push(...places);

        this.loading = false;
      }, this.filters);
    },
    importPage (place_id) {
      this.sendWM(`/app/page-import`, JSON.stringify({
        facebook_id: place_id
      }));
    }
  }
};
</script>
