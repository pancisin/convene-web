<template>
  <div>
    <div class="page-title-box">
      <h4 class="page-title" v-text="event.name"></h4>
    </div>
    <router-tab-navigation :menu="menu">
      <transition name="fade">
        <router-view :event="event" />
      </transition>
    </router-tab-navigation>

    <!-- <div class="col-md-3">
      <div class="widget-simple-chart text-right card-box">
        <h3 class="text-primary counter" v-text="event.attendeesCount"></h3>
        <p class="text-muted text-nowrap">Attender</p>
      </div>
    </div> -->
  </div>
</template>

<script>
import EventApi from 'api/event.api';
import InjectorGenerator from '../services/InjectorGenerator';
import { EventMenu } from '../services/maps/menus';
import { RouterTabNavigation } from 'elements';

export default {
  name: 'event',
  data () {
    return {
      event: {},
      edit: false,
      injector: null,
      revoke: null
    };
  },
  provide () {
    const provider = {};

    Object.defineProperty(provider, 'api', {
      get: () => this.injector
    });

    return { provider };
  },
  components: {
    RouterTabNavigation
  },
  watch: {
    '$route': 'getEvent'
  },
  created () {
    this.getEvent();
  },
  computed: {
    menu () {
      return EventMenu;
    }
  },
  methods: {
    getEvent () {
      this.injector = InjectorGenerator.generate(EventApi, this.$route.params.id);

      this.injector.getEvent(event => {
        this.event = event;
        this.edit = event.id != null;
      });
    },
    eventUpdated (event) {
      this.event = event;
    }
  }
};
</script>