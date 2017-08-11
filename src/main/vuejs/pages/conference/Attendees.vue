<script>
import AttendeesTemplate from '../templates/Attendees.vue';

export default {
  name: 'conference-attendees',
  extends: AttendeesTemplate,
  inject: ['provider'],
  computed: {
    api () {
      return this.provider.api;
    }
  },
  created () {
    this.initialize();
  },
  methods: {
    initialize () {
      this.api.getAttendees(attendees => {
        this.attendees = attendees;
      });

      this.api.getInvitations(invitations => {
        this.invitations = invitations;
      });
    },
    invite () {
      this.api.postInvitation(this.invitations, invitation => {
        this.invitations.push(invitation);
        this.invitation = {};
      });
    }
  }
};
</script>
