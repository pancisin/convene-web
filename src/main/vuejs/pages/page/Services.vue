<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.services') }}</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>{{ $t('admin.service.name') }}</th>
          <th>{{ $t('admin.service.detail') }}</th>
          <th>{{ $t('admin.service.unit') }}</th>
          <th class="text-center">{{ $t('admin.service.price_per_unit') }}</th>
          <th class="text-center"></th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="service in services" :key="service.id">
          <td v-text="service.name"></td>
          <td v-text="service.detail"></td>
          <td>
            <span v-if="service.unit">{{ $t(service.unit.code) }}</span>
          </td>
          <td class="text-center">{{ service.pricePerUnit }}
            <i class="fa fa-euro"></i>
          </td>
          <td class="text-center">
            <a @click="deleteService(service)" class="btn btn-rounded btn-xs btn-danger">
              <i class="fa fa-trash"></i>
            </a>
            <a @click="editService(service)" class="btn btn-rounded btn-xs btn-primary">
              <i class="fa fa-pencil"></i>
            </a>
          </td>
        </tr>
        <tr v-if="services.length == 0" :key="0">
          <td colspan="5" class="text-center">There's nothing to display.</td>
        </tr>
      </tbody>
    </table>
  
    <div class="text-center">
      <a @click="editService(null)" class="btn btn-rounded btn-default">{{ $t('admin.service.create') }}</a>
    </div>
  
    <modal :show.sync="displayEditModal" @close="displayEditModal = false" :full="false">
      <h4 slot="header">{{ $t('admin.service.create') }}</h4>
      <div slot="body">
        <service-form :service="selectedService" @updated="updatedService" />
      </div>
    </modal>
  </panel>
</template>

<script>
import ServiceForm from './Service.form.vue'
import ServiceApi from '../../services/api/service.api.js'

export default {
  inject: ['api'],
  data() {
    return {
      services: [],
      displayEditModal: false,
      selectedService: new Object(),
      loading: false,
    }
  },
  components: {
    ServiceForm
  },
  created() {
    this.getServices();
  },
  watch: {
    'page': 'getServices'
  },
  methods: {
    getServices() {
      this.loading = true;
      this.api.getServices(true, services => {
        this.services = services;
        this.loading = false;
      })
    },
    deleteService(service) {
      ServiceApi.deleteService(service.id, result => {
        this.services = this.services.filter(s => {
          return s.id != service.id;
        })
      })
    },
    editService(service) {
      this.selectedService = service ? service : new Object();
      this.displayEditModal = true;
    },
    updatedService(service) {
      this.services = this.services.filter(s => {
        return s.id != service.id;
      });
      this.services.push(service);
    }
  }
}
</script>