<script>
import AttendeesTemplate from '../templates/Attendees.vue';
export default {
  name: 'event-attendees',
  extends: AttendeesTemplate,
  inject: ['provider'],
  created () {
    this.initialize();
  },
  computed: {
    api() {
      return this.provider.api;
    },
  },
  watch: {
    'api': 'initialize'
  },
  methods: {
    initialize () {
      if (this.api != null) {
        this.api.getAttendees(attendees => {
          this.attendees = attendees;
        });
        this.api.getInvitations(invitations => {
          this.invitations = invitations;
        });
      }
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
