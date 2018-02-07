<template>
  <div>
    <div class="row">
      <div class="col-lg-9 col-md-8">
        <form-editor v-model="conference.registrationForm" />
      </div>
      <div class="col-lg-3 col-md-4">
        <h3>Preview</h3>
        <custom-form class="preview-box" :form="conference.registrationForm" :submitFunc="doNothing" />
      </div>
    </div>


    <div class="text-center">
      <button type="button" @click="submit" class="btn btn-primary">
        Save
      </button>
    </div>
  </div>
</template>

<script>
import { FormEditor } from 'elements';
import { mapActions } from 'vuex';
import { CustomForm } from 'elements/forms';

export default {
  name: 'registration-settings',
  props: ['conference'],
  components: {
    FormEditor,
    CustomForm
  },
  methods: {
    doNothing () {

    },
    submit () {
      this.updateConference(this.conference).then(conference => {
        this.$success('notification.conference.updated', conference.name);
      });
    },
    ...mapActions([
      'updateConference'
    ])
  }
};
</script>

<style lang="less">
.preview-box {
  border: 1px solid #ddd;
  padding: 15px;
}
</style>
