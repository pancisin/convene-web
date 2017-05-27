<template>
  <panel type="table">
    <span slot="title">{{ $t('settings.license.default') }}</span>
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
        <tr v-for="(sub, index) in subscriptions" :class="{ 'danger' : sub.state.name == 'UNPAID', 'info' : sub.state.name == 'NEW', 'success' : sub.state.name == 'ACTIVE' }">
          <td v-text="sub.id"></td>
          <td>
            {{ $t(sub.subscription.code) }}
          </td>
          <td>
            <b>{{ sub.subscription.price }}</b>
            <i class="fa fa-euro"></i>
          </td>
          <td>{{ sub.acquired | moment($store.getters.locale.dateFormat) }}</td>
          <td>{{ sub.expires | moment($store.getters.locale.dateFormat) }}</td>
          <td>{{ $t(sub.state.code) }}</td>
          <td>
            <a class="btn btn-rounded btn-primary btn-xs" :class="{ 'btn-danger' : sub.state.name == 'UNPAID' }" v-if="sub.state.name == 'NEW' || sub.state.name == 'UNPAID'">
              {{ $t('subscription.pay') }}
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
export default {
  name: 'licenses',
  data() {
    return {
      subscriptions: [],
    }
  },
  created() {
    this.getSubscriptions();
  },
  methods: {
    getSubscriptions() {
      this.$http.get('api/user/subscription').then(response => {
        this.subscriptions = response.body;
      })
    }
  }
}
</script>

<style>

</style>
