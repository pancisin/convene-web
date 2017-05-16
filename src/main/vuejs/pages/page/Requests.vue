<template>
  <div class="card-box">
    <h4 class="text-dark  header-title m-t-0">Overview</h4>
    
    <table class="table">
      <thead>
        <tr>
          <th>User</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="request in requests">
          <td>{{ request }}</td>
        </tr>
      </tbody>
    </table>

    <div class="row">
    
    </div>
  </div>
</template>

<script>
export default {
  name: 'page-requests',
  props: ['page'],
  data() {
    return {
      requests: []
    }
  },
  watch: {
    'page': 'getRequests'
  },
  created() {
    this.getRequests();
  },
  methods: {
    getRequests() {
      if (!this.page.id) retun();
      var url = ['api/page', this.page.id, 'requests'].join('/');
      this.$http.get(url).then(response => {
        this.requests = response.body;
      })
    }
  }
}
</script>