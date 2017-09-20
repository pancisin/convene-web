<template>
  <panel type="default">
    <span slot="title">Overview</span>

    <div class="row">
      <div class="col-md-6">
        <image-upload v-model="conference.posterData" :media="conference.poster"></image-upload>
      </div>

      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input class="form-control required" v-model="conference.name" type="text">
        </div>
        <div class="form-group">
          <label class="control-label">Visibility: </label>
          <select v-model="conference.visibility" class="form-control">
            <option :value="option" v-for="option in visibility_options" v-text="option" :key="option"></option>
          </select>
        </div>
      </div>

      <button class="btn btn-rounded btn-danger" @click="toggleConferencePublished(conference)" v-if="conference.state == 'PUBLISHED'">Deactivate</button>
      <button class="btn btn-rounded btn-success" @click="toggleConferencePublished(conference)" v-if="conference.state == 'DEACTIVATED'">
        Publish
      </button>
    </div>

    <div class="form-group m-t-20">
      <label class="control-label">Summary: </label>
      <text-editor v-model="conference.summary"></text-editor>
    </div>

    <div class="text-center">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit" :disabled="conference.state == 'BLOCKED'">
        <span>Save</span>
      </button>
    </div>
  </panel>
</template>

<script>
import { TextEditor, ImageUpload } from 'elements';
import { mapActions } from 'vuex';
export default {
  props:
  {
    conference: {
      type: Object,
      default () {
        return {};
      }
    }
  },
  computed: {
    visibility_options: {
      get () {
        return [
          'PUBLIC', 'PRIVATE', 'INVITED', 'AUTHENTICATED'
        ];
      }
    }
  },
  components: {
    TextEditor, ImageUpload
  },
  methods: {
    submit () {
      this.updateConference(this.conference).then(conference => {
        this.$success('notification.conference.updated', conference.name);
      });
    },
    ...mapActions([
      'updateConference', 'toggleConferencePublished'
    ])
  }
};
</script>