<template>
  <div class="row" v-if="place != null">
    <div class="col-xs-12">
      <h3 v-text="place.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link :to="{ name: 'place.overview' }" class="list-group-item waves-effect">
          Overview
        </router-link>
        <router-link :to="{ name: 'place.gallery' }" class="list-group-item waves-effect">
          Gallery
        </router-link>
        <router-link :to="{ name: 'place.venue' }" class="list-group-item waves-effect">
          Venue editor
        </router-link>
      </div>
    </div>
    <div class="col-md-9">
      <transition name="fade-up" mode="out-in">
        <router-view :place="place" :edit="edit" @updated="placeUpdated"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import PlaceApi from 'api/place.api';

export default {
  name: 'place-layout',
  data () {
    return {
      place: null,
      edit: false
    };
  },
  watch: {
    '$route': 'getPlace'
  },
  created () {
    this.getPlace();
  },
  methods: {
    getPlace () {
      var place_id = this.$route.params.id;

      if ((this.place === null || this.place.id !== place_id) && place_id != null) {
        PlaceApi.getPlace(place_id, place => {
          this.place = place;
          this.edit = true;
        });
      } else {
        this.place = {};
      }
    },
    placeUpdated (place) {
      this.place = place;
    }
  }
};
</script>

