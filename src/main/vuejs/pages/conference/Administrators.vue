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
            <user-search v-model="user.email" :options="users" @search="searchUsers" />
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
import { mapGetters } from 'vuex';
import UserSearch from '../../elements/UserSuggestInput.vue';
import UserApi from 'api/user.api';

export default {
  name: 'conference-administrators',
  inject: ['provider'],
  data () {
    return {
      administrators: [],
      roles: [],
      users: [],
      user: {},
      loading: false
    };
  },
  components: {
    UserSearch
  },
  computed: {
    ...mapGetters({
      current: 'user'
    }),
    api () {
      return this.provider.api;
    }
  },
  created () {
    this.getAdministrators();
  },
  methods: {
    getAdministrators () {
      this.loading = true;
      this.api.getAdministrators(administrators => {
        this.administrators = administrators;

        this.administrators.sort((a, b) => {
          return a.role.level < b.role.level;
        });

        this.getRoles();
      });
    },
    getRoles () {
      this.$http.get('api/roles').then(response => {
        this.roles = response.body.filter(r => {
          return r.level < this.current.role.level;
        });

        this.loading = false;
      });
    },
    toggleActive (admin) {
      admin.active = !admin.active;
      this.putAdministrator(admin);
    },
    putAdministrator (admin) {
      var data = {
        active: admin.active,
        role: admin.role.name
      };

      this.$http.put('api/conference-administrator/' + admin.id, data).then(response => {
        admin = response.body;
      });
    },
    hasPermission (admin) {
      return admin.role.level < this.current.role.level;
    },
    deleteAdministrator (admin) {
      this.$http.delete('api/conference-administrator/' + admin.id).then(response => {
        this.administrators = this.administrators.filter(a => {
          return a.id !== admin.id;
        });
      });
    },
    searchUsers (search, loading) {
      loading(true);
      UserApi.searchUsers(search, users => {
        this.users = users;
        loading(false);
      });
    },
    grantAccess () {
      this.api.postAdministrator(this.user, administrator => {
        this.administrators.push(administrator);
      });
    }
  }
};
</script>