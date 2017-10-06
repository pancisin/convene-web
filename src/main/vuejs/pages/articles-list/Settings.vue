<template>
  <panel>
    <span slot="title">List settings</span>
    <form class="form" @submit.prevent="submit">

      <div class="form-group">
        <label>Name</label>
        <input type="text" class="form-control" v-model="list.name">
      </div>

      <div class="form-group">
        <label>Tags</label>
        <tag-input v-model="list.tags" :tags="list.tags"></tag-input>
      </div>

      <div class="text-center">
        <button type="submit" class="btn btn-primary">Save</button>
      </div>
    </form>
  </panel>
</template>

<script>
import { TagInput } from 'elements';
export default {
  name: 'articles-list-settings',
  inject: ['provider'],
  data () {
    return {
      list: null
    };
  },
  components: {
    TagInput
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  watch: {
    'api': 'initializeList'
  },
  created () {
    this.initializeList();
  },
  methods: {
    initializeList () {
      this.list = this.api.instance;
    },
    submit () {
      this.api.putArticlesList(this.list, list => {
        this.list = list;
        this.$success('dsada');
      });
    }
  }
};
</script>

<style>

</style>
