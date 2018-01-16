<template>
  <div v-if="bot != null">
    <div class="page-title-box">
      <h4 class="page-title" v-text="bot.name"></h4>
    </div>
    <panel type="primary">
      <span slot="title">
        Article bot
      </span>

      <form class="form" @submit.prevent="submit">
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">Name</label>
              <input class="form-control required" v-model="bot.name" type="text">
            </div>
            <!-- <div class="form-group">
              <label class="control-label">Parser</label>
              <textarea class="form-control required" v-model="bot.parser" rows="5"></textarea>
            </div> -->
            <div class="form-group">
              <label class="control-label">Source type</label>
              <input class="form-control required" v-model="bot.sourceType" type="text">
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">Source url</label>
              <textarea class="form-control required" v-model="bot.sourceUrl" rows="5"></textarea>
            </div>
          </div>
        </div>

        <div class="form-group">
          <label class="control-label">Parser</label>
          <data-parser-editor v-model="bot.parser" />
        </div>

        <button type="submit" class="btn btn-primary" v-if="touched">Save</button>
      </form>
    </panel>

    <panel type="table" v-if="paginator.content && paginator.content.length > 0">
      <span slot="title">Recent runs</span>
      <table class="table">
        <thead>
          <tr>
            <th>
              State
            </th>
            <th class="text-center">
              Articles imported
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(run, index) in paginator.content" :key="index">
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
        <paginator :fetch="getRuns" :paginator="paginator" />
      </div>
    </panel>
  </div>
</template>

<script>
import ArticleBotApi from 'api/article-bot.api';
import { BotRunIndicator, Paginator, DataParserEditor } from 'elements';
import { calculateHash } from '../services/helpers';

export default {
  name: 'article-bot',
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
      originalBot: null,
      parsers: [],
      paginator: {}
    };
  },
  components: {
    BotRunIndicator,
    Paginator,
    DataParserEditor
  },
  watch: {
    parsers: {
      handler (newVal) {
        let parser = {};

        newVal.forEach(field => {
          parser = {
            ...parser,
            [field.name]: field.value
          };
        });

        this.bot.parser = parser;
      },
      deep: true
    }
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    },
    touched () {
      return this.originalBot !== calculateHash(JSON.stringify(this.bot));
    }
  },
  created () {
    if (this.edit) {
      ArticleBotApi.getArticleBot(this.$route.params.article_bot_id, bot => {
        this.initializeBot(bot);
        this.getRuns(0);
      });
    }
  },
  methods: {
    submit () {
      if (!this.edit) {
        this.api.postBot(this.bot, bot => {
          this.$router.push({
            name: 'admin.article-bot',
            params: {
              article_bot_id: bot.id
            }
          });
        });
      } else {
        ArticleBotApi.putArticleBot(this.bot.id, this.bot, bot => {
          this.initializeBot(bot);
        });
      }
    },
    initializeBot (bot) {
      this.bot = bot;
      this.originalBot = calculateHash(JSON.stringify(bot));
    },
    deleteBot (bot_id) {
      ArticleBotApi.deleteBot(bot_id, result => {

      });
    },
    getRuns (page) {
      ArticleBotApi.getRuns(this.bot.id, page, 10, paginator => {
        this.paginator = paginator;
      });
    }
  }
};
</script>
