<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-6" :class="{ 'col-sm-offset-3' : !edit }">
  
        <panel type="primary">
          <span slot="title">Overview</span>
  
          <div class="row">
            <div :class="{ 'col-md-6' : edit, 'col-xs-12' : !edit }">
              <div class="form-group" :class="{ 'has-error' : errors.name }">
                <label class="control-label">Name</label>
                <input class="form-control required" v-model="event.name" type="text">
              </div>
              <div class="form-group" :class="{ 'has-error' : errors.date }">
                <label class="control-label">Date</label>
                <date-picker v-model="event.date" />
              </div>
              <div class="form-group" :class="{ 'has-error' : errors.place }">
                <label class="control-label">Place</label>
                <select v-model="event.place" class="form-control">
                  <option :value="place" v-for="place in places" v-text="place.name"></option>
                </select>
              </div>
            </div>
            <div class="col-md-6" v-if="edit">
              <div class="fileupload waves-effect">
                <img v-if="event.poster != null" :src="event.poster.path" class="img-thumbnail dropzone" style="width: 100%" />
                <input type="file" class="upload" @change="onLogoChange">
              </div>
            </div>
          </div>
  
          <div class="form-group">
            <label class="control-label">Summary</label>
            <text-editor v-model="event.summary"></text-editor>
          </div>
  
          <div class="text-center">
            <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
              <span v-if="edit">Save</span>
              <span v-else>Submit</span> {{ event.name }}</button>
          </div>
        </panel>
      </div>
      <div class="col-sm-6" v-if="edit">
        <programme-editor :event="event" />
      </div>
    </div>
  </div>
</template>

<script>
import { DatePicker, TextEditor } from 'elements';
import ProgrammeEditor from '../event/Programme.vue';

export default {
  name: 'create-event',
  data () {
    return {
      event: {},
      edit: false,
      errors: {},
      places: []
    };
  },
  components: {
    DatePicker, TextEditor, ProgrammeEditor
  },
  methods: {
    submit: function () {
      this.errors = {};
      this.event.visibility = 'PUBLIC';

      if (this.edit) {
        var url = ['api/event', this.event.id].join('/');
        this.$http.put(url, this.event).then(response => {
          this.event = response.body;
          this.$success('Success !', 'Event ' + this.event.name + ' has been updated.');
        }, response => {
          response.body.fieldErrors.forEach((e) => {
            this.$set(this.errors, e.field, e);
          });
          this.$error('Error !', 'Problem in saving event.');
        });
      } else {
        this.$http.post('api/user/event', this.event).then(response => {
          this.event = response.body;
          this.$success('Success !', 'Event ' + this.event.name + ' has been created.');
          this.edit = true;
        }, response => {
          if (response.body != null) {
            response.body.fieldErrors.forEach((e) => {
              this.$set(this.errors, e.field, e);
            });
            this.$error('Error !', 'Problem in saving event.');
          } else this.$error(response.statusText, response.bodyText);
        });
      }
    },
    onLogoChange (e) {
      var self = this;

      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) {
        return;
      }

      var file = files[0];

      var reader = new FileReader();

      reader.onload = (e) => {
        self.event.posterData = e.target.result;
      };

      reader.readAsDataURL(file);
    }
  }
};
</script>

<style>

</style>
