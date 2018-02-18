<template>
  <div class="container">
    <div class="col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3" v-loading="loading">
      <div class="article-banner" v-if="article.thumbnail != null">
        <light-box :image="article.thumbnail.path">
          <vue-image :src="article.thumbnail.path" placeholder />
        </light-box>
      </div>
  
      <h3 v-if="article != null">
        {{article.title}}
      </h3>
  
      <div class="clearfix">
        <small class="text-muted" v-if="article.author != null"> 
          {{ article.author.displayName }}
        </small>

        <small class="pull-right text-muted">
          {{ article.created | luxon('fff') }}
        </small>
      </div>
      <hr />
  
      <div v-html="article.content" class="custom-content"></div>
    </div>
  </div>
</template>

<script>
import { LightBox, VueImage } from 'elements';
import ArticleApi from 'api/article.api';

export default {
  name: 'article',
  data () {
    return {
      article: {},
      loading: false
    };
  },
  components: {
    LightBox,
    VueImage
  },
  created () {
    this.loading = true;
    ArticleApi.getArticle(this.$route.params.article_id, article => {
      this.article = article;
      this.loading = false;
    });
  }
};
</script>

<style lang="less">
.article-banner {
  max-height: 200px;
  overflow: hidden;

  img {
    width: 100%;
  }
}
</style>
