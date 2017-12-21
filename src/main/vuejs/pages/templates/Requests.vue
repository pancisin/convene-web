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
        <tr v-for="(request, index) in paginator.content" :key="index">
          <td v-text="request.email"></td>
          <td>{{ request.date | moment('L') }}</td>
        </tr>
        <tr v-if="paginator.content && paginator.content.length == 0">
          <td colspan="2" class="text-center">There's nothing to display.</td>
        </tr>
      </tbody>
    </table>
  
    <div class="text-center">
      <paginator :fetch="getRequests" :paginator="paginator" history />
    </div>
  </panel>
</template>

<script>
import { Paginator } from 'elements';
export default {
  name: 'page-requests',
  inject: ['provider'],
  data () {
    return {
      paginator: {},
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
  methods: {
    getRequests (page) {
      console.log('dsadasdas');
      this.loading = true;
      if (this.api != null) {
        this.api.getRequests(page, 10, paginator => {
          this.paginator = paginator;
          this.loading = false;
        });
      }
    }
  }
};
</script>