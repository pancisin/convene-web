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
            <router-link :to="{ name: 'article', params: { id: article.id } }">
              {{ article.title }}
            </router-link>
          </td>
          <td>{{ article.created | moment('L') }}</td>
        </tr>
      </tbody>
    </table>
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