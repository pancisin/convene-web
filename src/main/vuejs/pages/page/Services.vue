<template>
  <div class="card-box" v-if="page != null">
    <h4 class="text-dark  header-title m-t-0">Services</h4>
  
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>Name</th>
          <th>Price</th>
          <th>Duration</th>
          <th class="text-center">Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="service in services">
          <td v-text="service.name"></td>
          <td v-text="service.price"></td>
          <td v-text="service.duration"></td>
          <td class="text-center">
            <a @click="deleteService(service)" class="btn btn-rounded btn-xs btn-danger">
              <i class="fa fa-trash"></i>
            </a>
            <a @click="editService(service)" class="btn btn-rounded btn-xs btn-primary">
              <i class="fa fa-pencil"></i>
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  
    <div class="text-center">
      <a @click="editService" class="btn btn-rounded btn-success">Create service</a>
    </div>
  
    <modal :show.sync="displayEditModal" @close="displayEditModal = false">
      <h4 slot="header">Create service</h4>
      <div slot="body">
        <service-form :service="selectedService" :pageId="page.id" @updated="updatedService" />
      </div>
    </modal>
  </div>
</template>

<script>
import Modal from '../../elements/Modal.vue'
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
    Modal, ServiceForm
  },
  created() {
    this.getServices();
  },
  methods: {
    getServices() {
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
    updatedService: function(service) {
      this.services = this.services.filter(s => {
        return s.id != service.id;
      });
      this.services.push(service);
    }
  }
}
</script>