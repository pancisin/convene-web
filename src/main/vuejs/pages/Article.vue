<template>
  <div>
    <div class="page-title-box">
      <h4 class="page-title" v-text="article.title"></h4>
    </div>

    <panel>
      <div class="row">
        <div :class="{ 'col-md-8' : edit }">
          <div class="form-group">
            <label class="control-label">Title</label>
            <input type="text" class="form-control" v-model="article.title">
          </div>

          <div class="form-group">
            <label class="control-label">Content</label>
            <text-editor v-model="article.content"></text-editor>
          </div>
        </div>

        <div class="col-md-4" v-if="edit">
          <image-upload v-model="article.thumbnailData" :media="article.thumbnail"></image-upload>
        </div>
      </div>

      <div class="text-center m-t-20">
        <span v-if="edit">
          <a v-if="article.published" class="btn btn-danger btn-rounded" @click="togglePublished">Unpublish</a>
          <a v-else class="btn btn-success btn-rounded" @click="togglePublished">Publish</a>
        </span>
        <a class="btn btn-primary btn-rounded" @click="submit">Submit</a>
      </div>
    </panel>
  </div>
</template>

<script>
import ArticleApi from 'api/article.api';
import { ImageUpload, TextEditor } from 'elements';

export default {
  name: 'article',
  inject: ['provider'],
  data () {
    return {
      article: {},
      edit: false
    };
  },
  components: {
    ImageUpload, TextEditor
  },
  created () {
    const route = this.$route.params.article_id;

    if (route != null) {
      ArticleApi.getArticle(route, article => {
        this.article = article;
        this.edit = article.id != null;
      });
    }
  },
  computed: {
    api () {
      return this.provider.api;
    }
  },
  methods: {
    submit () {
      if (this.edit) {
        ArticleApi.putArticle(this.article.id, this.article, article => {
          this.article = article;
          this.$success('notification.article.updated', article.title);
        });
      } else {
        this.api.postArticle(this.article, article => {
          this.$router.push({ name: 'article', params: { article_id: article.id } });
          this.$success('notification.article.saved', article.title);
        });
      }
    },
    togglePublished () {
      ArticleApi.togglePublished(this.article.id, article => {
        this.article.published = article.published;
      });
    }
  }
};
</script>