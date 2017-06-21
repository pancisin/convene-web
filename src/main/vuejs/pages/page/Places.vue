<template>
  <div class="row">
    <div class="col-md-12">
      <panel type="table" v-loading="loading">
        <span slot="title">{{ $t('admin.page.places') }}</span>
  
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Name</th>
              <th>Capacity</th>
              <th>City</th>
              <th>Street</th>
              <th class="text-center">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="place in places">
              <td>
                <router-link :to="{ name: 'place', params: { id: place.id }}" v-text="place.name">
                </router-link>
              </td>
              <td v-text="place.capacity"></td>
              <td v-text="place.address.city"></td>
              <td>{{ place.address.street + " " + place.address.number }}</td>
              <td class="text-center">
                <a @click="deletePlace(place)" class="btn btn-rounded btn-xs btn-danger">
                  <i class="fa fa-trash"></i>
                </a>
              </td>
            </tr>
          </tbody>
        </table>
  
        <div class="text-center">
          <router-link to="create-place" class="btn btn-default btn-rounded text-center">
            Create place
          </router-link>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import PlaceApi from '../../services/api/place.api.js'
export default {
  name: 'places',
  inject: ['api'],
  data() {
    return {
      places: [],
      loading: false,
    }
  },
  created() {
    this.getPlaces();
  },
  methods: {
    getPlaces() {
      this.loading = true;
      this.api.getPlaces(places => {
        this.places = places;
        this.loading = false;
      })
    },
    deletePlace(place) {
      PlaceApi.deletePlace(place.id, result => {
        this.places = this.places.filter(p => {
          return p.id != place.id;
        })
      })
    }
  }
}
</script>