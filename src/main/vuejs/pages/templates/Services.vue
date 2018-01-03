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
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="service in services" :key="service.id" @contextmenu.prevent="$refs.menu.open($event, service)">
          <td>
            <router-link :to="{ name: 'service', params: { service_id: service.id } }">
              {{ service.name }}
            </router-link>
          </td>
          <td>
            {{ service.detail }}
          </td>
          <td>
            <span v-if="service.unit">{{ $t(service.unit.code) }}</span>
          </td>
          <td class="text-center">
            {{ service.pricePerUnit }}
            <i class="fa fa-euro"></i>
          </td>
        </tr>
        <tr v-if="services.length == 0" :key="0">
          <td colspan="5" class="text-center">There's nothing to display.</td>
        </tr>
      </tbody>
    </table>

    <context-menu ref="menu">
      <template slot-scope="props">
        <ul>
          <li>
            <a @click="editService(props.data)">
              Edit
            </a>
          </li>
          <li v-if="editable">
            <a @click="deleteService(props.data)">
              Delete
            </a>
          </li>
          <li class="separator"></li>
          <li>
            <router-link :to="{ name: 'page.create-service' }">
              {{ $t('admin.service.create') }}
            </router-link>
          </li>
        </ul>
      </template>
    </context-menu>

    <div class="text-center" v-if="editable">
      <router-link :to="{ name: 'page.create-service' }" class="btn btn-rounded btn-primary">
        {{ $t('admin.service.create') }}
      </router-link>
    </div>
  </panel>
</template>

<script>
import ServiceApi from 'api/service.api';

export default {
  inject: ['provider'],
  data () {
    return {
      services: [],
      displayEditModal: false,
      selectedService: {},
      loading: false
    };
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
    editService (service) {
      this.selectedService = service ? service : {};
      this.displayEditModal = true;
    },
    updatedService (service) {
      this.services = this.services.filter(s => {
        return s.id !== service.id;
      });
      this.services.push(service);
    }
  }
};
</script>
