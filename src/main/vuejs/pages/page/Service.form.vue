<template>
  <div>
    <div class="form-group">
      <label class="control-label">Name: </label>
      <input class="form-control required" v-model="service.name" type="text">
    </div>
  
    <div class="form-group">
      <label class="control-label">Detail: </label>
      <input class="form-control required" v-model="service.detail" type="text">
    </div>
  
    <div class="form-group">
      <label class="control-label">Unit: </label>
      <input class="form-control required" v-model="service.unit" type="text">
    </div>
  
    <div class="form-group">
      <label class="control-label">Price per unit: </label>
      <input class="form-control required" v-model="service.pricePerUnit" type="text">
    </div>
  
    <button @click="submit" class="btn btn-rounded btn-success">Submit</button>
  </div>
</template>

<script>
export default {
  name: 'service-form',
  props: {
    service: {
      type: Object,
      default() {
        return new Object();
      }
    },
    pageId: Number,
  },  
  data() {
    return {
      edit: false
    }
  },
  methods: {
    submit() {
      if (this.edit) {
        this.$http.put('api/service/' + this.service.id, this.service).then(response => {
          this.service = response.body;
          this.$emit('updated', this.service);
        });
      } else {
        this.$http.post('api/page/' + this.pageId + '/service', this.service).then(response => {
          this.service = response.body;
          this.$emit('updated', this.service);
        });
      }
    }
  }
}
</script>