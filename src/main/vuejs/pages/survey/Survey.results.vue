<template>
  <panel type="table">
    <span slot="title">Results table</span>

    <table class="table table-striped">
      <thead>
        <tr>
          <th>
            User
          </th>
          <th v-for="(field, index) in metaFields" :key="index">
            {{ field.name }}
          </th>
          <th>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="submission in submissions" :key="submission.id">
          <td>
            {{ submission.user }}
          </td>
          <td v-for="field in metaFields" :key="field.ordering">
            {{ getSubmissionValue(submission, field.id) || '-' }}
          </td>
          <td>
            <button class="btn btn-danger btn-xs" @click="deleteSubmission(submission.id)">
              <i class="fa fa-trash-o"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import SurveySubmissionApi from 'api/survey-submission.api';
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
  computed: {
    api () {
      return this.provider.api;
    },
    metaFields () {
      const fields = this.survey.metaFields;

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
      if (this.api) {
        this.api.getSubmissions(submissions => {
          this.submissions = submissions;
        });
      }
    },
    getSubmissionValue (submission, field_id) {
      const fields = submission.values.filter(e => e.field === field_id);

      if (fields.length > 0) {
        return fields[0].value;
      }
    },
    deleteSubmission (submission_id) {
      this.$prompt('notification.delete_prompt', '', () => {
        SurveySubmissionApi.deleteSurveySubmission(submission_id, result => {
          this.submissions = this.submissions.filter(s => s.id !== submission_id);
        });
      });
    }
  }
};
</script>
