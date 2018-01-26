<template>
  <panel type="table" v-loading="loading">
    <span slot="title">Users</span>

    <vue-table :func="tableRender" :data="paginator.content" />
    <div class="text-center">
      <paginator :history="true" :paginator="paginator" :fetch="getUsers"></paginator>
    </div>
  </panel>
</template>

<script>
import RootApi from 'api/api';
import { Paginator, VueTable } from 'elements';

export default {
  name: 'users',
  data () {
    return {
      paginator: {},
      loading: false,
      subsctription: null
    };
  },
  components: {
    Paginator,
    VueTable
  },
  created () {
    this.connectWM('/stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/topic/active', response => {
        let activeUsers = JSON.parse(response.body);
        this.paginator.content = this.paginator.content.map(u => {
          return {
            ...u,
            online: activeUsers.includes(u.email)
          };
        });
      });
    });
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    getUsers (page) {
      this.loading = true;
      RootApi.getUsers(page, 10, paginator => {
        this.paginator = paginator;
        this.loading = false;
      });
    },
    tableRender (user) {
      return {
        id: user.id,
        A: {
          el: 'i',
          class: `fa fa-circle ${ user.online ? 'text-success' : ''}`
        },
        [this.$t('user.name')]: {
          el: 'a',
          onClick: () => {
            console.log(this);
            this.$router.push({ name: 'user', params: { user_id: user.id }});
          },
          content: user.displayName
        },
        [this.$t('user.email')]: user.email,
        V: {
          el: 'i',
          class: `fa fa-check ${ user.verified ? 'text-success' : 'text-warning' }`
        },
        [this.$t('settings.license.default')]: this.$t(user.license.subscription.code),
        user: this.$t(user.role.code),
        locale: this.$t(user.locale.code)
      };
    }
  }
};
</script>
