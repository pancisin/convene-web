<template>
  <div class="row">
    <div class="col-sm-12">
      <div class="page-title-box">
        <h4 class="page-title" v-if="conference != null" v-text="conference.name"></h4>
      </div>
    </div>
  
    <div class="col-xs-12" v-if="conference != null">
      <div class="alert alert-danger" v-if="conference.state == 'BLOCKED'">This conference state is blocked. You are not allowed to make changes to this conference.</div>
      <transition name="fade-down" mode="out-in">
        <keep-alive>
          <router-view :conference="conference" :editable="conference.state !== 'BLOCKED'"></router-view>
        </keep-alive>
      </transition>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import ConferenceInjector from '../services/injectors/conference.injector.js';

export default {
  name: 'conference',
  data () {
    return {
      edit: false,
      injector: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  computed: {
    ...mapGetters([
      'conferences', 'getConferenceById'
    ]),
    conference () {
      var conference_id = Number.parseInt(this.$route.params.id, 10);
      let conference = this.getConferenceById(conference_id);

      if (conference !== null) {
        this.edit = true;
        this.injector = new ConferenceInjector(conference_id);
      };

      return conference;
    }
  }
};
</script>