<script>
import FormSubmissionApi from 'api/survey-submission.api';
import FormApi from 'api/form.api';
import { VueTable } from 'elements';

export default {
  name: 'survey-submissions',
  inject: ['provider'],
  props: {
    survey: Object
  },
  data () {
    return {
      submissions: []
    };
  },
  render (h) {
    return h('panel', {
      attrs: {
        type: 'table'
      }
    }, [
      h('span', {
        slot: 'title'
      }, 'Results table'),
      h('vue-table', {
        attrs: {
          func: this.tableRender,
          data: this.submissions
        }
      })
    ]);
  },
  components: {
    VueTable
  },
  computed: {
    api () {
      return this.provider.api;
    },
    metaFields () {
      if (!this.survey.form) {
        return [];
      }

      const fields = this.survey.form.formFields;

      if (fields) {
        fields.sort((a, b) => a.ordering >= b.ordering);
        return fields;
      } else {
        return [];
      }
    }
  },
  created () {
    this.getSubmissions();
  },
  watch: {
    'api': 'getSubmissions'
  },
  methods: {
    getSubmissions () {
      if (this.api && this.survey.form) {
        FormApi.getSubmissions(this.survey.form.id, submissions => {
          this.submissions = submissions;
        });
      }
    },
    tableRender (submission) {
      const fields = this.metaFields.reduce((acc, field) => {
        acc[field.name] = this.getSubmissionValue(submission, field.id);
        return acc;
      }, {});

      return {
        user: submission.user,
        ...fields,
        action: {
          el: 'a',
          content: 'delete',
          onClick: submission => {
            this.deleteSubmission(submission.id);
          }
        }
      };
    },
    getSubmissionValue (submission, field_id) {
      const fields = submission.values.filter(e => e.field === field_id);

      if (fields.length > 0) {
        return fields[0].value;
      }
    },
    deleteSubmission (submission_id) {
      this.$prompt('notification.delete_prompt', '', () => {
        FormSubmissionApi.deleteFormSubmission(submission_id, result => {
          this.submissions = this.submissions.filter(s => s.id !== submission_id);
        });
      });
    }
  }
};
</script>
