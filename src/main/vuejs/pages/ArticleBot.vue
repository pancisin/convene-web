<template>
  <div v-if="bot != null">
    <div class="page-title-box">
      <h4 class="page-title" v-text="bot.id"></h4>
    </div>
    <panel type="primary">
      <span slot="title">
        Article bot
      </span>

      <form class="form" @submit.prevent="submit">
        <div class="form-group">
          <label class="control-label">Source url</label>
          <input class="form-control required" v-model="bot.sourceUrl" type="text">
        </div>

        <div class="form-group">
          <label class="control-label">Source type</label>
          <input class="form-control required" v-model="bot.sourceType" type="text">
        </div>

        <hr>
        <h4>Parsers</h4>

        <div class="row" v-for="(parser, index) in parsers" :key="index">
          <div class="form-group col-md-6">
            <label class="control-label">Field name</label>
            <input class="form-control required" v-model="parser.name" type="text">
          </div>
          <div class="form-group col-md-6">
            <label class="control-label">Parser</label>
            <input class="form-control required" v-model="parser.value" type="text">
          </div>
        </div>

        <div class="text-center">
          <a class="btn btn-xs btn-primary btn-rounded" @click="addParser"><i class="fa fa-plus"></i></a>
        </div>

        <button type="submit" class="btn btn-success">Submit</button>
      </form>
    </panel>
    <panel type="default">
      <span slot="title">Runs</span>
    </panel>
  </div>
</template>

<script>
import ArticleBotApi from 'api/article-bot.api';
import { BotRunIndicator } from 'elements';

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
      parsers: []
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
    }
  },
  created () {
    if (this.edit) {
      ArticleBotApi.getArticleBot(this.$route.params.article_bot_id, bot => {
        this.bot = bot;

        let parsers = [];
        for (var key in this.bot.parser) {
          parsers.push({
            name: key,
            value: this.bot.parser[key]
          });
        }

        this.parsers = parsers;
      });
    }
  },
  methods: {
    submit () {
      if (!this.edit) {
        this.api.postBot(this.bot, bot => {
          this.$router.push(this.api.base_route);
        });
      } else {
        ArticleBotApi.putArticleBot(this.bot.id, this.bot, bot => {
          this.bot = bot;
        });
      }
    },
    addParser () {
      this.parsers.push({
        name: '',
        value: ''
      });
    },
    deleteBot (bot_id) {
      ArticleBotApi.deleteBot(bot_id, result => {

      });
    }
  }
};
</script>
