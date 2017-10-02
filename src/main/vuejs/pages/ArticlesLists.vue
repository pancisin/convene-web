<template>
  <div>
    <panel type="table">
      <span slot="title">News lists</span>
      <table class="table">
        <thead>
          <tr>
            <th>
              Name
            </th>
            <th>
              UUID
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="list in articlesLists" :key="list.id">
            <td>
              <router-link :to="{ name: 'system.list', params: { list_id: list.id } }">
                {{ list.name }}
              </router-link>
            </td>
            <td>
              {{ list.id }}
            </td>
          </tr>
        </tbody>
      </table>
    </panel>

    <panel>
      <span slot="title">Create list of articles</span>
      <form @submit.prevent="submit">
        <div class="form-group">
          <label for="articlesList.name">Name</label>
          <input type="text" v-model="list.name" class="form-control">
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
      </form>
    </panel>
  </div>
</template>

<script>
import ArticlesListApi from 'api/articles-list.api';
export default {
  name: 'articles-lists',
  data () {
    return {
      articlesLists: [],
      list: {}
    };
  },
  created () {
    ArticlesListApi.getArticlesLists(articlesLists => {
      this.articlesLists = articlesLists;
    });
  },
  methods: {
    submit () {
      ArticlesListApi.postArticlesList(this.list, (articlesList) => {
        this.$router.push({
          name: 'system.list',
          params: {
            list_id: articlesList.id
          }
        });

        this.articlesLists.push(articlesList);
      });
    }
  }
};
</script>