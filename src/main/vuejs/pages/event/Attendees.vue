<script>
import AttendeesTemplate from '../templates/Attendees.vue'
export default {
  name: 'event-attendees',
  props: ['event'],
  extends: AttendeesTemplate,
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
    }
  }
}
</script>
