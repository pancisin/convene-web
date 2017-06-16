<template>
  <div class="row">
    <div class="col-md-6">
      <panel type="table">
        <span slot="title">Invitations</span>
  
        <form class="form" @submit.prevent="invite">
          <div class="input-group" style="margin: 0px 20px 20px 20px">
            <suggest-input v-model="invitation.email" :options="users" @search="searchUsers"></suggest-input>
            <!--<input type="email" v-model="invitation.email" class="form-control" placeholder="Email">-->
            <span class="input-group-btn">
              <input type="submit" class="btn waves-effect waves-light btn-primary">Invite</input>
            </span>
          </div>
        </form>
  
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Email</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inv in invitations">
              <td v-text="inv.email" v-if="inv.user == null"></td>
              <td v-else>
                {{ inv.user.firstName }} {{ inv.user.lastName }}
              </td>
              <td class="text-right">{{ inv.created | moment('DD.MM.YYYY') }}</td>
              <th>
                <a class="text-danger btn btn-link btn-xs">
                  Cancel
                </a>
              </th>
            </tr>
            <tr v-if="invitations.length == 0">
              <td class="text-center">
                There's nothing to display.
              </td>
            </tr>
          </tbody>
        </table>
  
      </panel>
    </div>
    <div class="col-md-6">
  
      <panel type="table">
        <span slot="title">Attendees</span>
        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="attender in attendees">
              <td>
                {{ attender.firstName + " " + attender.lastName }}
              </td>
            </tr>
            <tr v-if="attendees.length == 0">
              <td>There's nothing to display</td>
            </tr>
          </tbody>
        </table>
      </panel>
  
    </div>
  </div>
</template>

<script>
import SuggestInput from '../../elements/UserSuggestInput.vue'

export default {
  name: 'event-attendees',
  props: ['event'],
  components: {
    SuggestInput
  },
  data() {
    return {
      attendees: [],
      invitations: [],
      invitation: {},
      users: [],
    }
  },
  created() {
    this.initialize();
  },
  watch: {
    'event': 'initialize'
  },
  methods: {
    initialize() {
      this.getAttendees();
      this.getInvitations();
    },
    getAttendees() {
      if (this.event.id == null) return;

      var url = ['api/event', this.event.id, 'attendees'].join('/');
      this.$http.get(url).then(response => {
        this.attendees = response.body;
      })
    },
    getInvitations() {
      if (this.event.id == null) return;

      var url = ['api/event', this.event.id, 'invitation'].join('/');
      this.$http.get(url).then(response => {
        this.invitations = response.body;
      })
    },
    invite() {
      var url = ['api/event', this.event.id, 'invite'].join('/');

      this.$http.post(url, this.invitation).then(response => {
        this.invitations.push(response.body);
        this.invitation = {};
      })
    },
    searchUsers(search, loading) {
      console.warn(search);

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
  }
}
</script>
