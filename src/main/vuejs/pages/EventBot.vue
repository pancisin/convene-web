<template>
  <panel>
    <span slot="title">
      Event bot
    </span>

    <form class="form" @submit.prevent="submit">
      <div class="form-group">
        <label class="control-label">Facebook page</label>
        <input class="form-control required" v-model="bot.fbPageId" type="text">
      </div>

      <button type="submit" class="btn btn-success">Submit</button>
    </form>
  </panel>
</template>

<script>
import EventBotApi from 'api/event-bot.api';
export default {
  name: 'event-bot',
  inject: ['provider'],
  props: {
    edit: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      bot: {}
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  created () {
    if (this.edit) {
      EventBotApi.getEventBot(this.$route.params.bot_id, bot => {
        this.bot = bot;
      });
    }
  },
  methods: {
    submit () {
      this.api.postBot(this.bot, bot => {
        console.log('success');
      });
    },
    deleteBot (bot_id) {
      EventBotApi.deleteBot(bot_id, result => {

      });
    }
  }
};
</script>

<style>

</style>
