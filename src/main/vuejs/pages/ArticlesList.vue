<template>
  <div v-if="list != null">
    <h3 v-text="list.name"
      class="page-title"></h3>

    <router-tab-navigation :menu="menu">
      <keep-alive>
        <transition name="fade"
          mode="out-in">
          <router-view 
            :key="$route.path" 
            :list="list" 
            @update="updateList">
          </router-view>
        </transition>
      </keep-alive>
    </router-tab-navigation>
  </div>
</template>

<script>
import ArticlesListApi from 'api/articles-list.api';
import InjectorGenerator from '../services/InjectorGenerator';
import { RouterTabNavigation } from 'elements';
import { ArticlesListMenu } from '../services/maps/menus';

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
  computed: {
    menu () {
      return ArticlesListMenu;
    }
  },
  components: {
    RouterTabNavigation
  },
  created () {
    this.initializeInjector();
  },
  watch: {
    $route: 'initializeInjector'
  },
  methods: {
    initializeInjector () {
      var list_id = this.$route.params.list_id;

      this.injector = InjectorGenerator.generate(ArticlesListApi, list_id);
      this.injector.getArticlesList(articlesList => {
        this.list = articlesList;
      });
    },
    updateList (list) {
      this.list = list;
    }
  }
};
</script>
