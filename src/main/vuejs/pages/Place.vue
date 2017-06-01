<template>
  <div class="row">
    <div class="col-xs-12">
      <h3 v-text="place.name" class="page-title"></h3>
    </div>
    <div class="col-md-3">
      <div class="list-group mail-list">
        <router-link to="overview" class="list-group-item waves-effect">
          Overview
        </router-link>
        <router-link to="gallery" class="list-group-item waves-effect">
          Gallery
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
export default {
  name: 'place-layout',
  data() {
    return {
      place: {},
      edit: false,
    }
  },
  watch: {
    '$route': 'getPlace'
  },
  created() {
    this.getPlace();
  },
  methods: {
    getPlace() {
      var place_id = this.$route.params.id;

      if (this.place.id != null && this.place.id == place_id)
        return;

      if (place_id != null) {
        this.$http.get('api/place/' + place_id).then(response => {
          this.place = response.body;
          this.edit = true;
        })
      } else
        this.place = {};
    },
    placeUpdated(place) {
      this.place = place;
    }
  }
}
</script>

