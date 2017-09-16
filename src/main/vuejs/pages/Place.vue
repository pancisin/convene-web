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
        <router-view :place="place"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import PlaceInjector from '../services/injectors/place.injector.js';

export default {
  name: 'place-layout',
  data () {
    return {
      injector: null,
      place: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  watch: {
    '$route': 'initializeInjector'
  },
  created () {
    this.initializeInjector();
  },
  methods: {
    initializeInjector () {
      var place_id = this.$route.params.place_id;

      this.injector = new PlaceInjector(place_id);
      this.injector.getPlace(place => {
        this.place = place;
      })
    }
  }
};
</script>

