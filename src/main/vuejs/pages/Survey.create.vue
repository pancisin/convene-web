<template>
  <panel type="default">
    <span slot="title">Overview</span>

    <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
      <label class="control-label">Name</label>
      <input class="form-control required" v-model="survey.name" type="text" name="name" v-validate data-vv-rules="required|min:3">
      <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
    </div>
    <div class="form-group" :class="{ 'has-error' : errors.has('start_date') }">
      <label class="control-label">Start date</label>
      <date-picker v-model="survey.start_date" v-validate data-vv-rules="required" name="start_date"></date-picker>
      <span class="text-danger" v-if="errors.has('name')">{{ errors.first('start_date') }}</span>
    </div>
    <div class="form-group" :class="{ 'has-error' : errors.has('end_date') }">
      <label class="control-label">End date</label>
      <date-picker v-model="survey.end_date" v-validate data-vv-rules="required" name="end_date"></date-picker>
      <span class="text-danger" v-if="errors.has('name')">{{ errors.first('end_date') }}</span>
    </div>

    <div class="text-center">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">Save</button>
    </div>
  </panel>
</template>

<script>
import { DatePicker } from '../elements';

export default {
  name: 'survey-overview',
  inject: ['provider'],
  props: ['conference_id'],
  data () {
    return {
      survey: {}
    };
  },
  components: {
    DatePicker
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  created () {

  },
  methods: {
    submit () {
      this.api.postSurvey(this.survey, result => {
        this.$success('Survey has been created !', 'dsada');
        this.$router.push({ name: 'survey', params: { survey_id: result.id }});
      });
    }
  }
};
</script>

<style>

</style>
