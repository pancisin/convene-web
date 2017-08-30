<template>
  <panel type="table" v-loading="loading">
    <span slot="title">{{ $t('admin.page.events') }}</span>

    <table class="table table-striped table-hover">
      <thead>
        <tr>
          <th>
            Name
          </th>
          <th>Start date</th>
          <th>End date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="survey in surveys" :key="survey.id">
          <td>
            <router-link :to="{ name: 'survey.overview', params: { survey_id: survey.id } }">
              {{ survey.name }}
            </router-link>
          </td>
          <td>{{ survey.start_date | moment('L') }}</td>
          <td>{{ survey.end_date | moment('L') }}</td>
        </tr>
      </tbody>
    </table>

    <div class="text-center" v-if="editable">
      <router-link :to="{ name: 'conference.survey.create' }" class="btn btn-default btn-rounded text-center">
        Create survey
      </router-link>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'surveys',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
  data () {
    return {
      surveys: [],
      loading: false
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  created () {
    this.getSurveys();
  },
  watch: {
    'api': 'getSurveys'
  },
  methods: {
    getSurveys () {
      if (this.api != null) {
        this.loading = true;
        this.api.getSurveys(surveys => {
          this.surveys = surveys;
          this.loading = false;
        });
      }
    }
  }
};
</script>