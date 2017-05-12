<template>
  <div>
    <h2 v-text="service.name"></h2>
  
    <div class="form-group">
      <label class="control-label">Units: </label>
      <input class="form-control required" v-model="bookRequest.units" type="text">
    </div>
    <hr>
  
    <div class="text-center">
      {{ bookRequest.units }} x {{ service.pricePerUnit }}
      <br>
  
      <h4>= {{ bookRequest.units * service.pricePerUnit }}e</h4>
    </div>
    <hr>

    <div class="text-center">
      <a @click="submit" class="btn btn-rounded btn-success">Submit</a>
    </div>
  
  </div>
</template>

<script>
export default {
  name: "service-book",
  props: {
    service: Object,
  },
  data() {
    return {
      bookRequest: {
        units: 0,
      },
    }
  },
  methods: {
    submit() {
      this.$http.post('api/service/' + this.service.id + '/request', this.bookRequest).then(response => {
        this.$emit('submitted', response.body);
      })
    }
  }
}
</script>