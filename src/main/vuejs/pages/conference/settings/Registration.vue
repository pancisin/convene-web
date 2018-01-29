<template>
  <panel>
    <form-editor v-model="conference.registrationForm" />

    <div class="text-center">
      <button type="button" @click="submit" class="btn btn-primary">
        Save
      </button>
    </div>
  </panel>
</template>

<script>
import { FormEditor } from 'elements';
import { mapActions } from 'vuex';

export default {
  name: 'registration-settings',
  props: ['conference'],
  components: {
    FormEditor
  },
  methods: {
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
