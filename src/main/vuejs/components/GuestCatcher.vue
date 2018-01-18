<template>
  <div>
   <modal small :show.sync="displayLoginModal">
      <span slot="header">{{ $t('authenticate.login') }}</span>
      <div slot="body">
        <login-form :success="success" />
      </div>
    </modal>
  </div>
</template>

<script>
import Vue from 'vue';
import { mapGetters } from 'vuex';
import { LoginForm } from 'elements/forms';

export default {
  name: 'guest-cacher',
  data () {
    return {
      displayLoginModal: false,
      successCallback: () => {}
    };
  },
  components: {
    LoginForm
  },
  computed: {
    ...mapGetters(['authenticated'])
  },
  created () {
    Vue.prototype.$tryAuthenticate = this.tryAuthenticate;
  },
  methods: {
    tryAuthenticate (callback) {
      if (this.authenticated) {
        if (callback) {
          callback();
        }
      } else {
        this.displayLoginModal = true;
        this.successCallback = callback;
      }
    },
    success () {
      this.displayLoginModal = false;
      if (this.successCallback) {
        this.successCallback();
      }
    }
  }
};
</script>

<style>

</style>
