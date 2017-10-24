<template>
  <panel type="primary">
    
    <ul>
      <li v-for="(place, index) in places" :key="index">
        {{ place.name }} <a class="btn btn-success btn-xs" @click="importPage(place.id)">Import</a> 
      </li>
    </ul>


  </panel>
</template>

<script>
import ImporterApi from 'api/importer.api';
export default {
  name: 'page-import',
  data () {
    return {
      places: [],
      subscription: null
    };
  },
  created () {
    this.getPlaces();

    this.connectWM('stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/user/queue/page.import', response => {
        let data = JSON.parse(response.body);

        console.warn(data);
      });
    });
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    getPlaces () {
      ImporterApi.searchPlace(48.7209956, 21.2577477, places => {
        this.places = places;
      });
    },
    importPage (place_id) {
      this.sendWM(`/app/page-import`, JSON.stringify({
        facebook_id: place_id
      }));
    }
  }
};
</script>

<style>

</style>
