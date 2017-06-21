<template>
  <form class="form" @submit.prevent="submit">
    <div class="form-group" v-for="(field, index) in meta_fields">
      <label v-text="field.name"></label>
  
      <input v-if="field.type == 'NUMBER'" v-model="meta_values[index].value" type="number" class="form-control" />
      <input v-else="field.type == 'TEXT'" v-model="meta_values[index].value" type="text" class="form-control" />
    </div>
  
    <input type="submit" class="btn btn-primary btn-rounded" />
  </form>
</template>

<script>
export default {
  name: 'conference-application',
  inject: ['api'],
  data() {
    return {
      meta_fields: [],
      meta_values: []
    }
  },
  created() {
    this.api.getMetaFields(fields => {
      this.meta_fields = fields;

      for (var i = 0; i < fields.length; i++) {
        this.meta_values.push({
          field: {
            id: fields[i].id,
          },
          value: null,
        })
      }
    })
  },
  methods: {
    submit() {
      this.api.postAttend(this.meta_values, attendant => {
        console.log(attendant);
      })
    }
  }
}
</script>

<style>

</style>
