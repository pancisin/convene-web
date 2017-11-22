<template>
  <div class="row">
    <div class="col-md-10">
      <panel type="table">
        <span slot="title">Bots</span>
        <table class="table" v-loading="loading">
          <thead>
            <tr>
              <th>
                Facebook page
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
                <router-link :to="{ name: 'event-bot', params: { bot_id: bot.id } }">
                  {{ bot.fbPageId }}
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

        <div class="text-center" v-if="editable">
          <router-link to="create-event-bot" class="btn btn-default btn-rounded">Create event bot</router-link>
        </div>
      </panel>
    </div>
    <div class="col-md-2 hidden-sm hidden-xs">
      <img :src="eventBotImage" style="width: 100%; padding: 20px;">
    </div>
  </div>
</template>

<script>
import EventBotApi from 'api/event-bot.api';
import { BotRunIndicator } from 'elements';
import EventBotImg from 'assets/img/event_bot.png';

export default {
  name: 'bots',
  inject: ['provider'],
  props: {
    editable: Boolean
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
    },
    eventBotImage () {
      return EventBotImg;
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

    this.connectWM('/stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe('/user/queue/page.bots', response => {
        let run = JSON.parse(response.body);

        if (run != null) {
          this.bots.forEach((bot, index) => {
            if (bot.id === run.bot.id) {
              this.bots.splice(index, 1, {
                ...bot,
                lastRun: run,
                runsCount: run.state.name === 'SUCCESS' ? bot.runsCount + 1 : bot.runsCount
              });

              this.sortBots();
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
        this.sortBots();
        this.loading = false;
      });
    },
    toggleActive (bot_id) {
      EventBotApi.toggleActive(bot_id, bot => {
        let index = this.bots.map(b => b.id).indexOf(bot_id);
        this.bots.splice(index, 1, bot);
      });
    },
    run (bot_id) {
      this.sendWM(`/app/bot/${bot_id}/run`, JSON.stringify({}));
    },
    sortBots () {
      this.bots.sort((a, b) => {
        if (a.lastRun == null) {
          return 1;
        } else if (b.lastRun == null) {
          return -1;
        } else {
          return a.lastRun.date < b.lastRun.date ? 1 : -1;
        }
      });
    }
  }
};
</script>
