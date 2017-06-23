<template>
  <panel type="table">
    <span slot="title">Articles</span>
    <table class="table">
      <thead>
        <tr>
          <th>
            Title
          </th>
          <th>
            Created
          </th>
        </tr>
      </thead>
  
      <tbody>
        <tr v-for="article in articles" :key="article.id">
          <td>
            <router-link :to="{ name: 'article', params: { article_id: article.id } }">
              {{ article.title }}
            </router-link>
          </td>
          <td>{{ article.created | moment('L') }}</td>
        </tr>
      </tbody>
    </table>
  
    <div class="text-center">
      <router-link :to="{ name: 'conference.article.create' }" class="btn btn-primary btn-rounded">
        Create article
      </router-link>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'articles-template',
  inject: ['api'],
  data() {
    return {
      articles: [],
    }
  },
  created() {
    this.api.getArticles(articles => {
      this.articles = articles;
    })
  },
}
</script>