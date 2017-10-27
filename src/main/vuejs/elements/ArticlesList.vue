<template>
  <div class="articles-list"
    v-loading="loading">

    <div v-if="hasHeadline" class="articles-list-headline" :style="{ 'background-image': `url(${headline.thumbnail.path})` }">
      <div class="title">
        <router-link :to="{ name: 'article.public', params: { article_id: headline.id } }">
          <h3>
            {{ headline.title }}
          </h3>
        </router-link>
        
        <p v-if="headline.content != null"
          v-strip="headline.content.substring(0, 200)"></p>

        <router-link :to="{ name: 'article.public', params: { article_id: headline.id } }"
          class="btn btn-link btn-xs pull-right">
          Read more
          <i class="fa fa-angle-right"></i>
        </router-link>
      </div>
    </div>

    <transition-group name="fade">
      <div class="articles-list-item clearfix"
        v-for="article in articlesList"
        :key="article.id">
        <div class="article-image-container"
          v-if="article.thumbnail != null">
          <img :src="article.thumbnail.path">
        </div>

        <router-link :to="{ name: 'article.public', params: { article_id: article.id } }">
          <h4 v-text="article.title"></h4>
        </router-link>

        <p v-if="article.content != null"
          v-strip="article.content.substring(0, 200)"></p>

        <router-link :to="{ name: 'article.public', params: { article_id: article.id } }"
          class="btn btn-link btn-xs pull-right">
          Read more
          <i class="fa fa-angle-right"></i>
        </router-link>
        </a>
      </div>
    </transition-group>
  </div>
</template>

<script>
export default {
  name: 'articles-list',
  // inject: ['api'],
  props: {
    articles: {
      type: Array,
      default: null
    },
    hasHeadline: Boolean
  },
  data () {
    return {
      articlesList: [],
      headline: null,
      loading: false
    };
  },
  watch: {
    'articles': 'initialize'
  },
  created () {
    this.initialize();
  },
  methods: {
    initialize () {
      if (this.articles == null) {
        this.loading = true;
        this.api.getArticles(articles => {
          this.articlesList = articles;
          this.loading = false;
        });
      } else {
        this.articlesList = [ ...this.articles ];
        this.loading = false;
      }

      if (this.hasHeadline) {
        this.headline = this.articlesList.shift();
      }
    }
  }
};
</script>

<style lang="less">
@thumbnail_percentage: 20%;
@import (reference) '~less/variables.less';

.articles-list {
  .articles-list-headline {
    background-size: cover;
    height: 300px;
    margin-bottom: 20px;
    position: relative;

    overflow: hidden;
    border-radius: 5px;

    .title {
      position: absolute;
      background: #fff;
      padding: 15px;
      background: rgba(30, 30, 30, 0.75);
      color: #fff;
      bottom: 0;

      p {
        font-size: 13px;
      }

      h3 {
        color: @color-pink;
        margin-top: 0px;
      }

      a {
        color: #fff;
      }
    }
  }

  .articles-list-item {
    h4 {
      margin-top: 0;
    }

    .article-image-container {
      // border-radius: 100%;
      width: @thumbnail_percentage;
      float: left;
      margin-right: 20px;
      padding-bottom: @thumbnail_percentage;
      overflow: hidden;
      position: relative;
      border-radius: 5px;

      img {
        position: absolute;
        height: 100%;
        min-width: 100%;
      }
    }

    &~.articles-list-item {
      border-top: 1px solid #eee;
      margin-top: 10px;
      padding-top: 10px;
    }

    p {
      font-size: 13px;
    }

    a {
      color: @color-inverse;
    }
  }
}

@media screen and (max-width: 460px) {
  .articles-list {
    .articles-list-item {
      .article-image-container {
        border-radius: 0;
        float: none;
        width: 100%;
        max-width: initial;
        height: 100px;
        margin-bottom: 10px;

        img {
          height: auto;
          top: 50%;
          transform: translateY(-50%);
        }
      }
    }
  }
}
</style>
