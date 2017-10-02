<template>
  <div class="list-group mail-list">
    <router-link :to="{ name: 'system.list', params: { list_id: list.id } }" v-for="list in articlesLists" :key="list.id" class="list-group-item waves-effect">
      {{ list.name }}
    </router-link>
  </div>
</template>

<script>
import ArticlesListApi from 'api/articles-list.api';
export default {
  name: 'articles-lists',
  data () {
    return {
      articlesLists: []
    };
  },
  created () {
    ArticlesListApi.getArticlesLists(articlesLists => {
      this.articlesLists = articlesLists;
    });
  },
  methods: {

  }
};
</script>