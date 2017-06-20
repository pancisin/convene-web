<script>
import AttendeesTemplate from '../templates/Attendees.vue'

export default {
  name: 'conference-attendees',
  props: ['conference'],
  extends: AttendeesTemplate,
  inject: ['api'],
  created() {
    this.initialize();
  },
  watch: {
    'conference': 'initialize'
  },
  methods: {
    initialize() {
      this.api.getAttendees(this.conference.id, attendees => {
        this.attendees = attendees;
      });

      this.api.getInvitations(this.conference.id, invitations => {
        this.invitations = invitations;
      });
    },
    invite() {
      this.api.postInvitation(this.conference.id, this.invitations, invitation => {
        this.invitations.push(response.body);
        this.invitation = {};
      })
    }
  }
}
</script>
