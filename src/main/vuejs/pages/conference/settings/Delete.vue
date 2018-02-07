<template>
  <div>
    <p class="m-b-15">Write name of page to ensure it's not misstake and confirm with 'Delete'.</p>
    <div class="row">
      <div class="col-md-6">
        <input type="text" class="form-control" v-model="deleteConfirm">
      </div>
      <div class="col-md-6">
        <a class="btn btn-danger" :disabled="deleteConfirm !== conference.name" @click="deleteC">Delete</a>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  name: 'conference-delete',
  props: {
    conference: Object
  },
  data () {
    return {
      deleteConfirm: null
    };
  },
  methods: {
    ...mapActions([
      'deleteConference'
    ]),
    deleteC () {
      this.$prompt('notification.delete_prompt', this.conference.name, () => {
        this.deleteConference(this.conference).then(() => {
        });
      });
    }
  }
};
</script>

<style>

</style>
