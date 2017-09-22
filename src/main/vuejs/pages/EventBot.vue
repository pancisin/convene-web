<template>
  <div v-if="bot != null">
    <div class="page-title-box">
      <h4 class="page-title" v-text="bot.id"></h4>
    </div>
    <panel type="primary">
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

      <hr />

      <ul>
        <li v-for="(run, index) in runs" :key="index">
          <i class="fa fa-check text-success" v-if="run.success"></i>
          <i class="fa fa-times text-danger" v-else></i> {{ run.date | moment('L LT') }} - {{ run.eventsCount }}
        </li>
      </ul>
    </panel>
  </div>
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
      bot: {},
      runs: []
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

        this.getRuns();
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
    },
    getRuns () {
      EventBotApi.getRuns(this.bot.id, runs => {
        this.runs = runs;
      });
    }
  }
};
</script>

<style>

</style>
