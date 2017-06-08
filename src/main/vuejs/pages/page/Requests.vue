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
          <td>{{ request.date | moment(locale.dateFormat) }}</td>
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
  props: ['page'],
  data() {
    return {
      requests: [],
      loading: false
    }
  },
  watch: {
    'page': 'getRequests'
  },
  computed: {
    ...mapGetters({
      locale: 'getLocale'
    })
  },
  created() {
    this.getRequests();
  },
  methods: {
    getRequests() {
      if (!this.page.id) return;

      this.loading = true;
      var url = ['api/page', this.page.id, 'requests'].join('/');
      this.$http.get(url).then(response => {
        this.requests = response.body;
        this.loading = false;
      })
    }
  }
}
</script>