<template>
  <div class="row">
    <div class="col-md-6">
        <span slot="title">Invitations</span>

        <form class="form"
          @submit.prevent="invite">
          <div class="input-group">
            <user-search v-model="invitation.email"></user-search>
            <!--<input type="email" v-model="invitation.email" class="form-control" placeholder="Email">-->
            <span class="input-group-btn">
              <button type="submit"
                class="btn waves-effect waves-light btn-primary">
                Invite
              </button>
            </span>
          </div>
        </form>

        <table class="table table-striped">
          <thead>
            <tr>
              <th>User</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inv in invitations"
              :key="inv.id">
              <td v-text="inv.email"
                v-if="inv.user == null"></td>
              <td v-else>
                {{ inv.user.firstName }} {{ inv.user.lastName }}
              </td>
              <td class="text-right">{{ inv.created | luxon('FF') }}</td>
              <th class="text-right">
                <a class="text-danger btn btn-link btn-xs"
                  @click="cancel(inv.id)">
                  Cancel
                </a>
              </th>
            </tr>
            <tr v-if="invitations.length == 0">
              <td class="text-center"
                colspan="3">
                There's nothing to display.
              </td>
            </tr>
          </tbody>
        </table>

    </div>
    <div class="col-md-6">
      <span slot="title">Attendees</span>
      <user-list :users="attendees"></user-list>
    </div>
  </div>
</template>

<script>
import {
  UserSearch,
  UserList
} from 'elements';

export default {
  name: 'attendees',
  inject: ['provider'],
  components: {
    UserSearch,
    UserList
  },
  props: {
    editable: Boolean
  },
  created () {
    this.initialize();
  },
  data () {
    return {
      attendees: [],
      invitations: [],
      invitation: {}
    };
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  methods: {
    cancel (invitation_id) {
      this.$http.delete('/api/v1/invitation/' + invitation_id).then(response => {
        this.invitations = this.invitations.filter(x => {
          return x.id !== invitation_id;
        });
      });
    },
    initialize () {
      this.api.getAttendees(attendees => {
        this.attendees = attendees;
      });
      this.api.getInvitations(invitations => {
        this.invitations = invitations;
      });
    },
    invite () {
      this.api.postInvitation(this.invitation, invitation => {
        this.invitations.push(invitation);
        this.invitation = {};
      });
    }
  }
};
</script>
