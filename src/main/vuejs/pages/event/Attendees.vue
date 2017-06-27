<script>
import AttendeesTemplate from '../templates/Attendees.vue'
export default {
  name: 'event-attendees',
  extends: AttendeesTemplate,
  inject: ['api'],
  created() {
    this.initialize();
  },
  watch: {
    'event': 'initialize'
  },
  methods: {
    initialize() {
      this.api.getAttendees(attendees => {
        this.attendees = attendees;
      })
      this.api.getInvitations(invitations => {
        this.invitations = invitations;
      })
    },
    invite() {
      this.api.postInvitation(this.invitation, invitation => {
        this.invitations.push(invitation);
        this.invitation = {};
      })
    }
  }
}
</script>
