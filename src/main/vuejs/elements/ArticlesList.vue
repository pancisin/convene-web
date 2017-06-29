<template>
  <div class="articles-list" v-loading="loading">
    <div class="articles-list-item clearfix" v-for="article in articles" :key="article.id">
      <div class="article-image-container" v-if="article.bannerUrl != null">
        <img :src="article.bannerUrl">
      </div>
  
      <h4 v-text="article.title"></h4>
      <!--<small>{{ article.created | moment('L') }}</small>-->
      <p v-if="article.content != null" v-strip="article.content.substring(0, 250)"></p>

      <a class="btn btn-link btn-xs pull-right">Read more <i class="fa fa-angle-right"></i></a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'articles-list',
  inject: ['api'],
  data() {
    return {
      articles: [],
      loading: false,
    }
  },
  created() {
    this.loading = true;
    this.api.getArticles(articles => {
      this.articles = articles;
      this.loading = false;
    })
  },
  methods: {

  }
}
</script>

<style lang="less">
.articles-list {
  .articles-list-item {
    .article-image-container {
      width: 30%;
      max-width: 200px;
      float: left;
      margin-right: 20px;

      img {
        width: 100%;
        margin-bottom: 10px;
      }
    }

    &~.articles-list-item {
      // border-top: 1px solid #eee;
      margin-top: 20px;
    }
  }
}

@media screen and (max-width: 460px) {
  .articles-list {
    .articles-list-item {
      .article-image-container {
        float: none;
        width: 100%;
        max-width: initial;
      }
    }
  }
}
</style>
