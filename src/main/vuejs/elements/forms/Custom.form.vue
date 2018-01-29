<template>
  <form class="form" @submit.prevent="submit">
    <div class="form-group" v-for="(field, index) in form.formFields" :key="index" :class="{ 'has-error' : errors.has(`input-${index}`) }">
      <label v-text="field.name"></label>
      <p v-if="field.description">
        <small v-text="field.description"></small>
        <br>
        <br>
      </p>
      <input v-if="field.type == 'NUMBER'" type="number" class="form-control" v-model="meta_values[index].value" v-validate="!field.optional ? 'required' : ''" :name="`input-${index}`" :data-vv-as="field.name" />
      <select v-else-if="field.type == 'SELECT'" class="form-control" v-model="meta_values[index].value" v-validate="!field.optional ? 'required' : ''" :name="`input-${index}`" :data-vv-as="field.name">
        <option :value="null">- select one -</option>
        <option v-for="option in field.options" :value="option" :key="option">{{ option }}</option>
      </select>
      <div v-else-if="field.type == 'RADIO'" v-validate="!field.optional ? 'required' : ''" :name="`input-${index}`" :data-vv-as="field.name">
        <div class="radio radio-primary" v-for="(option, i) in field.options" :key="i">
          <input :id="'radio-' + i" type="radio" :value="option" v-model="meta_values[index].value">
          <label :for="'radio-' + i">
            {{ option }}
          </label>
        </div>
      </div>
      <date-picker v-else-if="field.type == 'DATE'" v-model="meta_values[index].value" v-validate="!field.optional ? 'required' : ''" :name="`input-${index}`" :data-vv-as="field.name" />
      <input v-else-if="field.type == 'TEXT'" type="text" class="form-control" v-model="meta_values[index].value" v-validate="!field.optional ? 'required' : ''" :name="`input-${index}`" :data-vv-as="field.name" />
      <span class="text-danger" v-if="errors.has(`input-${index}`)">{{ errors.first(`input-${index}`) }}</span>
    </div>

    <div class="text-center m-b-15">
      <input type="submit" class="btn btn-primary btn-rounded" />
    </div>
  </form>
</template>

<script>
import { DatePicker } from 'elements';
import FormApi from 'api/form.api';

export default {
  name: 'custom-form',
  data () {
    return {
      meta_values: [],
      loading: false
    };
  },
  props: {
    form: {
      type: Object,
      required: true
    }
  },
  components: {
    DatePicker
  },
  created () {
    this.initializeSurvey();
  },
  methods: {
    initializeSurvey () {
      this.form.formFields.sort((a, b) => {
        return a.ordering > b.ordering;
      });

      this.form.formFields.forEach((field, index) => {
        this.meta_values.push({
          field: {
            id: field.id
          },
          value: null
        });
      });
    },
    submit () {
      this.$validator.validateAll().then(valid => {
        if (!valid) return;
        FormApi.postSubmission(this.form.id, this.meta_values, result => {
          this.$emit('submit', this.form);
        });
      });
    }
  }
};
</script>
