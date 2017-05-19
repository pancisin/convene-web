<template>
  <panel type="table">
    <span slot="title">{{ $t('admin.page.administrators') }}</span>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>User</th>
          <th>Created</th>
          <th>Role</th>
          <th>Action</th>
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
          <td>
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
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
    'page': 'getAdministrators'
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