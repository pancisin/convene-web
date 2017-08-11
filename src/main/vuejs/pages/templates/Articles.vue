<template>
  <panel type="table">
    <span slot="title">Articles</span>
    <table class="table">
      <thead>
        <tr>
          <th>
            Title
          </th>
          <th>
            Created
          </th>
        </tr>
      </thead>
  
      <tbody>
        <tr v-for="article in articles" :key="article.id" @contextmenu.prevent="$refs.menu.open($event, article)">
          <td>
            <router-link :to="{ name: 'article', params: { article_id: article.id } }">
              {{ article.title }}
            </router-link>
          </td>
          <td>{{ article.created | moment('L') }}</td>
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
          <li>
            <router-link :to="{ name: 'article', params: { article_id: props.data.id } }">
              Edit
            </router-link>
          </li>
          <li>
            <a @click="deleteArticle(props.data)">
              Delete
            </a>
          </li>
          <li class="separator"></li>
          <li>
            <router-link :to="{ name: 'conference.article.create' }">
              Create article
            </router-link>
          </li>
        </ul>
      </template>
    </context-menu>
  
    <div class="text-center">
      <router-link :to="{ name: 'conference.article.create' }" class="btn btn-primary btn-rounded">
        Create article
      </router-link>
    </div>
  </panel>
</template>

<script>
import ArticleApi from '../../services/api/article.api.js';
export default {
  name: 'articles-template',
  inject: ['provider'],
  data () {
    return {
      articles: []
    };
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  created () {
    this.api.getArticles(articles => {
      this.articles = articles;
    });
  },
  methods: {
    deleteArticle (article) {
      ArticleApi.deleteArticle(article.id, result => {
        this.articles = this.articles.filter(x => {
          x.id !== article.id;
        });
      });
    }
  }
};
</script>