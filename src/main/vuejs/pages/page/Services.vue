<template>
  <panel type="table">
    <span slot="title">Services</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Name</th>
          <th>Detail</th>
          <th>Unit</th>
          <th>Price per unit</th>
          <th class="text-center">Action</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="service in services" :key="service.id">
          <td v-text="service.name"></td>
          <td v-text="service.detail"></td>
          <td v-text="service.unit"></td>
          <td v-text="service.pricePerUnit"></td>
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
      <a @click="editService" class="btn btn-rounded btn-default">Create service</a>
    </div>
  
    <modal :show.sync="displayEditModal" @close="displayEditModal = false" :full="false">
      <h4 slot="header">Create service</h4>
      <div slot="body">
        <service-form :service="selectedService" :pageId="page.id" @updated="updatedService" />
      </div>
    </modal>
  </panel>
</template>

<script>
import ServiceForm from './Service.form.vue'
export default {
  props: ['page'],
  data() {
    return {
      services: [],
      displayEditModal: false,
      selectedService: new Object()
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
      if (this.page.id == null) return;
      var url = ['api/page', this.page.id, 'service'].join('/');
      this.$http.get(url).then(response => {
        this.services = response.body;
      })
    },
    deleteService(service) {
      this.$http.delete('api/service/' + service.id).then(response => {
        this.services = this.services.filter(s => {
          return s.id != service.id;
        })
      })
    },
    editService(service) {
      this.selectedService = service ? service : new Object();
      this.displayEditModal = true;
    },
    updatedService: function (service) {
      this.services = this.services.filter(s => {
        return s.id != service.id;
      });
      this.services.push(service);
    }
  }
}
</script>