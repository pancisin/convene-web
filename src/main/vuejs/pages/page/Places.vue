<template>
  <div class="row">
    <div class="col-md-12">
      <panel type="table">
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
                <router-link to="create-place" v-text="place.name">
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
export default {
  name: 'places',
  props: ['page'],
  data() {
    return {
      places: [],
    }
  },
  watch: {
    'page': 'getPlaces'
  },
  created() {
    this.getPlaces();
  },
  methods: {
    getPlaces() {
      if (this.page.id == null) return;

      var url = ['api/page', this.page.id, 'place'].join('/');
      this.$http.get(url).then(response => {
        this.places = response.body;
      })
    },
    deletePlace(place) {
      this.$http.delete('api/place/' + place.id).then(response => {
        this.places = this.places.filter(p => {
          return p.id != place.id;
        })
      })
    }
  }
}
</script>

<style>

</style>
