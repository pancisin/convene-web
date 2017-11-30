<template>
  <panel type="default">
    <span slot="title">Create place</span>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group" :class="{ 'has-error' : errors.name }">
          <label class="control-label">Name</label>
          <input class="form-control required" v-model="place.name" type="text">
        </div>

        <div class="form-group">
          <label class="control-label">Description</label>
          <textarea class="form-control required" v-model="place.description" rows="3"></textarea>
        </div>

        <div class="form-group">
          <label class="control-label">Capacity</label>
          <input class="form-control required" v-model="place.capacity" type="number">
        </div>

      </div>
      <div class="col-md-6">
        <label class="control-label">Location</label>
        <place-picker v-model="location" class="m-b-20"></place-picker>

        <google-map :location="location"></google-map>
      </div>
    </div>

    <div class="text-center m-t-20">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ place.name }}</button>
    </div>
  </panel>
</template>

<script>
import { GoogleMap, PlacePicker } from 'elements';
import PlaceApi from 'api/place.api';

export default {
  name: 'place-overview',
  inject: ['provider'],
  props: {
    place: {
      type: Object,
      default () {
        return {};
      }
    },
    edit: {
      type: Boolean,
      default: false
    }
  },
  components: {
    GoogleMap,
    PlacePicker
  },
  computed: {
    api () {
      return this.provider.api;
    },
    location: {
      get () {
        return {
          lat: this.place.latitude,
          lng: this.place.longitude
        };
      },
      set (value) {
        this.place = {
          ...this.place,
          latitude: value.lat,
          longitude: value.lng
        };
      }
    }
  },
  data () {
    return {
      // errors: []
    };
  },
  methods: {
    getPlace () {
      this.api.getPlace(place => {
        this.place = place;
      });
    },
    submit () {
      if (this.edit) {
        PlaceApi.putPlace(this.place.id, this.place, place => {
          this.$success('notification.place.updated', this.place.name);
          this.$emit('updated', place);
        });
      } else {
        this.api.postPlace(this.place, place => {
          this.$success('notification.place.created', this.place.name);

          this.$router.push({
            name: 'place',
            params: {
              place_id: place.id
            }
          });
        });
      }
    },
    mapUpdated (location) {
      this.place.address.latitude = location.lat;
      this.place.address.longitude = location.lng;
      this.place.address.formatted = location.address;
    }
  }
};
</script>