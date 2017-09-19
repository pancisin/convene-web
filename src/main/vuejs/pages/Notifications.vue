<template>
  <panel type="table" v-loading="loading">
    <span slot="title">Notifications</span>
    <table class="table table-striped">
      <thead>
        <tr>
          <th class="text-center">
            #id
          </th>
          <th>
            Created
          </th>
          <th>
            Title
          </th>
          <th>
            Message
          </th>
          <th class="text-center">
            Seen
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="not in paginator.content" @contextmenu.prevent="$refs.menu.open($event, not)" :key="not.id">
          <td v-text="not.id" class="text-center"></td>
          <td>{{ not.created | moment('from') }}</td>
          <td v-text="not.title"></td>
          <td v-text="not.message"></td>
          <td class="text-center">
            <a @click="toggleSeen(not)">
              <transition name="fade-down" mode="out-in">
                <i class="material-icons" v-if="not.seen" key="1">done</i>
                <i class="material-icons" v-else key="0">radio_button_unchecked</i>
              </transition>
            </a>
          </td>
        </tr>
      </tbody>
    </table>
  
    <context-menu ref="menu">
      <template scope="props">
        <ul>
          <li>
            <a @click="toggleSeen(props.data)">
              Toggle seen
            </a>
          </li>
        </ul>
      </template>
    </context-menu>
  
    <div class="text-center">
      <paginator :paginator="paginator" @navigate="paginatorNavigate" />
    </div>
  </panel>
</template>

<script>
import { Paginator } from 'elements';
export default {
  name: 'notifications',
  data () {
    return {
      paginator: {},
      loading: false
    };
  },
  components: {
    Paginator
  },
  created () {
    this.getNotifications(0);
  },
  methods: {
    getNotifications (page) {
      this.loading = true;
      var size = 8;
      var url = ['api/user/notification', page, size].join('/');
      this.$http.get(url).then(response => {
        this.loading = false;
        this.paginator = response.body;
      });
    },
    paginatorNavigate (e) {
      if (e.direction != null) {
        this.getNotifications(this.paginator.number + e.direction);
      } else if (e.page != null) {
        this.getNotifications(e.page);
      }
    },
    toggleSeen (notification) {
      this.$http.patch('api/notification/' + notification.id + '/toggle-seen').then(response => {
        var index = this.paginator.content.indexOf(notification);

        this.paginator.content.splice(index, 1, response.body);
      });
    }
  }
};
</script>

<style>

</style>
