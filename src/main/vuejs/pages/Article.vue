<template>
  <div>
    <div class="page-title-box">
      <h4 class="page-title" v-text="article.title"></h4>
    </div>
  
    <panel>
      <div class="row">
        <div class="col-md-8">
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
          <image-upload v-model="article.bannerUrl"></image-upload>
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
import ArticleApi from '../services/api/article.api.js'
import ImageUpload from '../elements/ImageUpload.vue'
import TextEditor from '../elements/TextEditor.vue'

export default {
  name: 'article',
  inject: ['api'],
  data() {
    return {
      article: {},
      edit: false,
    }
  },
  components: {
    ImageUpload, TextEditor
  },
  created() {
    ArticleApi.getArticle(this.$route.params.article_id, article => {
      this.article = article;
      this.edit = article.id != null;
    })
  },
  methods: {
    submit() {
      if (this.edit) {
        ArticleApi.putArticle(this.article.id, this.article, article => {
          this.article = article;
          this.$success('Success', `Article ${article.title} has been save !`);
        })
      } else {
        this.api.postArticle(this.article, article => {
          this.$router.push({ name: 'article', params: { article_id: article.id } })
          this.$success('Success', `Article ${article.title} has been created !`);
        })
      }
    },
    togglePublished() {
      ArticleApi.togglePublished(this.article.id, article => {
        this.article.published = article.published;
      })
    }
  }
}
</script>