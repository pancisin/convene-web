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
        </tr>
      </thead>
      <tbody is="transition-group" name="fade">
        <tr v-for="admin in administrators" :key="admin.id" @contextmenu.prevent="$refs.menu.open($event, admin)">
          <td>
            {{ admin.user.firstName + ' ' + admin.user.lastName }}
          </td>
          <td>{{ admin.created | moment('L') }}</td>
          <td class="text-center">
            <select v-model="admin.role" class="form-control" @change="putAdministrator(admin)" v-if="hasPermission(admin) && editable">
              <option v-for="role in roles" :value="role">{{ $t(role.code) }}</option>
            </select>
            <span v-else>
              {{ $t(admin.role.code) }}
            </span>
          </td>
          <td class="text-center">
            <a @click="toggleActive(admin)" class="btn btn-primary btn-rounded" v-if="hasPermission(admin) && editable">
              {{ $t(admin.active ? 'administrator.active' : 'administrator.inactive') }}
            </a>
            <span v-else>
              {{ $t(admin.active ? 'administrator.active' : 'administrator.inactive') }}
            </span>
          </td>
        </tr>
        <tr :key="0" v-if="editable">
          <td colspan="2">
            <user-search v-model="selected_user.email" />
          </td>
          <td>
            <a @click="grantAccess" class="btn btn-success btn-rounded">Grant access</a>
          </td>
          <td colspan="2">
          </td>
        </tr>
      </tbody>
    </table>

    <context-menu ref="menu">
      <template slot-scope="props">
        <ul>
          <li :class="{ 'disabled' : !editable || !hasPermission(props.data) }">
            <a @click="toggleActive(props.data)">
              {{ $t(props.data.active ? 'administrator.active' : 'administrator.inactive') }}
            </a>
          </li>
          <li class="separator"></li>
          <li :class="{ 'disabled' : !editable || !hasPermission(props.data) }">
            <a @click="deleteAdministrator(props.data)">
              Delete
            </a>
          </li>
        </ul>
      </template>
    </context-menu>
  </panel>
</template>

<script>
import { mapGetters } from 'vuex';
import { UserSearch } from 'elements';

export default {
  name: 'conference-administrators',
  props: {
    editable: Boolean
  },
  inject: ['provider'],
  data () {
    return {
      administrators: [],
      roles: [],
      selected_user: {},
      loading: false,
      current_pa: null
    };
  },
  components: {
    UserSearch
  },
  computed: {
    ...mapGetters(['user']),
    api () {
      return this.provider.api;
    }
  },
  watch: {
    'api': 'getAdministrators'
  },
  created () {
    this.getAdministrators();
  },
  methods: {
    getAdministrators () {
      if (this.api != null) {
        this.loading = true;
        this.api.getAdministrators(administrators => {
          this.administrators = administrators;

          this.administrators.sort((a, b) => {
            return a.role.level < b.role.level;
          });

          this.current_pa = this.administrators.filter(a => {
            return a.user.id === this.user.id;
          })[0];

          this.getRoles();
        });
      }
    },
    getRoles () {
      this.$http.get('/api/roles').then(response => {
        this.roles = response.body.filter(r => {
          return r.level < this.current_pa.role.level;
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

      this.api.putAdministrator(admin.id, data, administrator => {
        admin = administrator;
      });
    },
    hasPermission (admin) {
      return admin.role.level < this.current_pa.role.level;
    },
    deleteAdministrator (admin) {
      this.api.deleteAdministrator(admin.id, result => {
        this.administrators = this.administrators.filter(a => {
          return a.id !== admin.id;
        });
      });
    },
    grantAccess () {
      this.api.postAdministrator(this.selected_user, administrator => {
        this.administrators.push(administrator);
      });
    }
  }
};
</script>