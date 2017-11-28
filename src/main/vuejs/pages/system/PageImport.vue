<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="primary">
        <span slot="title">Search & filter</span> 
        <div class="form-group">
          <label>Keyword</label>
          <input 
            type="text" 
            class="form-control" 
            v-model="filters.q" 
            @keyup="onKeywordChange($event)" >
        </div>
        <div class="form-group">
          <label>Search places</label>
          <input type="text" class="form-control" id="maps_autocomplete">
        </div>
        <div class="form-group">
          <label>Radius (meters)</label>
          <input type="number" class="form-control" v-model="filters.radius">
        </div>

        <div class="form-group">
          <label>Import state</label>
          <div class="radio radio-primary" v-for="(show, index) in showFilters" :key="index">
            <input :id="`radio-${show}`" type="radio" v-model="filters.show" :value="show">
            <label :for="`radio-${show}`">
              {{ $t(`filter.${show}`) }} 
            </label>
          </div>
        </div>
        <a class="btn btn-block btn-primary" @click="searchPlaces(null)">Search</a>
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
            <tr v-for="(place, index) in paginator.content" :key="index">
              <td>
                {{ place.id }}
              </td>
              <td>
                {{ place.name }}
              </td>
              <td class="text-center"> 
                <a v-if="place.imported != 'true'" class="btn btn-default btn-xs" @click="importPage(place.id)">Import</a> 
                <a v-else class="btn btn-inverse btn-xs" @click="updatePage(place.pageImportId)">Update</a> 
              </td>
            </tr>
          </tbody>
        </table>

        <div class="text-center" v-if="paginator.nextCursor != ''">
          <a class="btn btn-rounded btn-default" @click="searchPlaces(paginator.nextCursor)">Load more</a>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import debounce from 'debounce';
import ImporterApi from 'api/importer.api';
import GoogleMapsApiLoader from 'google-maps-api-loader';
import { mapActions } from 'vuex';

export default {
  name: 'page-import',
  data () {
    return {
      subscription: null,
      loading: false,
      paginator: {},
      filters: {
        radius: 1000,
        limit: 10,
        show: 'all'
      },
      coords: {
        lat: null,
        lng: null
      }
    };
  },
  computed: {
    showFilters () {
      return [
        'all', 'imported', 'notimported'
      ];
    }
  },
  watch: {
    '$route': 'initialize'
  },
  created () {
    this.connectWM('/stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/user/queue/page.import', response => {
        let pageImport = JSON.parse(response.body);

        if (pageImport.state.name === 'SUCCESS') {
          this.initializePages();
          this.updateSearchResults(pageImport);
        }
      });
    });

    this.initialize();
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    ...mapActions(['initializePages', 'updatePages']),
    initialize () {
      GoogleMapsApiLoader({
        apiKey: 'AIzaSyBKua_eTxYYK4hJf7sRKeH666HdcH3UlAg',
        libraries: [ 'places' ]
      }).then(google => {
        var input = document.getElementById('maps_autocomplete');
        const autocomplete = new google.maps.places.Autocomplete(input);

        google.maps.event.addListener(autocomplete, 'place_changed', () => {
          const location = autocomplete.getPlace().geometry.location;

          this.searchPlaces(null, {
            lat: location.lat(),
            lng: location.lng()
          });
        });
      });
    },
    searchPlaces (after, coords) {
      this.loading = true;

      this.filters.after = after;
      if (coords != null) {
        this.coords = coords;
      }

      ImporterApi.searchPlace(this.coords.lat, this.coords.lng, paginator => {
        if (this.filters.after != null) {
          this.paginator = {
            ...paginator,
            content: this.paginator.content.concat(paginator.content)
          };
        } else this.paginator = paginator;
        this.loading = false;
      }, this.filters);
    },
    onKeywordChange: debounce(function () {
      this.searchPlaces();
    }, 500),
    importPage (place_id) {
      this.sendWM(`/app/page-import`, JSON.stringify({
        facebook_id: place_id
      }));
    },
    updatePage (importId) {
      this.sendWM('/app/import-replay', JSON.stringify({
        id: importId
      }));
    },
    updateSearchResults (pageImport) {
      this.paginator.content = this.paginator.content.map(place => {
        if (place.id === pageImport.sourceId) {
          return {
            ...place,
            imported: 'true',
            pageImportId: pageImport.id
          };
        } else {
          return place;
        }
      });
    }
  }
};
</script>
