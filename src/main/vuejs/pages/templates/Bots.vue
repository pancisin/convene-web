<template>
  <div class="row">
    <div class="col-md-10">
      <panel type="table">
        <span slot="title">Bots</span>
        <table class="table" v-loading="loading">
          <thead>
            <tr>
              <th>
                UUID
              </th>
              <th>
                Facebook page
              </th>
              <th>
                Runs
              </th>
              <th>
                Action
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(bot, index) in bots" :key="bot.id">
              <td>
                <router-link :to="{ name: 'event-bot', params: { bot_id: bot.id } }">
                  {{ bot.id }}
                </router-link>
              </td>
              <td>
                {{ bot.fbPageId }}
              </td>
              <td>
                {{ bot.runsCount }}
              </td>
              <td>
                <a class="btn btn-default btn-xs" @click="toggleActive(bot.id)" :class="{ 'btn-danger' : bot.active }">{{ bot.active ? 'Dectivate' : 'Activate' }}</a>
                <a class="btn btn-warning btn-xs" @click="run(bot.id)">Run</a>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="text-center">
          <router-link to="create-event-bot" class="btn btn-default btn-rounded">Create event bot</router-link>
        </div>
      </panel>
    </div>
    <div class="col-md-2 hidden-sm hidden-xs">
      <img src="event_bot.png" style="width: 100%; padding: 20px;">
    </div>
  </div>
</template>

<script>
import EventBotApi from 'api/event-bot.api';
export default {
  name: 'bots',
  inject: ['provider'],
  data () {
    return {
      bots: [],
      loading: false
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  watch: {
    'api': 'getBots'
  },
  created () {
    this.getBots();
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
      EventBotApi.toggleActive(bot_id, bot => {
        let index = this.bots.map(b => b.id).indexOf(bot_id);
        this.bots.splice(index, 1, bot);
      });
    },
    run (bot_id) {
      this.loading = true;
      EventBotApi.run(bot_id, run => {
        console.log(run);
        this.loading = false;
      });
    }
  }
};
</script>
