<template>
  <div v-if="service != null">
    <!-- <div class="form-group">
      <label class="control-label">Units: </label>
      <input class="form-control required" v-model="serviceRequest.units" type="text">
    </div>

    <date-picker v-model="serviceRequest.date" placeholder="Date" />
    <hr>

    <div class="text-center">
      {{ serviceRequest.units }} x {{ service.price }}
      <br>

      <h4>= {{ serviceRequest.units * service.price }}e</h4>
    </div>
    <hr> -->

    <custom-form :form="service.form" :submitFunc="submit" />
  </div>
</template>

<script>
import { DatePicker } from 'elements';
import { CustomForm } from 'elements/forms';
import ServiceApi from 'api/service.api';

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
      serviceRequest: {
        units: 0
      }
    };
  },
  methods: {
    submit (values) {
      ServiceApi.postServiceRequest(this.service.id, values, submission => {
        this.$emit('submit', submission);
      });
    }
  }
};
</script>
