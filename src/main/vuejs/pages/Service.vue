<template>
  <div v-if="service != null">
    <div class="page-title-box">
      <h4 class="page-title">
        {{ service.name }}
      </h4>
    </div>

    <panel type="primary">
      <span slot="title">Service</span>
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
        <select class="form-control" v-model="service.unit">
          <option v-for="(unit, index) in units" :value="unit.prop" :selected="service.unit != null && service.unit.prop == unit.prop" :key="index">
            {{ $t(unit.code) }}
          </option>
        </select>
      </div>

      <div class="text-center">
        <button @click="submit" class="btn btn-rounded btn-primary">Save</button>
      </div>
    </panel>
  </div>
</template>

<script>
import ServiceApi from 'api/service.api';
import RootApi from 'api/api';

export default {
  name: 'service',
  inject: ['provider'],
  data () {
    return {
      units: [],
      service: {}
    };
  },
  created () {
    this.getService();
    this.getUnits();
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  methods: {
    getService () {
      const service_id = this.$route.params.service_id;

      if (service_id != null) {
        ServiceApi.getService(service_id, service => {
          this.service = {
            ...service,
            unit: service.unit != null ? service.unit.prop : null
          };
        });
      }
    },
    submit () {
      if (this.service.id == null) {
        this.api.postService(this.service, service => {
          this.service = {
            ...service,
            unit: service.unit != null ? service.unit.prop : null
          };
        });
      } else {
        ServiceApi.putService(this.service.id, this.service, service => {
          this.service = {
            ...service,
            unit: service.unit != null ? service.unit.prop : null
          };
        });
      }
    },
    getUnits () {
      RootApi.getUnits(units => {
        this.units = units;
      });
    }
  }
};
</script>
