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
          <label class="control-label">Name</label>
          <input class="form-control required" v-model="bot.name" type="text">
        </div>

        <div class="form-group">
          <label class="control-label">Facebook page</label>
          <input class="form-control required" v-model="bot.fbPageId" type="text">
        </div>

        <button type="submit" class="btn btn-success">Submit</button>
      </form>
    </panel>
    <panel type="table">
      <span slot="title">Recent runs</span>
      <table class="table">
        <thead>
          <tr>
            <th>
              State
            </th>
            <th class="text-center">
              Events imported
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(run, index) in runsPaginator.content" :key="index">
            <td>
              <bot-run-indicator :run="run" />
            </td>
            <td class="text-center">
              {{ run.dataCount }}
            </td>
          </tr>
        </tbody>
      </table>

      <div class="text-center">
        <paginator :paginator="runsPaginator" :fetch="getRuns" />
      </div>
    </panel>
  </div>
</template>

<script>
import EventBotApi from 'api/event-bot.api';
import { BotRunIndicator, Paginator } from 'elements';

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
      runsPaginator: []
    };
  },
  components: {
    BotRunIndicator,
    Paginator
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

        // this.getRuns(0);
      });
    }
  },
  methods: {
    submit () {
      if (this.bot.id) {
        EventBotApi.putEventBot(this.bot.id, this.bot, bot => {
          this.bot = bot;
        });
      } else {
        this.api.postBot(this.bot, bot => {
          this.$router.push(this.api.base_route);
        });
      }
    },
    deleteBot (bot_id) {
      EventBotApi.deleteBot(bot_id, result => {

      });
    },
    getRuns (page) {
      const id = this.$route.params.bot_id;
      EventBotApi.getRuns(id, page, 10, paginator => {
        this.runsPaginator = paginator;
      });
    }
  }
};
</script>
