<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">Create or update customer</div>
        <div class="card-body">
  
          <form class="form form-horizontal"
                @submit.prevent="submitCustomer">
            <div class="section">
              <div class="section-body">
                <input type="text"
                       v-model="customer.name"
                       class="form-control"
                       placeholder="Name">
                <input type="text"
                       v-model="customer.person"
                       class="form-control"
                       placeholder="Contact person">
                <input type="text"
                       v-model="customer.email"
                       class="form-control"
                       placeholder="Contact email">
                <input type="text"
                       v-model="customer.address"
                       class="form-control"
                       placeholder="Address">
                <input type="text"
                       v-model="customer.description"
                       class="form-control"
                       placeholder="Description">
              </div>
            </div>
  
            <div class="form-footer">
              <div class="form-group">
                <div class="col-md-9 col-md-offset-3">
                  <button type="submit"
                          class="btn btn-primary">
                    <span v-if="edit_mode">Update and close</span>
                    <span v-else>Create</span>
                  </button>
                  <button type="button"
                          class="btn btn-default"
                          @click="$router.go(-1)">Cancel</button>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import datepicker from '../elements/DatePicker.vue';
import recurrenceInput from '../elements/RecurrenceInput.vue';

export default {
  name: 'create-customer',
  data: function () {
    return {
      customer: {
        name: null,
        person: null,
        email: null,
        address: null,
        description: null,
      }
    }
  },
  computed: {
    edit_mode: {
      get: function () {
        return this.customer.id != null;
      }
    }
  },
  created: function () {
    var customer_id = this.$route.params.id;
    if (customer_id != null) {
      this.$http.get('api/customer/' + customer_id).then(response => {
        this.customer = response.body;
      })
    }
  },
  methods: {
    submitCustomer: function () {
      if (this.edit_mode) {
        var url = ['api/customer', this.customer.id].join('/');
        this.$http.put(url, JSON.stringify(this.customer)).then(response => {
          this.customer = response.body;
        });
        this.$router.go(-1);
      } else {
        var url = ['api/company', this.$store.getters.company_id, 'customers'].join('/');
        this.$http.post(url, JSON.stringify(this.customer)).then(response => {
          this.customer = response.body;
        });
      }
    }
  }
}
</script>