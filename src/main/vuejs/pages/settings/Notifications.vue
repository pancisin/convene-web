<template>
 <panel type="table" v-loading="loading">
    <span slot="title">Notifications</span>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>
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
          <td>{{ not.created | moment('from') }}</td>
          <td>
            <span v-t="{ path: not.code + '.title', args: { subject: not.target }}">
            </span>
          </td>
          <td>
            <span v-t="{ path: not.code + '.message', args: { subject: not.target }}">
            </span>
          </td>
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
      <template slot-scope="props">
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
      <paginator :paginator="paginator" :fetch="getNotifications" history/>
    </div>
  </panel>
</template>

<script>
import UserApi from 'api/user.api';
import { Paginator } from 'elements';
import NotificationApi from 'api/notification.api';

export default {
  data () {
    return {
      paginator: {},
      loading: false
    };
  },
  components: {
    Paginator
  },
  methods: {
    getNotifications (page) {
      UserApi.getNotifications(page, 7, paginator => {
        this.paginator = paginator;
      });
    },
    toggleSeen (not) {
      NotificationApi.toggleSeen(not.id, notification => {
        const index = this.paginator.content.findIndex(n => n.id === notification.id);
        this.paginator.content.splice(index, 1, notification);
      });
    }
  }
};
</script>
