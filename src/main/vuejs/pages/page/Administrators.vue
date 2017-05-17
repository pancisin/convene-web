<template>
  <div class="card-box">
    <h4 class="header-title m-t-0">Administrators</h4>
  
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>User</th>
          <th>Created</th>
          <th>Role</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="admin in administrators">
          <td>
            {{ admin.user.firstName + ' ' + admin.user.lastName }}
          </td>
          <td>{{ admin.created | moment('DD.MM.YYYY') }}</td>
          <td>
            {{ $t(admin.role.code) }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'page-administrators',
  props: ['page'],
  data() {
    return {
      administrators: [],
    }
  },
  watch: {
    'page' : 'getAdministrators'
  },
  created() {
    this.getAdministrators();
  },
  methods: {
    getAdministrators() {
      if (this.page.id == null) return;
      var url = ['api/page', this.page.id, 'administrator'].join('/');
      this.$http.get(url).then(response => {
        this.administrators = response.body;
      })
    }
  }
}
</script>

<style>

</style>
