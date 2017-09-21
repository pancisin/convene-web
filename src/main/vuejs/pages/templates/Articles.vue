<template>
  <panel type="table" v-loading="loading">
    <span slot="title">Articles</span>
    <table class="table">
      <thead>
        <tr>
          <th>
          </th>
          <th>
            Title
          </th>
          <th>
            Created
          </th>
          <th class="text-center">
            Status
          </th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="article in articles" :key="article.id" @contextmenu.prevent="$refs.menu.open($event, article)">
          <td>
            <img :src="article.thumbnail.path" v-if="article.thumbnail != null" style="height:50px">
          </td>
          <td>
            <router-link :to="{ name: 'article', params: { article_id: article.id } }">
              {{ article.title }}
            </router-link>
          </td>
          <td>{{ article.created | moment('L') }}</td>
          <td class="text-center">
            <i class="fa fa-check text-success" aria-hidden="true" v-if="article.published"></i>
            <i class="fa fa-eye-slash text-warning" aria-hidden="true" v-else></i>
          </td>
        </tr>
      </tbody>
    </table>

    <context-menu ref="menu">
      <template scope="props">
        <ul>
          <li>
            <router-link :to="{ name: 'event.public', params: { id: props.data.id } }">
              Go to article
            </router-link>
          </li>
          <li class="separator"></li>
          <li v-if="editable">
            <router-link :to="{ name: 'article', params: { article_id: props.data.id } }">
              Edit
            </router-link>
          </li>
          <li v-if="editable">
            <a @click="deleteArticle(props.data)">
              Delete
            </a>
          </li>
          <li class="separator"></li>
          <li>
            <router-link :to="{ name: 'conference.place.create' }">
              Create article
            </router-link>
          </li>
        </ul>
      </template>
    </context-menu>

    <div class="text-center" v-if="editable">
      <router-link :to="{ name: 'conference.article.create' }" class="btn btn-primary btn-rounded">
        Create article
      </router-link>
    </div>
  </panel>
</template>

<script>
import ArticleApi from 'api/article.api';
export default {
  name: 'articles-template',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
  data () {
    return {
      articles: [],
      loading: false
    };
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  watch: {
    '$route': 'getArticles'
  },
  created () {
    this.getArticles();
  },
  methods: {
    getArticles () {
      this.loading = true;
      this.api.getArticles(articles => {
        this.articles = articles;
        this.loading = false;
      });
    },
    deleteArticle (article) {
      this.$prompt('notification.article.delete_prompt', () => {
        ArticleApi.deleteArticle(article.id, result => {
          this.articles = this.articles.filter(x => {
            x.id !== article.id;
          });
        });
      });
    }
  }
};
</script>