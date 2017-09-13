<template>
  <panel type="primary" class="panel-nopadding" v-loading="loading">
    <span slot="title">Venue editor</span>
    <venue-editor :json="venue" @submit="submitVenue"></venue-editor>
  </panel>
</template>

<script>
import { VenueEditor } from 'elements';
import PlaceApi from 'api/place.api';

export default {
  name: 'place-venue-editor',
  props: ['place'],
  components: {
    VenueEditor
  },
  data () {
    return {
      venue: null,
      loading: false
    };
  },
  watch: {
    '$route': 'getVenue'
  },
  created () {
    this.getVenue();
  },
  methods: {
    getVenue () {
      if (this.place.venueJsonUrl != null) {
        this.loading = true;
        this.$http.get(this.place.venueJsonUrl).then(response => {
          this.venue = JSON.stringify(response.body);
          this.loading = false;
        });
      }
    },
    submitVenue (venue_json) {
      PlaceApi.patchVenue(this.place.id, venue_json, result => {
        this.$success('venue data saved !');
      });
    }
  }
};
</script>
