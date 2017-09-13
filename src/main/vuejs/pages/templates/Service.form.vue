<template>
  <div>
    <div class="form-group">
      <label class="control-label">Name</label>
      <input class="form-control required" v-model="service.name" type="text">
    </div>
  
    <div class="form-group">
      <label class="control-label">Detail</label>
      <textarea class="form-control required" v-model="service.detail" />
    </div>
  
    <label class="control-label">Price</label>
    <div class="input-group m-b-10">
      <span class="input-group-addon">
        <i class="fa fa-euro"></i>
      </span>
      <input type="text" class="form-control text-center" v-model="service.pricePerUnit">
      <span class="input-group-addon">/</span>
      <select class="form-control" @change="changeUnit">
        <option v-for="unit in units" :value="unit.name" :selected="service.unit != null && service.unit.name == unit.name">
          {{ $t(unit.code) }}
        </option>
      </select>
    </div>
  
    <div class="text-center">
      <button @click="submit" class="btn btn-rounded btn-success">Submit</button>
    </div>
  </div>
</template>

<script>
import { vSelect } from '../../elements';
import ServiceApi from 'api/service.api';

export default {
  name: 'service-form',
  inject: ['provider'],
  props: {
    service: {
      type: Object,
      default () {
        return {
          name: null,
          detail: null,
          unit: null,
          pricePerUnit: null
        };
      }
    }
  },
  components: {
    vSelect
  },
  data () {
    return {
      edit: false,
      units: []
    };
  },
  computed: {
    api () {
      return this.provider.api;
    },
  },
  created () {
    this.$http.get('api/enum/unit').then(response => {
      this.units = response.body;
    });
  },
  methods: {
    submit () {
      if (this.edit) {
        ServiceApi.putService(this.service, service => {
          this.$emit('updated', service);
        });
      } else {
        this.api.postService(this.service, service => {
          this.$emit('updated', service);
        });
      }
    },
    changeUnit (e) {
      this.service.unit = event.target.value;
    }
  }
};
</script>