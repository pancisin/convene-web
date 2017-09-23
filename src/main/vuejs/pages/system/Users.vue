<template>
  <panel type="table" v-loading="loading">
    <span slot="title">Users</span>
    <table class="table">
      <thead>
        <tr>
          <th>
            ID
          </th>
          <th>
          </th>
          <th>
            Name
          </th>
          <th>
            Email
          </th>
          <th>
            License
          </th>
          <th>
            Role
          </th>
          <th>
            Locale
          </th>
          <th>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in paginator.content" :key="user.id">
          <td>
            {{ user.id }}
          </td>
          <td>
            <i class="fa fa-check text-success" v-if="user.verified"></i>
            <i class="fa fa-book text-warning" v-else></i>
          </td>
          <td>
            {{ user.displayName }}
          </td>
          <td>
            <a :href="'mailto:' + user.email">
              {{ user.email }}
            </a>
          </td>
          <td>
            {{ $t(user.license.subscription.code) }}
          </td>
          <td>
            {{ $t(user.role.code) }}
          </td>
          <td>
            {{ $t(user.locale.code) }}
          </td>
          <td>
            <a class="btn btn-xs btn-default">
              <i class="fa fa-comment-o"></i>
            </a>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="text-center">
      <paginator :history="true" :paginator="paginator" @navigate="paginatorNavigate"></paginator>
    </div>
  </panel>
</template>

<script>
import RootApi from 'api/api';
import { Paginator } from 'elements';

export default {
  name: 'users',
  data () {
    return {
      paginator: [],
      loading: false
    }
  },
  components: {
    Paginator
  },
  created () {
    this.getUsers(0);
  },
  methods: {
    getUsers (page) {
      this.loading = true;
      RootApi.getUsers(page, 10, paginator => {
        this.paginator = paginator;
        this.loading = false;
      })
    },
    paginatorNavigate (e) {
      if (e.direction != null) {
        this.getUsers(this.paginator.number + e.direction);
      } else if (e.page != null) {
        this.getUsers(e.page);
      }
    },
  }
}
</script>
