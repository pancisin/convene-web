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
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label class="control-label">Source url</label>
              <textarea class="form-control required" v-model="bot.sourceUrl" rows="5"></textarea>
            </div>

            <div class="form-group">
              <label class="control-label">Source type</label>
              <input class="form-control required" v-model="bot.sourceType" type="text">
            </div>
          </div>
        </div>

        <hr>
        <h4>Parsers</h4>

        <transition-group name="fade-up">
          <div class="form-group" v-for="(parser, index) in parsers" :key="index">
            <div class="input-group">
              <input class="form-control required" v-model="parser.name" type="text">
              <span class="input-group-addon">
                <i class="fa fa-code"></i>
              </span>
              <input class="form-control required" v-model="parser.value" type="text">
              <span class="input-group-btn">
                <button class="btn btn-danger" type="button" @click="deleteParser(parser.name)">
                  <i class="fa fa-trash-o"></i>
                </button>
              </span>
            </div>
          </div>
        </transition-group>

        <div class="text-center">
          <a class="btn btn-xs btn-primary btn-rounded" @click="addParser">
            <i class="fa fa-plus"></i>
          </a>
        </div>

        <button type="submit" class="btn btn-primary" v-if="touched">Save</button>
      </form>
    </panel>

    <panel type="table" v-if="runs.length > 0">
      <span slot="title">Recent runs</span>
      <table class="table">
        <thead>
          <tr>
            <th>
              State
            </th>
            <th>
              Articles imported
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(run, index) in runs" :key="index">
            <td>
              <bot-run-indicator :run="run" />
            </td>
            <td>
              {{ run.articlesCount }}
            </td>
          </tr>
        </tbody>
      </table>
    </panel>
  </div>
</template>

<script>
import ArticleBotApi from 'api/article-bot.api';
import { BotRunIndicator } from 'elements';
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
      runs: []
    };
  },
  components: {
    BotRunIndicator
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
        this.getRuns();
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

      let parsers = [];
      for (var key in this.bot.parser) {
        parsers.push({
          name: key,
          value: this.bot.parser[key]
        });
      }

      this.parsers = parsers;
    },
    addParser () {
      this.parsers.push({
        name: '',
        value: ''
      });
    },
    deleteParser (key) {
      const index = this.parsers.map(p => p.name).indexOf(key);
      this.parsers.splice(index, 1);
    },
    deleteBot (bot_id) {
      ArticleBotApi.deleteBot(bot_id, result => {

      });
    },
    getRuns () {
      ArticleBotApi.getRuns(this.bot.id, runs => {
        this.runs = runs;
        this.runs.sort((a, b) => b.date - a.date);
      });
    }
  }
};
</script>
