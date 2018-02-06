<template>
  <form @submit.prevent="submit" v-loading="loading">
    <div class="form-group" :class="{ 'has-error' : errors.has('service.name') }" >
      <label class="control-label">Name</label>
      
      <input 
        class="form-control" 
        v-model.trim="serviceCopy.name" 
        name="service.name"
        type="text"
        v-validate="'required'"
        data-vv-as="name">
      
      <span 
        class="text-danger" 
        v-if="errors.has('service.name')">
          {{ errors.first('service.name') }}
      </span>
    </div>

    <div class="form-group">
      <label class="control-label">Detail</label>
      
      <textarea 
        class="form-control" 
        v-model="serviceCopy.detail" />
    </div>

    <div class="form-group" :class="{ 'has-error' : errors.has('service.price') || errors.has('service.unit') }">
      <label class="control-label">Price</label>
      <div class="input-group m-b-10">
        <span class="input-group-addon">
          <i class="fa fa-euro"></i>
        </span>
        
        <input 
          type="text" 
          class="form-control text-center" 
          v-model="serviceCopy.price"
          name="service.price"
          v-validate="'required'"
          data-vv-as="price">

        <span class="input-group-addon">/</span>
        
        <select 
          class="form-control" 
          v-model="serviceCopy.unit"
          v-validate="'required'"
          name="service.unit"
          data-vv-as="unit">

          <option 
            v-for="(unit, index) in units" :value="unit.prop" 
            :selected="serviceCopy.unit != null && serviceCopy.unit.prop == unit.prop" 
            :key="index">

            {{ $t(unit.code) }}
          </option>
        </select>
      </div>

      <span 
        class="text-danger" 
        v-if="errors.has('service.price') || errors.has('service.unit')">
          {{ errors.first('service.price') || errors.first('service.unit') }}
      </span>
    </div>

    <h4 class="m-t-20">Additional fields</h4>
    <hr />

    <form-editor v-model="serviceCopy.form" />

    <div class="text-right">
      <button type="submit" class="btn btn-primary">Save & close</button>
    </div>
  </form>
</template>

<script>
import PublicApi from 'api/public.api';
import FormEditor from '../Form.editor';
import ServiceApi from 'api/service.api';

export default {
  props: {
    service: Object
  },
  inject: ['provider'],
  data () {
    return {
      units: [],
      serviceCopy: {},
      loading: false
    };
  },
  components: {
    FormEditor
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  created () {
    this.getUnits();
    this.initialize(this.service);
  },
  watch: {
    service (newVal) {
      this.initialize(newVal);
    }
  },
  methods: {
    initialize (service) {
      this.serviceCopy = {
        ...service,
        unit: service.unit != null ? service.unit.prop : null
      };
    },
    getUnits () {
      PublicApi.getUnits(units => {
        this.units = units;
      });
    },
    submit () {
      this.$validator.validateAll().then(valid => {
        if (!valid) return;

        this.loading = true;
        if (this.serviceCopy.id == null) {
          this.api.postService(this.serviceCopy, service => {
            this.initialize(service);
            this.$emit('update', service);
            this.$success('notification.service.created', service.name);
            this.loading = false;
          });
        } else {
          ServiceApi.putService(this.serviceCopy.id, this.serviceCopy, service => {
            this.initialize(service);
            this.$emit('update', service);
            this.$success('notification.service.updated', service.name);
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>
