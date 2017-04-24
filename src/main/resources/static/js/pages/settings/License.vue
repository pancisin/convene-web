<template>
  <div class="card card-mini">
    <div class="card-header">
      <div class="card-title">Billing</div>
      <ul class="card-action">
        <li>
          <a @click="getLicenses()">
            <i class="fa fa-refresh"></i>
          </a>
        </li>
      </ul>
    </div>
    <div class="card-body no-padding table-responsive">
      <table class="table card-table">
        <thead>
          <tr>
            <th>Products</th>
            <th class="right">Expires</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="license in licenses">
            <td>Basic license</td>
            <td class="right">{{ license.expires | moment("dddd, MMMM Do YYYY") }}</td>
            <td>
              <span class="badge badge-info badge-icon"
                    v-if="license.status == 'NEW'">
                        <i class="fa fa-credit-card" aria-hidden="true"></i>
                        <span>Payment required</span>
              </span>
              <span class="badge badge-success badge-icon"
                    v-if="license.status == 'ACTIVE'">
                        <i class="fa fa-check" aria-hidden="true"></i>
                        <span>Paid & active</span>
              </span>
              <span class="badge badge-danger badge-icon"
                    v-if="license.status == 'EXPIRED'">
                        <i class="fa fa-times" aria-hidden="true"></i>
                        <span>Expired</span>
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
  
  <script>
  export default {
    name: 'licenses',
    data: function () {
      return {
        licenses: [],
      }
    },
    created: function () {
      this.getLicenses();
    },
    methods: {
      getLicenses: function () {
        var url = ['api/company', this.$store.getters.company_id, 'licenses'].join('/');

        this.$http.get(url).then(response => {
          this.licenses = response.body;
        });
      }
    }
  }
  </script>