<template>
  <panel type="table" v-loading="loading">
    <span slot="title">
      Article bots
    </span>

    <table class="table">
      <thead>
        <tr>
          <th>
            Name
          </th>
          <th class="text-center">
            Runs
          </th>
          <th>
            Last run
          </th>
          <th class="text-center" v-if="editable">
            Action
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(bot, index) in bots" :key="bot.id">
          <td>
            <router-link :to="{ name: 'admin.article-bot', params: { article_bot_id: bot.id } }">
              {{ bot.name }}
            </router-link>
          </td>
          <td class="text-center">
            {{ bot.runsCount }}
          </td>
          <td>
            <bot-run-indicator v-if="bot.lastRun != null" :run="bot.lastRun" />
          </td>
          <td class="text-center" v-if="editable && (bot.lastRun == null || bot.lastRun.state.name !== 'RUNNING')">
            <a class="btn btn-default btn-xs" @click="toggleActive(bot.id)" :class="{ 'btn-danger' : bot.active }">{{ bot.active ? 'Dectivate' : 'Activate' }}</a>
            <a class="btn btn-warning btn-xs" @click="run(bot.id)">Run</a>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="text-center">
      <router-link :to="{ name: 'system.list.create-bot' }" class="btn btn-primary btn-rounded">Create bot</router-link>
    </div>
  </panel>
</template>

<script>
import { BotRunIndicator } from 'elements';
export default {
  name: 'article-bots',
  inject: ['provider'],
  props: {
    editable: {
      type: Boolean,
      default () {
        return true;
      }
    }
  },
  data () {
    return {
      bots: [],
      loading: false,
      subscription: null
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  components: {
    BotRunIndicator
  },
  watch: {
    'api': 'getBots'
  },
  created () {
    this.getBots();

    this.connectWM('stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/user/queue/list.bots', response => {
        let run = JSON.parse(response.body);

        if (run != null) {
          this.bots.forEach((bot, index) => {
            if (bot.id === run.bot.id) {
              this.bots.splice(index, 1, {
                ...bot,
                lastRun: run,
                runsCount: run.state.name === 'SUCCESS' ? bot.runsCount + 1 : bot.runsCount
              });
            }
          });
        }
      });
    });
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    getBots () {
      this.loading = true;
      this.api.getBots(bots => {
        this.bots = bots;
        this.loading = false;
      });
    },
    toggleActive (bot_id) {
      // todo
    },
    run (bot_id) {
      this.sendWM(`/app/article-bot/${bot_id}/run`, JSON.stringify({}));
    }
  }
};
</script>
