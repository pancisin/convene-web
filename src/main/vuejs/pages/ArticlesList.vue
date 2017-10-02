<template>
  <div v-if="list != null">
    <h3 v-text="list.name" class="page-title"></h3>

    <div class="row">
      <div class="col-md-3">
        <div class="list-group mail-list">
          <router-link :to="{ name: 'system.list.articles' }" class="list-group-item waves-effect">
            <i class="fa fa-file" aria-hidden="true"></i> Articles
          </router-link>
          <router-link :to="{ name: 'system.list.create-article' }" class="list-group-item waves-effect">
            <i class="fa fa-cog" aria-hidden="true"></i> Settings
          </router-link>
        </div>
      </div>
      <div class="col-md-9">
        <keep-alive>
          <transition name="fade-down" mode="out-in">
            <router-view :key="$route.path"></router-view>
          </transition>
        </keep-alive>
      </div>
    </div>
  </div>
</template>

<script>
import ArticlesListInjector from '../services/injectors/articles-list.injector';

export default {
  name: 'news',
  data () {
    return {
      list: null,
      injector: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  created () {
    this.initializeInjector();
  },
  watch: {
    '$route': 'initializeInjector'
  },
  methods: {
    initializeInjector () {
      var list_id = this.$route.params.list_id;

      this.injector = new ArticlesListInjector(list_id);
      this.injector.getArticlesList(articlesList => {
        this.list = articlesList;
      });
    }
  }
};
</script>
