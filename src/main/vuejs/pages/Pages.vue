<template>
  <panel type="table">
    <span slot="title">Pages</span>

    <table class="table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Followers</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(page, index) in pages" :key="index">
          <td>
            <router-link :to="{ name: 'page.settings', params: { id: page.id }}">
              {{ page.name }}
            </router-link>
          </td>
          <td>{{ page.followersCount }}</td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import { PagesList } from 'elements';
import { mapGetters } from 'vuex';

export default {
  name: 'pages',
  data () {
    return {

    };
  },
  components: {
    PagesList
  },
  computed: {
    ...mapGetters(['pages'])
  }
};
</script>

<style>

</style>
