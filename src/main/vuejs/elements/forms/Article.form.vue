<template>
  <form @submit.prevent="submit">
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
      <button class="btn btn-primary btn-rounded" type="submit">Submit</button>
    </div>
  </form>
</template>

<script>
import ArticleApi from 'api/article.api';
import { ImageUpload, TextEditor } from 'elements';

export default {
  name: 'article-form',
  props: {
    article: Object
  },
  inject: ['provider'],
  // data () {
  //   return {
  //     articleCopy: {}
  //   };
  // },
  computed: {
    edit () {
      return this.article.id != null;
    },
    api () {
      return this.provider.api;
    }
  },
  components: {
    ImageUpload,
    TextEditor
  },
  methods: {
    submit () {
      if (this.article.id) {
        ArticleApi.putArticle(this.article.id, this.article, article => {
          this.$success('notification.article.updated', article.title);
          this.$emit('submit', article);
        });
      } else {
        this.api.postArticle(this.article, article => {
          this.$success('notification.article.saved', article.title);
          this.$emit('submit', article);
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
