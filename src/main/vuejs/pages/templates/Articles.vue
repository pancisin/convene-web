<template>
  <panel type="table" v-loading="loading">
    <span slot="title">Articles</span>

    <vue-table class="articles-table" :func="tableRender" :data="articlesPaginator.content" :contextmenu="contextMenuRender" />
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
import { Paginator, VueTable } from 'elements';
import { DateTime } from 'luxon';

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
    Paginator,
    VueTable
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
    tableRender (article, index) {
      return {
        id: index + 1,
        img () {
          if (article.thumbnail) {
            return {
              el: 'img',
              attrs: {
                src: article.thumbnail.path
              },
              style: {
                height: '50px'
              }
            };
          }
        },
        title: {
          el: 'a',
          onClick: () => {
            this.$router.push({ name: 'article', params: { article_id: article.id } });
          },
          content: article.title
        },
        created: DateTime.fromMillis(article.created).toFormat('F'),
        status: {
          el: 'i',
          colClass: 'text-center',
          class: `fa ${ article.published ? 'fa-check text-success' : 'fa-eye-slash text-warning' }`
        }
      };
    },
    contextMenuRender (item) {
      return [
        item('Go to article', article => this.$router.push({ name: 'article.public', params: { article_id: article.id } })),
        item('Toggle published', article => this.togglePublished(article.id)),
        item('Edit', article => this.$router.push({ name: 'article', params: { article_id: article.id } })),
        item('Delete', article => this.deleteArticle(article)),
        item('Create article', article => this.$router.push('create-article'))
      ];
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
      this.$prompt('notification.delete_prompt', article.title, () => {
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