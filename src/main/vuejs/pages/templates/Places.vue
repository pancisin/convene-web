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
            </tr>
          </thead>
          <tbody>
            <tr v-for="place in places" :key="place.id" @contextmenu.prevent="$refs.menu.open($event, place)">
              <td>
                <router-link :to="{ name: 'place', params: { place_id: place.id }}" v-text="place.name">
                </router-link>
              </td>
              <td v-text="place.capacity"></td>
              <td v-text="place.address.city"></td>
              <td>{{ place.address.street + " " + place.address.number }}</td>
            </tr>
          </tbody>
        </table>

        <context-menu ref="menu">
          <template scope="props">
            <ul>
              <li v-if="editable">
                <router-link :to="{ name: 'place', params: { place_id: props.data.id } }">
                  Edit
                </router-link>
              </li>
              <li v-if="editable">
                <router-link :to="{ name: 'place.venue', params: { place_id: props.data.id } }">
                  Edit in venue editor
                </router-link>
              </li>
              <li v-if="editable">
                <a @click="deletePlace(props.data)">
                  Delete
                </a>
              </li>
              <li class="separator"></li>
              <li>
                <router-link :to="{ name: 'conference.article.create' }">
                  Create place
                </router-link>
              </li>
            </ul>
          </template>
        </context-menu>

        <div class="text-center" v-if="editable">
          <router-link to="create-place" class="btn btn-default btn-rounded text-center">
            Create place
          </router-link>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import PlaceApi from 'api/place.api';
export default {
  name: 'places',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
  data () {
    return {
      places: [],
      loading: false
    };
  },
  created () {
    this.getPlaces();
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  watch: {
    'api': 'getPlaces'
  },
  methods: {
    getPlaces () {
      this.loading = true;
      if (this.api != null) {
        this.api.getPlaces(places => {
          this.places = places;
          this.loading = false;
        });
      }
    },
    deletePlace (place) {
      this.$prompt('notification.place.delete_prompt', () => {
        PlaceApi.deletePlace(place.id, result => {
          this.places = this.places.filter(p => {
            return p.id !== place.id;
          });
        });
      });
    }
  }
};
</script>