<template>
  <div class="position-query">
   <modal :show.sync="displayModal">
      <span slot="header">Where are you ?</span>
      <div slot="body">
        <position-form />
      </div>
    </modal>
  </div>
</template>

<script>
import Vue from 'vue';
import { mapGetters, mapActions } from 'vuex';
import { PositionForm } from 'elements/forms';

export default {
  name: 'position-query',
  data () {
    return {
      displayModal: false,
      successCallback: () => {},
      errorCallback: () => {}
    };
  },
  components: {
    PositionForm
  },
  computed: {
    ...mapGetters(['positionKnown', 'position'])
  },
  watch: {
    positionKnown () {
      if (this.successCallback) {
        this.successCallback(this.position);
      }

      this.displayModal = false;
    },
    displayModal (newVal, oldVal) {
      if (oldVal === true && newVal === false && !this.positionKnown && this.errorCallback != null) {
        this.errorCallback();
      }
    }
  },
  created () {
    Vue.prototype.$tryGetPosition = this.tryGetPosition;
  },
  methods: {
    ...mapActions(['setPosition']),
    tryGetPosition (callback, errorCallback) {
      if (this.positionKnown) {
        callback(this.position);
      } else {
        this.successCallback = callback;
        this.errorCallback = errorCallback;
        this.displayModal = true;
      }
    }
  }
};
</script>

