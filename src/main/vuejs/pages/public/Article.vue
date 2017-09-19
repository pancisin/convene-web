<template>
  <div class="container">
    <div class="col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
      <div class="article-banner" v-if="article.thumbnail != null">
        <img :src="article.thumbnail.path">
      </div>
  
      <h3 v-if="article != null">
        {{article.title}}
      </h3>
  
      <div class="clearfix">
        <small class="text-muted" v-if="article.author != null"> 
          {{ article.author.displayName }}
        </small>

        <small class="pull-right text-muted">
          {{ article.created | moment('LLLL') }}
        </small>
      </div>
      <hr />
  
      <div v-html="article.content"></div>
    </div>
  </div>
</template>

<script>
import ArticleApi from 'api/article.api';

export default {
  name: 'article',
  data () {
    return {
      article: {}
    };
  },
  created () {
    ArticleApi.getArticle(this.$route.params.article_id, article => {
      this.article = article;
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
