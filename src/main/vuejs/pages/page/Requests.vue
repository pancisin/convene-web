<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.requests') }}</span>
  
    <table class="table table-striped">
      <thead>
        <tr>
          <th>User</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="request in requests">
          <td v-text="request.email"></td>
          <td>{{ request.date | moment('L') }}</td>
        </tr>
        <tr v-if="requests.length == 0">
          <td colspan="2" class="text-center">There's nothing to display.</td>
        </tr>
      </tbody>
    </table>
  
  </panel>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'page-requests',
  inject: ['api'],
  data() {
    return {
      requests: [],
      loading: false
    }
  },
  created() {
    this.getRequests();
  },
  methods: {
    getRequests() {
      this.loading = true;
      this.api.getRequests(requests => {
        this.requests = requests;
        this.loading = false;
      })
    }
  }
}
</script>