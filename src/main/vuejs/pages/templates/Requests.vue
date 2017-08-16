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
        <tr v-for="request in requests" :key="request.id">
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
export default {
  name: 'page-requests',
  inject: ['provider'],
  data () {
    return {
      requests: [],
      loading: false
    };
  },
  computed: {
    api() {
      return this.provider.api;
    }
  },
  created () {
    this.getRequests();
  },
  watch: {
    'api': 'getRequests'
  },
  methods: {
    getRequests () {
      this.loading = true;
      if (this.api != null) {
        this.api.getRequests(requests => {
          this.requests = requests;
          this.loading = false;
        });
      }
    }
  }
};
</script>