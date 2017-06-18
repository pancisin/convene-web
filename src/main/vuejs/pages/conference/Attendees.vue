<script>
import AttendeesTemplate from '../templates/Attendees.vue'

export default {
  name: 'conference-attendees',
  props: ['conference'],
  extends: AttendeesTemplate,
  created() {
    this.initialize();
  },
  watch: {
    'conference': 'initialize'
  },
  methods: {
    initialize() {
      this.getAttendees();
      this.getInvitations();
    },
    getAttendees() {
      if (this.conference.id == null) return;

      var url = ['api/conference', this.conference.id, 'attendees'].join('/');
      this.$http.get(url).then(response => {
        this.attendees = response.body;
      })
    },
    getInvitations() {
      if (this.conference.id == null) return;

      var url = ['api/conference', this.conference.id, 'invitation'].join('/');
      this.$http.get(url).then(response => {
        this.invitations = response.body;
      })
    },
    invite() {
      var url = ['api/conference', this.conference.id, 'invite'].join('/');

      this.$http.post(url, this.invitation).then(response => {
        this.invitations.push(response.body);
        this.invitation = {};
      })
    }
  }
}
</script>
