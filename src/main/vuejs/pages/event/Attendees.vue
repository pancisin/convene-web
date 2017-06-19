<script>
import AttendeesTemplate from '../templates/Attendees.vue'
export default {
  name: 'event-attendees',
  props: ['event'],
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
      if (this.event.id != null) {
        this.api.getAttendees(this.event.id, attendees => {
          this.attendees = attendees;
        })
        this.api.getInvitations(this.event.id, invitations => {
          this.invitations = invitations;
        })
      }

      this.getAttendees();
      this.getInvitations();
    },
    invite() {
      this.api.postInvitation(this.event.id, this.invitation, invitation => {
        this.invitations.push(invitation);
        this.invitation = {};
      })
    }
  }
}
</script>
