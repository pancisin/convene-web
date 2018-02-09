<template>
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
</template>

<script>
import ArticleBotApi from 'api/article-bot.api';
import { DataParserEditor } from 'elements';
import { calculateHash } from '../../services/helpers';

export default {
  name: 'article-bot-form',
  inject: ['provider'],
  props: {
    bot: Object
  },
  data () {
    return {
      originalBot: null
    };
  },
  components: {
    DataParserEditor
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
  methods: {
    submit () {
      if (!this.bot.id) {
        this.api.postBot(this.bot, bot => {
          this.initializeBot(bot);
          this.$emit('submit', bot);
        });
      } else {
        ArticleBotApi.putArticleBot(this.bot.id, this.bot, bot => {
          this.initializeBot(bot);
          this.$emit('submit', bot);
        });
      }
    },
    initializeBot (bot) {
      this.bot = bot;
      this.originalBot = calculateHash(JSON.stringify(bot));
    }
  }
};
</script>
