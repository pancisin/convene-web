<template>
  <div v-loading="loading">
    <div class="row">
      <div class="col-lg-4 col-md-6">
        <image-upload 
          v-model="conference.posterData" 
          :media="conference.poster">
        </image-upload>
      </div>

      <div class="col-lg-4 col-md-6">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input 
            class="form-control required" 
            v-model="conference.name" 
            type="text">
        </div>

        <div class="form-group">
          <label class="control-label">Visibility: </label>
          <select v-model="conference.visibility" class="form-control">
            <option 
              :value="option" 
              v-for="option in visibility_options" 
              v-text="option" 
              :key="option"></option>
          </select>
        </div>

        <categorizer 
          :category.sync="conference.category" 
          :branch.sync="conference.branch" 
          required />
      </div>
    </div>

    <div class="form-group m-t-20">
      <label class="control-label">Summary: </label>
      <text-editor v-model="conference.summary"></text-editor>
    </div>

    <div class="text-center">
      <button 
        class="btn btn-rounded btn-danger"
        @click="toggleConferencePublished(conference)" 
        v-if="conference.state == 'PUBLISHED'">
        Deactivate</button>
      <button 
        class="btn btn-rounded btn-success" 
        @click="toggleConferencePublished(conference)" 
        v-if="conference.state == 'DEACTIVATED'">
        Publish
      </button>
      <button 
        class="btn btn-rounded btn-primary" 
        type="submit" 
        @click="submit" 
        :disabled="conference.state == 'BLOCKED'">
        <span>Save</span>
      </button>
    </div>
  </div>
</template>

<script>
import {
  TextEditor,
  ImageUpload,
  Categorizer
} from 'elements';
import { mapActions } from 'vuex';

export default {
  props: {
    conference: {
      type: Object,
      default () {
        return {};
      }
    }
  },
  data () {
    return {
      loading: false
    };
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
    TextEditor,
    ImageUpload,
    Categorizer
  },
  methods: {
    submit () {
      this.loading = true;
      this.updateConference(this.conference).then(conference => {
        this.$success('notification.conference.updated', conference.name);
        this.loading = false;
      });
    },
    ...mapActions([
      'updateConference', 'toggleConferencePublished'
    ])
  }
};
</script>