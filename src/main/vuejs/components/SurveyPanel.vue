<script>
import FormApi from 'api/form.api';
import { VueTable } from 'elements';

export default {
  name: 'survey-panel',
  props: {
    survey: Object
  },
  data () {
    return {
      submissions: []
    };
  },
  render (h) {
    return h('vue-table', {
      attrs: {
        func: this.tableRender,
        data: this.submissions,
        contextmenu: this.contextmenu
      }
    });
  },
  components: {
    VueTable
  },
  created () {
    this.getSubmissions();
  },
  methods: {
    getSubmissions () {
      if (this.survey && this.survey.form) {
        FormApi.getSubmissions(this.survey.form.id, submissions => {
          this.submissions = submissions;
        });
      }
    },
    tableRender (submission) {
      const fields = this.survey.form.formFields.reduce((acc, field) => {
        var value = submission.values.find(v => v.field === field.id);
        acc[field.name] = value ? value.value : '-';
        return acc;
      }, {});

      return {
        user: submission.user,
        ...fields
      };
    },
    contextmenu (item) {
      return [
        item('Delete', this.deleteSubmission)
      ];
    }
  }
};
</script>
