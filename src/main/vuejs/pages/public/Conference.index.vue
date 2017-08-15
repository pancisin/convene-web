<template>
  <div class="container">
    <ul>
      <li v-for="conf in conferences" :key="conf.id">
        <router-link :to="{ name: 'conference', params: { id: conf.id } }" >
          {{ conf.name }}
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import RootApi from 'api/api';
import Auth from '../../services/auth.js';
export default {
  name: 'conference-index',
  data () {
    return {
      conferences: []
    };
  },
  created () {
    this.getConferences();
  },
  methods: {
    getConferences () {
      RootApi.getConferences(0, 10, Auth.user.authenticated, conferences => {
        this.conferences = conferences.content;
      });
    }
  }
};
</script>