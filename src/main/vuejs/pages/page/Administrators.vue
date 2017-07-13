<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.administrators') }}</span>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>User</th>
          <th>Granted</th>
          <th class="text-center">Role</th>
          <th class="text-center">State</th>
          <th class="text-center">Action</th>
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="admin in administrators" :key="admin.id">
          <td>
            {{ admin.user.firstName + ' ' + admin.user.lastName }}
          </td>
          <td>{{ admin.created | moment('L') }}</td>
          <td class="text-center">
            <select v-model="admin.role" class="form-control" @change="putAdministrator(admin)" v-if="hasPermission(admin)">
              <option v-for="role in roles" :value="role">{{ $t(role.code) }}</option>
            </select>
            <span v-else>
              {{ $t(admin.role.code) }}
            </span>
          </td>
          <td class="text-center">
            <a @click="toggleActive(admin)" class="btn btn-primary btn-rounded" v-if="hasPermission(admin)">{{ $t(admin.active ? 'administrator.active' : 'administrator.inactive') }}</a>
          </td>
          <td class="text-center">
            <a @click="deleteAdministrator(admin)" class="btn btn-rounded btn-xs btn-danger" v-if="hasPermission(admin)">
              <i class="fa fa-trash"></i>
            </a>
          </td>
        </tr>
        <tr :key="0">
          <td colspan="2">
            <!--<v-select v-model="user" :debounce="2000" :on-search="searchUsers" :options="users" placeholder="Search users" label="email"></v-select>-->
          </td>
          <td>
            <a @click="grantAccess" class="btn btn-success btn-rounded">Grant access</a>
          </td>
          <td colspan="2">
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'page-administrators',
  props: ['page'],
  data() {
    return {
      administrators: [],
      roles: [],
      users: [],
      user: null,
      loading: false,
    }
  },
  watch: {
    'page': 'getAdministrators',
  },
  computed: {
    ...mapGetters({
      current: 'getUser',
    })
  },
  created() {
    this.getAdministrators();
  },
  methods: {
    getAdministrators() {
      if (this.page.id == null) return;
      this.loading = true;
      var url = ['api/page', this.page.id, 'administrator'].join('/');
      this.$http.get(url).then(response => {
        this.administrators = response.body;

        this.administrators.sort((a, b) => {
          return a.role.level < b.role.level;
        });

        this.getRoles();
      })
    },
    getRoles() {
      this.$http.get('api/roles').then(response => {
        this.roles = response.body.filter(r => {
          return r.level < this.current.role.level;
        })

        this.loading = false;
      })
    },
    toggleActive(admin) {
      admin.active = !admin.active;
      this.putAdministrator(admin);
    },
    putAdministrator(admin) {
      var data = {
        active: admin.active,
        role: admin.role.name
      }

      this.$http.put('api/page-administrator/' + admin.id, data).then(response => {
        admin = response.body;
      })
    },
    hasPermission(admin) {
      return admin.role.level < this.current.role.level;
    },
    deleteAdministrator(admin) {
      this.$http.delete('api/page-administrator/' + admin.id).then(response => {
        this.administrators = this.administrators.filter(a => {
          return a.id != admin.id;
        })
      })
    },
    searchUsers(search, loading) {
      loading(true);
      this.$http.get('api/user/search', {
        params: {
          q: search
        }
      }).then(response => {
        this.users = response.body
        loading(false)
      })
    },
    grantAccess() {
      var url = ['api/page', this.page.id, 'administrator'].join('/');
      this.$http.post(url, { id: this.user.id }).then(response => {
        this.administrators.push(response.body);
      })
    }
  }
}
</script>