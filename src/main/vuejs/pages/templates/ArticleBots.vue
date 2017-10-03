<template>
  <panel type="table" v-loading="loading">
    <span slot="title">
      Article bots
    </span>

    <table class="table">
      <thead>
        <tr>
          <th>
            UUID
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(bot, index) in bots" :key="bot.id">
          <td>
            <router-link :to="{ name: 'admin.article-bot', params: { article_bot_id: bot.id } }">
              {{ bot.id }}
            </router-link>
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
export default {
  name: 'article-bots',
  inject: ['provider'],
  data () {
    return {
      bots: [],
      loading: false
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  watch: {
    'api': 'getBots'
  },
  created () {
    this.getBots();
  },
  methods: {
    getBots () {
      this.loading = true;
      this.api.getBots(bots => {
        this.bots = bots;
        this.loading = false;
      });
    }
  }
};
</script>
