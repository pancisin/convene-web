<template>
  <form @submit.prevent="submit">
    <div class="row">
      <div class="col-md-6">
        <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
          <label class="control-label">Name</label>
          <input class="form-control required" v-model="survey.name" type="text" name="name" v-validate data-vv-rules="required|min:3">
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
        </div>
      </div>
      <div class="col-md-6">
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
      </div>
    </div>

    <h3>Fields</h3>
    <hr />
    <form-editor v-if="survey.form" v-model="survey.form" />

    <div class="text-center">
      <a v-if="survey.state == 'IN_PROGRESS'" class="btn btn-danger btn-rounded" @click="togglePublished">Unpublish</a>
      <a v-else-if="survey.state == 'NEW'" class="btn btn-success btn-rounded" @click="togglePublished">Publish</a>

      <button class="btn btn-rounded btn-primary" type="submit">Save</button>
    </div>
  </form>
</template>

<script>
import SurveyApi from 'api/survey.api';
import { DatePicker, FormEditor } from 'elements';
export default {
  name: 'survey-form',
  props: {
    survey: Object
  },
  components: {
    DatePicker,
    FormEditor
  },
  methods: {
    submit () {
      // this.survey.metaFields.forEach((m, index) => {
      //   this.survey.metaFields[index].ordering = index;
      // });

      if (this.survey.id != null) {
        SurveyApi.putSurvey(this.survey.id, this.survey, response => {
          this.survey = response;
          // this.survey.metaFields.sort((a, b) => {
          //   return a.ordering >= b.ordering;
          // });

          this.$success('notification.survey.updated', this.survey.name);
        });
      } else {

      }
    },
    togglePublished () {
      SurveyApi.togglePublished(this.survey.id, result => {
        this.survey = result;
        // this.survey.metaField+s.sort((a, b) => {
        //   return a.ordering >= b.ordering;
        // });
      });
    }
  }
};
</script>

<style>

</style>
