<template>
  <panel type="table" v-loading="loading">
    <span slot="title">Articles</span>
    <table class="table articles-table">
      <thead>
        <tr>
          <th>
            #
          </th>
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
        <tr v-for="(article, index) in articlesPaginator.content" :key="article.id" @contextmenu.prevent="$refs.menu.open($event, article)">
          <td>
            {{ articlesPaginator.number * articlesPaginator.size + index + 1 }}
          </td>
          <td>
            <img :src="article.thumbnail.path" v-if="article.thumbnail != null" style="height:50px">
          </td>
          <td>
            <router-link :to="{ name: 'article', params: { article_id: article.id } }">
              {{ article.title }}
            </router-link>
          </td>
          <td>{{ article.created | luxon('F') }}</td>
          <td class="text-center">
            <i class="fa fa-check text-success" aria-hidden="true" v-if="article.published"></i>
            <i class="fa fa-eye-slash text-warning" aria-hidden="true" v-else></i>
          </td>
        </tr>
      </tbody>
    </table>

    <context-menu ref="menu">
      <template slot-scope="props">
        <ul>
          <li>
            <router-link :to="{ name: 'article.public', params: { article_id: props.data.id } }">
              Go to article
            </router-link>
          </li>
          <li class="separator"></li>
          <li v-if="editable">
            <a @click="togglePublished(props.data.id)">
              Toggle published
            </a>
          </li>
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
            <router-link to="create-article">
              Create article
            </router-link>
          </li>
        </ul>
      </template>
    </context-menu>

    <div class="text-center">
      <paginator :paginator="articlesPaginator" :fetch="getArticles" />
    </div>

    <div class="text-center" v-if="editable && insertable">
      <router-link to="create-article" class="btn btn-primary btn-rounded">
        Create article
      </router-link>
    </div>
  </panel>
</template>

<script>
import ArticleApi from 'api/article.api';
import { Paginator } from 'elements';

export default {
  name: 'articles-template',
  inject: ['provider'],
  props: {
    editable: Boolean,
    insertable: {
      type: Boolean,
      default: true
    },
    paginated: Boolean
  },
  components: {
    Paginator
  },
  data () {
    return {
      loading: false,
      articlesPaginator: {}
    };
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  watch: {
    'api': 'initialize'
  },
  methods: {
    initialize () {
      this.getArticles(0);
    },
    getArticles (page) {
      this.loading = true;
      this.api.getArticles(page, 8, paginator => {
        this.articlesPaginator = paginator;
        this.loading = false;
      });
    },
    togglePublished (article_id) {
      ArticleApi.togglePublished(article_id, article => {
        this.articlesPaginator.content.forEach((a, index) => {
          if (a.id === article_id) {
            this.articlesPaginator.content.splice(index, 1, article);
          }
        });
      });
    },
    deleteArticle (article) {
      this.$prompt('notification.article.delete_prompt', () => {
        ArticleApi.deleteArticle(article.id, result => {
          this.articlesPaginator.content = this.articles.filter(x => {
            x.id !== article.id;
          });
        });
      });
    }
  }
};
</script>

<style lang="less">
.articles-table {
  & > tbody > tr > td {
    vertical-align: middle;
  }
}
</style>