<template>
  <div v-loading="loading">
    <vue-table :func="tableRender" :data="paginator.content" :contextmenu="contextmenu" />

    <div class="text-center">
      <paginator :paginator="paginator" :fetch="getBots" />
    </div>

    <modal :show.sync="displayBotModal" v-if="selectedBot">
      <span slot="title">Edit bot</span>

      <div slot="body">
        <article-bot-form :bot="selectedBot" @submit="updateBot" />
      </div>
    </modal>

    <div class="text-center">
      <a class="btn btn-default" @click="createBot">Create bot</a>
    </div>
  </div>
</template>

<script>
import {
  BotRunIndicator,
  Paginator,
  VueTable
} from 'elements';
import ArtcleBotApi from 'api/article-bot.api';
import { ArticleBotForm } from 'elements/forms';

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
      paginator: {},
      loading: false,
      subscription: null,
      displayBotModal: false,
      selectedBot: {}
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
    BotRunIndicator,
    Paginator,
    VueTable,
    ArticleBotForm
  },
  created () {
    this.connectWM('/stomp').then(frame => {
      this.subscription = this.$stompClient.subscribe(
        '/user/queue/list.bots',
        response => {
          let run = JSON.parse(response.body);

          if (run != null) {
            this.paginator.content.forEach((bot, index) => {
              if (bot.id === run.articleBot.id) {
                this.paginator.content.splice(index, 1, {
                  ...bot,
                  lastRun: run,
                  runsCount:
                    run.state.name === 'SUCCESS'
                      ? bot.runsCount + 1
                      : bot.runsCount
                });
              }
            });
          }
        }
      );
    });
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  methods: {
    getBots (page) {
      this.loading = true;
      this.api.getBots(page, 10, paginator => {
        this.paginator = paginator;
        this.loading = false;
      });
    },
    toggleActive (bot_id) {
      ArtcleBotApi.toggleActive(bot_id, bot => this.updateBot(bot));
    },
    deleteBot (bot_id) {
      this.$prompt('notification.delete_prompt', bot_id, () => {
        ArtcleBotApi.deleteArticleBot(bot_id, result => {
          this.paginator.content = this.paginator.content.filter(bot => bot.id !== bot_id);
        });
      });
    },
    createBot () {
      this.selectedBot = {};
      this.displayBotModal = true;
    },
    editBot (bot) {
      this.selectedBot = bot;
      this.displayBotModal = true;
    },
    updateBot (bot) {
      const index = this.paginator.content.findIndex(b => b.id === bot.id);
      if (index !== -1) {
        this.paginator.content.splice(index, 1, bot);
      } else {
        this.paginator.content.push(bot);
      }

      this.displayBotModal = false;
    },
    run (bot_id) {
      this.sendWM(`/app/article-bot/${bot_id}/run`, JSON.stringify({}));
    },
    tableRender (bot) {
      return {
        name: {
          el: 'a',
          content: bot.name,
          onClick: () => this.editBot(bot)
        },
        runs: bot.runsCount,
        last_run: {
          el: BotRunIndicator,
          props: {
            run: bot.lastRun || {}
          }
        },
        active: bot.active
      };
    },
    contextmenu (item) {
      return [
        item('Run', bot => this.run(bot.id)),
        item('Toggle active', bot => this.toggleActive(bot.id)),
        item('Edit', bot => this.editBot(bot)),
        item('Delete', bot => this.deleteBot(bot.id)),
        item('Create bot', this.createBot)
      ];
    }
  }
};
</script>
