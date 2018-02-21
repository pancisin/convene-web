<template>
  <form class="form" @submit.prevent="submit">

    <div class="form-group">
      <label>Name</label>
      <input type="text" class="form-control" v-model="listClone.name">
    </div>

    <div class="form-group">
      <label>Tags</label>
      <tag-input 
        v-model="listClone.tags">
      </tag-input>
    </div>

    <div class="text-center">
      <button type="submit" class="btn btn-primary">Save</button>
    </div>
  </form>
</template>

<script>
import { TagInput } from 'elements';
export default {
  name: 'articles-list-settings',
  inject: ['provider'],
  props: {
    list: Object
  },
  data: {
    listClone: null
  },
  components: {
    TagInput
  },
  created () {
    this.listClone = { ...this.list };
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  methods: {
    submit () {
      this.api.putArticlesList(this.listClone, list => {
        this.$success('update', list);
      });
    }
  }
};
</script>

<style>

</style>
