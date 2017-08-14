<template>
  <panel type="default">
    <span slot="title">Overview</span>
  
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input class="form-control required" v-model="conference.name" type="text">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Visibility: </label>
          <select v-model="conference.visibility" class="form-control">
            <option :value="option" v-for="option in visibility_options" v-text="option" :key="option"></option>
          </select>
        </div>
      </div>
    </div>
  
    <div class="form-group">
      <label class="control-label">Summary: </label>
      <text-editor v-model="conference.summary"></text-editor>
    </div>
  
    <div class="text-center">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ conference.name }}</button>
    </div>
  </panel>
</template>

<script>
import TextEditor from '../../elements/TextEditor.vue';
import { mapActions } from 'vuex';
export default {
  props:
  {
    conference: {
      type: Object,
      default () {
        return {};
      }
    },
    edit: {
      type: Boolean,
      default: false
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
    TextEditor
  },
  methods: {
    ...mapActions([
      'createConference', 'updateConference'
    ]),
    submit () {
      if (this.edit) {
        this.updateConference(this.conferences);
        // var url = ['api/conference', this.conference.id].join('/');
        // this.$http.put(url, this.conference).then(response => {
        //   this.$emit('updated', response.body);
        //   this.$success('Success !', this.conference.name + ' has been updated.');
        // });
      } else {
        this.createConference(this.conference);
        // this.$http.post('api/user/conference', this.conference).then(response => {
        //   this.edit = true;
        //   this.addConference(this.conference);
        //   this.$router.push('/admin/conference/' + this.conference.id);
        // });
      }
    }
  }
};
</script>