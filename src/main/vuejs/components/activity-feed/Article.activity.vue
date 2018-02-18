<template>
  <div class="article-activity">
    <img :src="obj.thumbnail.path" style="width:100%">

    <div class="article-activity-content">
      <h4>
        <router-link :to="{ name: 'article.public', params: { article_id: obj.id }}">
          {{ obj.title }}
        </router-link>
      </h4>
      <p v-strip="obj.content.substring(0, 200)">...</p>
      <div class="text-right m-t-20 clearfix">
        <button type="button" class="btn btn-link pull-right">Read more</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'article-activity-feed',
  props: {
    obj: Object
  }
};
</script>

<style lang="less">
.article-activity {
  .article-activity-content {
    padding: 20px;
  }
}
</style>
