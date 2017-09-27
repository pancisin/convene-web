<template>
  <div class="row">
    <div class="col-md-10">
      <panel type="table">
        <span slot="title">Bots</span>
        <!-- <span slot="subtitle">Here are all ya robots ! Release them all at once...</span> -->
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
                <h4 v-if="bot.loading" class="text-danger">Loading...</h4>
                <router-link :to="{ name: 'event-bot', params: { bot_id: bot.id } }">
                  {{ bot.fbPageId }}
                </router-link>
              </td>
              <td class="text-center">
                {{ bot.runsCount }}
              </td>
              <td>
                <span v-if="bot.lastRun != null">
                  <i class="fa fa-check text-success" v-if="bot.lastRun.state.name === 'SUCCESS'"></i>
                  <i class="fa fa-exclamation-triangle text-danger" v-else></i>
                  <b>{{ bot.lastRun.date | moment('L LT') }}</b>
                </span>
              </td>
              <td class="text-center" v-if="editable">
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
      <img src="event_bot.png" style="width: 100%; padding: 20px;">
    </div>
  </div>
</template>

<script>
import EventBotApi from 'api/event-bot.api';
export default {
  name: 'bots',
  inject: ['provider'],
  props: {
    editable: Boolean
  },
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

    this.connectWM('stomp').then(frame => {
      this.$stompClient.subscribe('/user/queue/page.bots', response => {
        let run = JSON.parse(response.body);

        if (run != null) {
          this.bots.forEach((bot, index) => {
            if (bot.id === run.bot.id) {
              this.bots.splice(index, 1, {
                ...bot,
                loading: false,
                lastRun: run,
                runsCount: bot.runsCount + 1
              });
            }
          })
        }
      });
    });
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
      // this.loading = true;
      // EventBotApi.run(bot_id, run => {
      //   let index = this.bots.map(b => b.id).indexOf(bot_id);
      //   this.bots.splice(index, 1, {
      //     ...this.bots[index],
      //     lastRun: run,
      //     runsCount: this.bots[index].runsCount + 1
      //   });
      //   this.loading = false;
      // });

      this.sendWM(`/app/bot/${bot_id}/run`, JSON.stringify({})).then(() => {
        this.setLoading(bot_id, true);
      });
    },
    setLoading (bot_id, value) {
      this.bots.forEach((bot, index) => {
        if (bot.id === bot_id) {
          this.bots.splice(index, 1, {
            ...bot,
            loading: value
          });
        }
      })
    }
  }
};
</script>
