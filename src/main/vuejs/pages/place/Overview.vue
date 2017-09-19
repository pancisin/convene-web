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
          <input class="form-control required" v-model="place.description" type="text">
        </div>

        <div class="form-group">
          <label class="control-label">Capacity</label>
          <input class="form-control required" v-model="place.capacity" type="number">
        </div>

      </div>
      <div class="col-md-6" v-if="place.address != null">
        <div class="form-group" :class="{ 'has-error' : errors.name }">
          <label class="control-label">City</label>
          <input class="form-control required" v-model="place.address.city" type="text">
        </div>

        <div class="row">
          <div class="col-xs-8">
            <div class="form-group" :class="{ 'has-error' : errors.name }">
              <label class="control-label">Street</label>
              <input class="form-control required" v-model="place.address.street" type="text">
            </div>
          </div>
          <div class="col-xs-4">
            <div class="form-group">
              <label class="control-label">Number</label>
              <input class="form-control required" v-model="place.address.number" type="number">
            </div>
          </div>
        </div>

        <div class="form-group" :class="{ 'has-error' : errors.state }">
          <label class="control-label">State</label>
          <input class="form-control required" v-model="place.address.state" type="text">
        </div>

      </div>
    </div>

    <label>Please verify that map points to the correct place</label>
    <g-map :address="place.address" :lat="place.address.latitude" :lng="place.address.longitude" @updated="mapUpdated"></g-map>

    <div class="text-center m-t-20">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ place.name }}</button>
    </div>
  </panel>
</template>

<script>
import { GMap } from 'elements';
import PlaceApi from 'api/place.api';

export default {
  name: 'place-overview',
  inject: ['provider'],
  props: {
    place: {
      type: Object,
      default () {
        return {
          address: {}
        };
      }
    },
    edit: {
      type: Boolean,
      default: false
    }
  },
  components: {
    GMap
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  data () {
    return {
      errors: []
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
          this.$success('Success !', 'Place ' + this.place.name + ' has been updated.');
          this.$emit('updated', place);
        });
      } else {
        this.api.postPlace(this.place, place => {
          this.$success('Success !', 'Place ' + this.place.name + ' has been created.');

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