<template>
  <div v-if="list != null">
    <!-- <panel>
            <span slot="title">Create list of articles</span>
            <form @submit.prevent="submit">
              <div class="form-group">
                <label for="articlesList.name">Name</label>
                <input type="text" v-model="articlesList.name" class="form-control">
              </div>

              <button type="submit" class="btn btn-primary">Submit</button>
            </form>
          </panel> -->

    <div class="row">
      <div class="col-md-3">

      </div>
      <div class="col-md-9">
        <transition name="slide-left" mode="out-in">
          <router-view></router-view>
        </transition>
        <div class="text-center" v-if="injector != null">
          <router-link :to="{ name: 'system.news.create-article' }" class="btn btn-default btn-rounded">
            Create article
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ArticlesListApi from 'api/articles-list.api';
import ArticlesListInjector from '../services/injectors/articles-list.injector';

export default {
  name: 'news',
  data () {
    return {
      list: null,
      articles: [],
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
    submit () {
      ArticlesListApi.postArticlesList(this.articlesList, (articlesList) => {
        this.articlesLists.push(articlesList);
      });
    },
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
