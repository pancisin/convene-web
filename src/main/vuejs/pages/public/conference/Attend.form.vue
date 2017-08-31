<template>
  <form class="form" @submit.prevent="submit">
    <div class="form-group" v-for="(field, index) in meta_fields" :key="index">
      <label v-text="field.name"></label>
      <p v-if="field.description">
        <small v-text="field.description"></small>
        <br>
        <br>
      </p>

      <input v-if="field.type == 'NUMBER'" v-model="meta_values[index].value" type="number" class="form-control" />
      <select v-else-if="field.type == 'SELECT'" v-model="meta_values[index].value" class="form-control">
        <option v-for="option in field.options" :value="option" :key="option">{{ option }}</option>
      </select>
      <div v-else-if="field.type == 'RADIO'">
        <div class="radio radio-primary" v-for="(option, i) in field.options" :key="i">
          <input :id="'radio-' + i" type="radio" :value="option" v-model="meta_values[index].value">
          <label :for="'radio-' + i">
            {{ option }}
          </label>
        </div>
      </div>
      <date-picker v-else-if="field.type == 'DATE'" v-model="meta_values[index].value" />
      <input v-else-if="field.type == 'TEXT'" v-model="meta_values[index].value" type="text" class="form-control" />
    </div>

    <div class="text-center">
      <input type="submit" class="btn btn-primary btn-rounded" />
    </div>
  </form>
</template>

<script>
import DatePicker from '../../../elements/DatePicker.vue';
export default {
  name: 'conference-application',
  inject: ['api'],
  data () {
    return {
      meta_fields: [],
      meta_values: []
    };
  },
  components: {
    DatePicker
  },
  created () {
    this.api.getMetaFields(fields => {
      this.meta_fields = fields;

      for (var i = 0; i < fields.length; i++) {
        this.meta_values.push({
          field: {
            id: fields[i].id
          },
          value: null
        });
      }
    });
  },
  methods: {
    submit () {
      this.api.postAttend(this.meta_values, status => {
        this.$emit('statusChanged', status);
      });
    }
  }
};
</script>

<style>

</style>
