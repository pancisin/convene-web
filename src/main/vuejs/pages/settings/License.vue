<template>
  <div>
    <table class="table">
      <thead>
        <tr>
          <th>#</th>
          <th>{{ $t('settings.license.default') }}</th>
          <th>{{ $t('settings.license.price') }}</th>
          <th>{{ $t('settings.license.acquired') }}</th>
          <th>{{ $t('settings.license.expires') }}</th>
          <th>{{ $t('settings.license.state') }}</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr 
          v-for="(sub, index) in subscriptions" 
          :class="{ 'danger' : sub.state.prop == 'UNPAID', 'info' : sub.state.prop == 'NEW', 'success' : sub.state.prop == 'ACTIVE' }"
          :key="index">
          <td>
            <router-link :to="{ name: 'invoice', params: { invoice_id : sub.id }}" v-text="sub.id">
            </router-link>
          </td>
          <td>
            {{ $t(sub.subscription.code) }}
          </td>
          <td>
            <b>{{ sub.subscription.price }}</b>
            <i class="fa fa-euro"></i>
          </td>
          <td>{{ sub.acquired | luxon('ff') }}</td>
          <td>{{ sub.expires | luxon('ff') }}</td>
          <td>{{ $t(sub.state.code) }}</td>
          <td>
            <a @click="doPayment(sub)" 
              class="btn btn-primary btn-xs" :class="{ 'btn-danger' : sub.state.prop == 'UNPAID' }" v-if="sub.state.prop == 'NEW' || sub.state.prop == 'UNPAID'">
              {{ $t('subscription.pay') }}
            </a>
          </td>
        </tr>
      </tbody>
    </table>

    <modal :show.sync="displayPayment">
      <span slot="header">Payment</span>
      <div slot="body">
        <payment :license="selectedLicense"></payment>
      </div>
    </modal>
  </div>
</template>

<script>
import { Payment } from 'elements';
export default {
  name: 'licenses',
  data () {
    return {
      subscriptions: [],
      displayPayment: false,
      selectedLicense: null
    };
  },
  components: {
    Payment
  },
  created () {
    this.getSubscriptions();
  },
  methods: {
    getSubscriptions () {
      this.$http.get('/api/user/subscription').then(response => {
        this.subscriptions = response.body;
      });
    },
    doPayment (license) {
      this.displayPayment = true;
      this.selectedLicense = license;
    }
  }
};
</script>