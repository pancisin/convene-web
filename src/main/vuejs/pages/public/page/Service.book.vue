<template>
  <div v-if="service != null">
    <div class="form-group">
      <label class="control-label">Units: </label>
      <input class="form-control required" v-model="bookRequest.units" type="text">
    </div>

    <date-picker v-model="bookRequest.date" placeholder="Date" />
    <hr>
  
    <div class="text-center">
      {{ bookRequest.units }} x {{ service.price }}
      <br>
  
      <h4>= {{ bookRequest.units * service.price }}e</h4>
    </div>
    <hr>
  
    <custom-form :form="service.form" :submitFunc="submit" />
  </div>
</template>

<script>
import { DatePicker } from 'elements';
import { CustomForm } from 'elements/forms';

export default {
  name: 'service-book',
  props: {
    service: Object
  },
  components: {
    DatePicker,
    CustomForm
  },
  data () {
    return {
      bookRequest: {
        units: 0
      }
    };
  },
  methods: {
    submit (values) {
      console.log(values);

      // this.$http.post('/api/service/' + this.service.id + '/request', this.bookRequest).then(response => {
      //   this.$emit('submitted', response.body);
      // });
    }
  }
};
</script>