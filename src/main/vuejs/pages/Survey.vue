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
            <div class="col-md-2 col-lg-1 text-center">
              <h3>
                {{ index + 1 }}.
              </h3>

              <div class="m-b-10">
                <button class="btn btn-default btn-rounded btn-xs" @click="moveMeta(index, -1)" v-if="index > 0">
                  <i class="fa fa-arrow-up"></i>
                </button>

                <button class="btn btn-default btn-rounded btn-xs" @click="moveMeta(index, 1)" v-if="index + 1 < survey.metaFields.length">
                  <i class="fa fa-arrow-down"></i>
                </button>
              </div>

              <button class="btn btn-danger btn-rounded btn-xs" @click="removeMeta(index)">
                <i class="fa fa-minus"></i>
              </button>
            </div>

            <div class="col-md-5">
              <div class="form-group">
                <label class="control-label">Name</label>
                <input class="form-control required" v-model="field.name" type="text">
              </div>
              <div class="form-group">
                <label class="control-label">Description</label>
                <input class="form-control" v-model="field.description" type="text">
              </div>
              <div class="form-group">
                <div class="checkbox checkbox-primary">
                  <input id="optional-checkbox" type="checkbox" v-model="field.optional">
                  <label for="optional-checkbox">
                    Optional
                  </label>
                </div>
              </div>
            </div>
            <div class="col-md-5 col-lg-6">
              <div class="form-group">
                <label class="control-label">Type: </label>

                <select class="form-control" v-model="field.type">
                  <option v-for="mtype in meta_types" v-text="mtype" :key="mtype"></option>
                </select>
              </div>

              <div class="form-group" v-if="field.type == 'SELECT' || field.type == 'RADIO'">
                <label class="control-label">Options: </label>

                <ul class="list-unstyled">
                  <li v-for="(option, op_in) in field.options" :key="option" class="m-b-10">
                    {{ option }}
                    <a class="pull-right" @click="deleteOption(field, op_in)">
                      <i class="fa fa-times text-danger"></i>
                    </a>
                  </li>
                  <li>
                    <form @submit.prevent="addOption(field, $event)">
                      <div class="form-group">
                        <div class="input-group">
                          <input type="text" class="form-control" name="option">
                          <span class="input-group-btn">
                            <button class="btn btn-primary" type="submit">
                              <i class="fa fa-plus"></i> Add
                            </button>
                          </span>
                        </div>
                      </div>
                    </form>
                  </li>
                </ul>
              </div>
            </div>

          </div>
        </transition-group>

        <div class="text-center">
          <button @click="survey.metaFields.push({})" class="btn btn-rounded btn-lg m-t-15 m-b-15">
            <i class="fa fa-plus"></i>
          </button>
        </div>

        <div class="text-center">
          <a v-if="survey.state == 'IN_PROGRESS'" class="btn btn-danger btn-rounded" @click="togglePublished">Unpublish</a>
          <a v-else-if="survey.state == 'NEW'" class="btn btn-success btn-rounded" @click="togglePublished">Publish</a>

          <button class="btn btn-rounded btn-primary" type="submit" v-show="touched" @click="submit">Save</button>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import SurveyApi from 'api/survey.api';
import DatePicker from '../elements/DatePicker.vue';
import PublicApi from 'api/public.api';
import { calculateHash } from '../services/helpers';

export default {
  name: 'survey',
  data () {
    return {
      original_survey: null,
      survey: {},
      meta_types: []
    };
  },
  components: {
    DatePicker
  },
  created () {
    PublicApi.getMetaTypes(meta_types => {
      this.meta_types = meta_types;
    });

    this.getSurvey();
  },
  computed: {
    touched () {
      return calculateHash(this.original_survey) !== calculateHash(JSON.stringify(this.survey));
    }
  },
  beforeRouteLeave (to, from, next) {
    if (this.touched) {
      this.$info('You have unsaved changes !');
    } else {
      next();
    }
  },
  methods: {
    getSurvey () {
      SurveyApi.getSurvey(this.$route.params.survey_id, survey => {
        this.survey = survey;
        this.survey.metaFields.sort((a, b) => {
          return a.ordering >= b.ordering;
        });

        this.original_survey = JSON.stringify(this.survey);
      });
    },
    submit () {
      this.survey.metaFields.forEach((m, index) => {
        this.survey.metaFields[index].ordering = index;
      });

      if (this.survey.id != null) {
        SurveyApi.putSurvey(this.survey.id, this.survey, response => {
          this.survey = response;
          this.survey.metaFields.sort((a, b) => {
            return a.ordering >= b.ordering;
          });

          this.original_survey = JSON.stringify(this.survey);
          this.$success('saved');
        });
      } else {

      }
    },
    removeMeta (index) {
      this.survey.metaFields.splice(index, 1);
    },
    moveMeta (index, direction) {
      let element = this.survey.metaFields[index];
      this.survey.metaFields.splice(index, 1);
      this.survey.metaFields.splice(index + direction, 0, element);
    },
    deleteOption (field, index) {
      field.options.splice(index, 1);
    },
    addOption (field, e) {
      let option = e.target.option.value;

      if (field.options == null) {
        field.options = [];
      }

      if (option != null && option.trim() !== '') {
        field.options.push(option);
        e.target.option.value = null;
      }
    },
    togglePublished () {
      SurveyApi.togglePublished(this.survey.id, result => {
        this.survey = result;
        this.survey.metaFields.sort((a, b) => {
          return a.ordering >= b.ordering;
        });

        this.original_survey = JSON.stringify(this.survey);
      })
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
