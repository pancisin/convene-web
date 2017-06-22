<template>
  <panel type="default">
    <span slot="title">Overview</span>
  
    <div class="row">
      <div class="col-md-6">
        <image-upload v-model="conference.bannerUrl" />
      </div>
  
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input class="form-control required" v-model="conference.name" type="text">
        </div>
        <div class="form-group">
          <label class="control-label">Visibility: </label>
          <select v-model="conference.visibility" class="form-control">
            <option :value="option" v-for="option in visibility_options" v-text="option"></option>
          </select>
        </div>
      </div>
    </div>
  
    <div class="form-group m-t-20">
      <label class="control-label">Summary: </label>
      <text-editor v-model="conference.summary"></text-editor>
    </div>
  
    <div class="text-center">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span>Save</span>
      </button>
    </div>
  </panel>
</template>

<script>
import TextEditor from '../../../elements/TextEditor.vue'
import ImageUpload from '../../../elements/ImageUpload.vue'
import { mapActions } from 'vuex'
export default {
  props:
  {
    conference: {
      type: Object,
      default() {
        return new Object();
      }
    },
  },
  computed: {
    visibility_options: {
      get() {
        return [
          'PUBLIC', 'PRIVATE', 'INVITED', 'AUTHENTICATED'
        ]
      }
    }
  },
  inject: ['api'],
  components: {
    TextEditor, ImageUpload
  },
  methods: {
    submit() {
      this.api.putConference(this.conference, conference => {
        this.$emit('updated', conference);
        this.$success('Success !', conference.name + ' has been updated.');
      });
    },
    ...mapActions([
      'addConference'
    ])
  }
}
</script>