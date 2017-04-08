  <template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-mini">
        <div class="card-header">
          <div class="card-title">Customers</div>
          <ul class="card-action">
            <li>
              <a @click="getCustomers">
                <i class="fa fa-refresh"></i>
              </a>
            </li>
            <li>
              <router-link to="/customers/create">
                <i class="fa fa-plus"></i>
              </router-link>
            </li>
          </ul>
        </div>
        <div class="card-body no-padding table-responsive">
          <table class="table card-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Name</th>
                <th>Contact person</th>
                <th>Contact email</th>
                <th>Description</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cust in customers">
                <th scope="row"
                    v-text="cust.id"></th>
                <td>
                  <router-link :to="'/customers/' + cust.id" class="btn-link">
                    {{ cust.name }}
                  </router-link>
                </td>
                <td v-text="cust.person"></td>
                <td v-text="cust.email"></td>
                <td v-text="cust.description"></td>
                <td>
                  <a @click="deleteCustomer(cust.id)"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'customers',
  data: function () {
    return {
      customers: [],
      display: {
        modal: false
      }
    }
  },
  created: function () {
    this.getCustomers();
  },
  methods: {
    getCustomers: function () {
      var url = ['api/company', this.$store.getters.company_id, 'customers'].join('/');
      this.$http.get(url).then(response => {
        this.customers = response.body;
      });
    },
    deleteCustomer: function (customer_id) {
      var url = ['api/customer', customer_id].join('/');
      this.$http.delete(url).then(response => {
        for (var i = 0; i < this.customers.length; i++) {
          if (this.customers[i].id == customer_id)
            this.customers.splice(i, 1);
        }
      });
    },
  }
}
</script>