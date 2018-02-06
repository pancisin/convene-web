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
        <tr v-for="(request, index) in requests" :key="index">
          <td>{{ request.user }}</td>
          <td>{{ request.date | luxon('D') }}</td>
        </tr>
        <tr v-if="paginator && paginator.length == 0">
          <td colspan="2" class="text-center">There's nothing to display.</td>
        </tr>
      </tbody>
    </table>
  
    <!-- <div class="text-center">
      <paginator :fetch="getRequests" :paginator="paginator" history />
    </div> -->
  </panel>
</template>

<script>
import { Paginator } from 'elements';
export default {
  name: 'page-requests',
  inject: ['provider'],
  data () {
    return {
      requests: [],
      loading: false
    };
  },
  components: {
    Paginator
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  created () {
    this.getRequests(0);
  },
  methods: {
    getRequests (page) {
      console.log('dsadasdas');
      this.loading = true;
      if (this.api != null) {
        this.api.getRequests(page, 10, requests => {
          this.requests = requests;
          this.loading = false;
        });
      }
    }
  }
};
</script>