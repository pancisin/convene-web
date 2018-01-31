<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.services') }}</span>
    <vue-table :func="tableRender" :data="services" :contextmenu="contextmenu" />

    <modal :show.sync="displayServiceModal">
      <span slot="header">Edit service</span>
      
      <div slot="body">
        <service-form 
          v-if="displayServiceModal" 
          :service="selectedService" 
          @update="updateService" />
      </div>
    </modal>

    <div class="text-center" v-if="editable">
      <button type="button" @click="createService" class="btn btn-default">
        {{ $t('admin.service.create') }}
      </button>
    </div>
  </panel>
</template>

<script>
import ServiceApi from 'api/service.api';
import { VueTable } from 'elements';
import { ServiceForm } from 'elements/forms';

export default {
  inject: ['provider'],
  data () {
    return {
      services: [],
      selectedService: {},
      loading: false,
      displayServiceModal: false
    };
  },
  components: {
    VueTable,
    ServiceForm
  },
  props: {
    editable: Boolean
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  created () {
    this.getServices();
  },
  watch: {
    'api': 'getServices'
  },
  methods: {
    tableRender (service) {
      return {
        [this.$t('admin.service.name')]: {
          el: 'a',
          content: service.name,
          onClick: () => {
            this.selectedService = service;
            this.displayServiceModal = true;
          }
        },
        [this.$t('admin.service.detail')]: service.detail,
        [this.$t('admin.service.unit')]: service.unit != null ? this.$t(service.unit.code) : '',
        [this.$t('admin.service.price_per_unit')]: service.price
      };
    },
    contextmenu (item) {
      return [
        item('Edit', this.editService),
        item('Delete', this.deleteService),
        item(this.$t('admin.service.create'), this.createService)
      ];
    },
    getServices () {
      this.loading = true;
      if (this.api != null) {
        this.api.getServices(services => {
          this.services = services;
          this.loading = false;
        });
      }
    },
    deleteService (service) {
      this.$prompt('notification.delete_prompt', service.name, () => {
        ServiceApi.deleteService(service.id, result => {
          this.services = this.services.filter(s => {
            return s.id !== service.id;
          });
        });
      });
    },
    updateService (service) {
      this.services = this.services.filter(s => {
        return s.id !== service.id;
      });
      this.services.push(service);
      this.displayServiceModal = false;
    },
    createService () {
      this.selectedService = {};
      this.displayServiceModal = true;
    }
  }
};
</script>
