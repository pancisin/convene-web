<script>
import { VueTable } from 'elements';
export default {
  name: 'conference-attendees-new',
  inject: ['provider'],
  props: ['conference'],
  data () {
    return {
      members: []
    };
  },
  render (h) {
    return h('panel', {
      props: {
        type: 'table'
      }
    }, [
      h('span', {
        slot: 'title'
      }, 'Attendees'),
      h('vue-table', {
        props: {
          func: this.tableRender,
          data: this.members
        }
      })
    ]);
  },
  created () {
    this.initialize();
  },
  components: {
    VueTable
  },
  computed: {
    api () {
      return this.provider.api;
    },
    metaFields () {
      if (this.conference && this.conference.registrationForm != null) {
        return this.conference.registrationForm.formFields;
      } else return [];
    }
  },
  methods: {
    tableRender (member) {
      const getMeta = (fieldId) => {
        const fields = member.submission.values.filter(e => e.field === fieldId);

        if (fields.length > 0) {
          return fields[0].value;
        }
      };

      const fields = this.metaFields.reduce((acc, field) => {
        acc[field.name] = getMeta(field.id);
        return acc;
      }, {});

      return {
        name: member.user.displayName,
        email: member.user.email,
        ...fields
      };
    },
    initialize () {
      this.api.getAttendees(members => {
        this.members = members;
      });
    }
  }
};
</script>
