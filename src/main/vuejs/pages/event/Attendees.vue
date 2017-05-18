<template>
  <div class="row">
    <div class="col-md-6">
      <panel type="table">
        <span slot="title">Invitations</span>
  
        <div class="input-group" style="margin: 0px 20px 20px 20px">
          <input type="email" class="form-control" placeholder="Email">
          <span class="input-group-btn">
            <button type="button" class="btn waves-effect waves-light btn-primary">Invite</button>
          </span>
        </div>
  
        <table class="table">
          <thead>
            <tr>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inv in invitations">
              <td v-text="inv.email"></td>
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
export default {
  name: 'event-attendees',
  props: ['event'],
  data() {
    return {
      attendees: [],
      invitations: []
    }
  },
  created() {
    this.getAttendees();
  },
  watch: {
    'event': 'getAttendees'
  },
  methods: {
    getAttendees() {
      if (this.event.id == null) return;

      var url = ['api/event', this.event.id, 'attendees'].join('/');
      this.$http.get(url).then(response => {
        this.attendees = response.body;
      })
    }
  }
}
</script>
