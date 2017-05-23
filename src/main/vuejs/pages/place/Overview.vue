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
  
    <div class="text-center">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ place.name }}</button>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'place-overview',
  props:
  {
    place: {
      type: Object,
      default() {
        return {
          address: {},
        };
      }
    },
    edit: {
      type: Boolean,
      default: false
    },
    page_id: String,
    conference_id: String
  },
  data() {
    return {
      errors: [],
    }
  },
  methods: {
    submit() {
      if (this.edit) {
        this.$http.put('api/place/' + this.place.id, this.place).then(response => {
          this.$emit('updated', response.body);
        })
      } else {
        var url = ['api/page', this.page_id, 'place'].join('/');

        this.$http.post(url, this.place).then(response => {
          this.place = response.body;
          this.$router.push({
            name: 'place',
            params: {
              id: this.place.id,
            }
          })
        })
      }
    }
  }
}
</script>