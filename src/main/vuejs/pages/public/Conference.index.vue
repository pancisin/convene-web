<template>
  <div class="container">
    <ul>
      <li v-for="conf in conferences">
        {{ conf.name }}
      </li>
    </ul>
  </div>
</template>

<script>
import RootApi from '../../services/api/api.js'
import Auth from '../../services/auth.js'
export default {
  name: 'conference-index',
  data() {
    return {
      conferences: [],
    }
  },
  created() {
    this.getConferences();
  },
  methods: {
    getConferences() {
      RootApi.getConferences(0, 10, Auth.user.authenticated, conferences => {
        this.conferences = conferences.content;
      })
    }
  }
}
</script>