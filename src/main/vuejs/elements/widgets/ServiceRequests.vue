<template>
  <panel type="table" v-loading="loading">
    <span slot="title">
      Service requests
    </span>

    {{ paginator }}

    <div class="text-center" slot="footer">
      <paginator :fetch="getRequests" :paginator="paginator" />
    </div>
  </panel>
</template>

<script>
import Paginator from '../Paginator';
export default {
  name: 'service-widgets',
  inject: ['provider'],
  data () {
    return {
      paginator: {},
      loading: false
    };
  },
  component: {
    Paginator
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  methods: {
    getRequests (page) {
      this.loading = true;
      this.api.getRequests(page, 10, paginator => {
        this.loading = false;
        this.paginator = paginator;
      });
    }
  }
};
</script>
