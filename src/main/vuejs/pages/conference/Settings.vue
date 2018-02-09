<template>
  <router-tab-navigation :menu="menu">
    <transition name="fade" mode="out-in">
      <router-view :conference="conference"></router-view>
    </transition>
  </router-tab-navigation>
</template>


<script>
import { RouterTabNavigation } from 'elements';
import { ConferenceSettingsMenu } from '../../services/maps/menus';

export default {
  name: 'conference-settings',
  props: {
    conference: Object
  },
  components: {
    RouterTabNavigation
  },
  computed: {
    menu () {
      return ConferenceSettingsMenu;
    }
  },
  render (h) {
    return h('router-tab-navigation', {
      props: {
        menu: ConferenceSettingsMenu
      }
    }, [
      h('transition', {
        props: {
          name: 'fade',
          mode: 'out-in'
        }
      }, [
        h('router-view', {
          props: {
            conference: this.conference || {}
          }
        })
      ])
    ]);
  }
};
</script>
