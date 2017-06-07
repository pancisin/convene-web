<template>
  <div class="row">
    <div class="col-md-9">
      <panel type="default">
        <span slot="title">Overview</span>
      </panel>
    </div>
    <div class="col-md-3">
      <div class="widget-simple-chart text-right card-box">
        <h3 class="text-primary counter" v-text="page.followersCount"></h3>
        <p class="text-muted text-nowrap">Followers</p>
      </div>
    </div>
  </div>
</template>

<script>
import TextEditor from '../../elements/TextEditor.vue'
import { mapActions } from 'vuex'

export default {
  name: 'page-overview',
  props: ['page'],
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>
