<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-if="survey != null" v-text="survey.name"></h4>
      </div>
    </div>

    <div class="col-xs-12">
      <panel type="default">
        <span slot="title">Overview</span>

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

        <transition-group name="fade-down">
          <div class="row meta-field-row" v-for="(field, index) in survey.metaFields" :key="index">
            <div class="col-md-6">
              <button class="btn btn-danger btn-rounded" v-if="field.id != null" @click="removeMeta(field)">Remove meta field</button>

              <div class="form-group">
                <label class="control-label">Name</label>
                <input class="form-control required" v-model="field.name" type="text">
              </div>

            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label class="control-label">Description</label>
                <input class="form-control" v-model="field.description" type="text">
              </div>
            </div>
          </div>
        </transition-group>

        <button @click="survey.metaFields.push({})">Add field</button>

        <div class="text-center">
          <button class="btn btn-rounded btn-primary" type="submit" @click="submit">Save</button>
        </div>
      </panel>
    </div>
  </div>
</template>


<script>
import SurveyApi from 'api/survey.api';
import DatePicker from '../elements/DatePicker.vue';

export default {
  name: 'survey',
  data () {
    return {
      survey: {}
    };
  },
  components: {
    DatePicker
  },
  created () {
    this.getSurvey();
  },
  methods: {
    getSurvey () {
      SurveyApi.getSurvey(this.$route.params.survey_id, survey => {
        this.survey = survey;
      });
    },
    submit () {
      if (this.survey.id != null) {
        SurveyApi.putSurvey(this.survey.id, this.survey, response => {
          this.survey = response;
          this.$success('saved');
        });
      } else {

      }
    },
    removeMeta (meta) {
      this.survey.metaFields = this.survey.metaFields.filter(m => {
        return m.id !== meta.id;
      });
    }
  }
};
</script>

<style lang="less">
.meta-field-row {
  border: 1px solid #eee;
  padding-top: 15px;
  padding-bottom: 15px;

  &~.meta-field-row {
    margin-top: 15px;
  }
}
</style>
