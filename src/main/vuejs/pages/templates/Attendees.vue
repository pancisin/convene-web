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
              <th>User</th>
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
              <td class="text-right">{{ inv.created | moment('L') }}</td>
              <th class="text-right">
                <a class="text-danger btn btn-link btn-xs" @click="cancel(inv.id)">
                  Cancel
                </a>
              </th>
            </tr>
            <tr v-if="invitations.length == 0">
              <td class="text-center" colspan="3">
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
import UserApi from '../../services/api/user.api.js'

export default {
  name: 'event-attendees',
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
  methods: {
    searchUsers(search, loading) {
      loading(true);
      UserApi.searchUsers(search, users => {
        this.users = users;
        loading(false);
      })
    },
    cancel(invitation_id) {
      this.$http.delete('api/invitation/' + invitation_id).then(response => {
        this.invitations = this.invitations.filter(x => {
          return x.id != invitation_id;
        })
      })
    }
  }
}
</script>
