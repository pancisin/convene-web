<template>
  <form class="form" @submit.prevent="submit">
    <div class="form-group" v-for="(field, index) in survey.metaFields" :key="index">
      <label v-text="field.name"></label>
      <p v-if="field.description">
        <small v-text="field.description"></small>
        <br>
        <br>
      </p>

      <input v-if="field.type == 'NUMBER'" type="number" class="form-control" />
      <select v-else-if="field.type == 'SELECT'" class="form-control">
        <option v-for="option in field.options" :value="option" :key="option">{{ option }}</option>
      </select>
      <div v-else-if="field.type == 'RADIO'">
        <div class="radio radio-primary" v-for="(option, i) in field.options" :key="i">
          <input :id="'radio-' + i" type="radio" :value="option">
          <label :for="'radio-' + i">
            {{ option }}
          </label>
        </div>
      </div>
      <date-picker v-else-if="field.type == 'DATE'" />
      <input v-else-if="field.type == 'TEXT'" type="text" class="form-control" />
    </div>

    <div class="text-center">
      <input type="submit" class="btn btn-primary btn-rounded" />
    </div>
  </form>
</template>

<script>
import DatePicker from '../../../elements/DatePicker.vue';
import SurveyApi from 'api/survey.api';

export default {
  name: 'survey',
  props: ['survey'],
  data () {
    return {
      meta: {}
    };
  },
  components: {
    DatePicker
  },
  methods: {
    submit () {
      SurveyApi.postMetaValues(this.survey.id, this.meta, result => {
        this.$success('published !');
      });
    }
  }
};
</script>

<style>

</style>
