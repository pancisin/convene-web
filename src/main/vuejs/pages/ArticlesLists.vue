<template>
  <div>
    <panel type="table" v-loading="loading">
      <span slot="title">News lists</span>
      <table class="table">
        <thead>
          <tr>
            <th>
              Name
            </th>
            <th>
              Tags
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
              <span v-for="(tag, index) in list.tags" :key="index">
                {{ tag }},
              </span>
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
import RootApi from 'api/api';
export default {
  name: 'articles-lists',
  data () {
    return {
      articlesLists: [],
      list: {},
      loading: false
    };
  },
  created () {
    this.loading = true;
    RootApi.getArticlesLists(articlesLists => {
      this.articlesLists = articlesLists;
      this.loading = false;
    });
  },
  methods: {
    submit () {
      RootApi.postArticlesList(this.list, (articlesList) => {
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