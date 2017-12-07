<template>
  <div class="row" v-if="place != null">
    <div class="col-xs-12">
      <h3 v-text="place.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link :to="{ name: 'place.overview' }" class="list-group-item waves-effect">
          <i class="fa fa-dashboard"></i>
          Overview
        </router-link>
        <router-link :to="{ name: 'place.gallery' }" class="list-group-item waves-effect">
          <i class="fa fa-picture-o"></i>
          Gallery
        </router-link>
        <router-link :to="{ name: 'place.venue' }" class="list-group-item waves-effect">
          <i class="fa fa-pencil"></i>
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
import PlaceApi from 'api/place.api';
import InjectorGenerator from '../services/InjectorGenerator';

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

      this.injector = InjectorGenerator.generate(PlaceApi, place_id);
      this.injector.getPlace(place => {
        this.place = place;
      });
    }
  }
};
</script>

