<template>
  <div v-loading="loading">

    <panel type="table" v-for="(service, index) in services" :key="index">
      <span slot="title">
        {{service.name}}
        <button type="button" class="btn btn-link pull-right" @click="editService(service)">
          <i class="fa fa-cogs"></i>
        </button>
      </span>

      <service-panel :service="service" />
    </panel>


    <div class="text-center" v-if="editable">
      <button type="button" @click="createService" class="btn btn-default">
        {{ $t('admin.service.create') }}
      </button>
    </div>

    <!-- <panel type="table" v-loading="loading" class="m-t-20">
      <span slot="title">{{ $t('admin.page.services') }}</span>
      <vue-table :func="tableRender" :data="services" :contextmenu="contextmenu" />
    </panel> -->

    <modal :show.sync="displayServiceModal">
      <span slot="header">Edit service</span>
      
      <div slot="body">
        <service-form 
          v-if="displayServiceModal" 
          :service="selectedService" 
          @update="updateService" />
      </div>
    </modal>
  </div>
</template>

<script>
import ServiceApi from 'api/service.api';
import { VueTable } from 'elements';
import { ServiceForm } from 'elements/forms';
import { ServicePanel } from 'components';

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
    ServiceForm,
    ServicePanel
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
    },
    editService (service) {
      this.selectedService = service;
      this.displayServiceModal = true;
    }
  }
};
</script>
