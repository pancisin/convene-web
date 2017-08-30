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
            <div class="col-md-2 text-center">
              <button class="btn btn-danger btn-rounded btn-lg" @click="removeMeta(index)">
                <i class="fa fa-minus"></i>
              </button>

              <br>
              <button class="btn btn-rounded btn-lg" @click="moveMeta(index, -1)" v-if="index > 0">
                <i class="fa fa-arrow-up"></i>
              </button>

              <button class="btn btn-rounded btn-lg" @click="moveMeta(index, 1)" v-if="index + 1 < survey.metaFields.length">
                <i class="fa fa-arrow-down"></i>
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
            </div>
            <div class="col-md-5">
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

        <button @click="survey.metaFields.push({})" class="btn btn-default btn-rounded btn-lg m-t-15">
          <i class="fa fa-plus"></i>
        </button>

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
import PublicApi from 'api/public.api';

export default {
  name: 'survey',
  data () {
    return {
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
  methods: {
    getSurvey () {
      SurveyApi.getSurvey(this.$route.params.survey_id, survey => {
        this.survey = survey;
        this.survey.metaFields.sort((a, b) => {
          return a.ordering >= b.ordering;
        });
      });
    },
    submit () {
      this.survey.metaFields.forEach((m, index) => {
        this.survey.metaFields[index].ordering = index;
      });

      if (this.survey.id != null) {
        SurveyApi.putSurvey(this.survey.id, this.survey, response => {
          this.survey = response;
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
