<template>
  <panel type="default">
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
        <div class="form-group" :class="{ 'has-error' : errors.visibility }">
          <label class="control-label">Visibility</label>
          <select v-model="event.visibility" class="form-control">
            <option :value="option" v-for="option in visibility_options" v-text="option"></option>
          </select>
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
          <img :src="event.bannerUrl" class="img-thumbnail dropzone" style="width: 100%" />
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
</template>

<script>
import TextEditor from '../../elements/TextEditor.vue'
import DatePicker from '../../elements/DatePicker.vue'
export default {
  props:
  {
    event: {
      type: Object,
      default() {
        return new Object();
      }
    },
    edit: {
      type: Boolean,
      default: false
    },
    page_id: String,
    conference_id: String
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
  components: {
    TextEditor, DatePicker
  },
  data() {
    return {
      errors: new Object(),
      places: [],
    }
  },
  created() {
    this.getPlaces();
  },
  watch: {
    'event': 'getPlaces'
  },
  methods: {
    submit: function () {
      this.errors = new Object();
      if (this.edit) {
        var url = ['api/event', this.event.id].join('/');
        this.$http.put(url, this.event).then(response => {
          this.event = response.body;
          this.$success('Success !', 'Event ' + this.event.name + ' has been updated.')
        }, response => {
          response.body.fieldErrors.forEach((e) => {
            this.$set(this.errors, e.field, e);
          });
          this.$error('Error !', 'Problem in saving event.');
        });
      } else {
        var url = null;
        if (this.page_id != null) {
          url = ['api/page', this.page_id, 'event'].join('/');
        } else if (this.conference_id != null) {
          url = ['api/conference', this.conference_id, 'event'].join('/');
        } else {
          url = 'api/user/event';
        }

        this.$http.post(url, this.event).then(response => {
          this.event = response.body;
          this.$router.push('/admin/event/' + this.event.id);
          this.$success('Success !', 'Event ' + this.event.name + ' has been created.')
          this.edit = true;
        }, response => {
          response.body.fieldErrors.forEach((e) => {
            this.$set(this.errors, e.field, e);
          });
          this.$error('Error !', 'Problem in saving event.');
        })
      }
    },
    onLogoChange(e) {
      var self = this;

      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;

      var file = files[0];

      var image = new Image();
      var reader = new FileReader();

      reader.onload = (e) => {
        self.event.bannerUrl = e.target.result;
      };

      reader.readAsDataURL(file);
    },
    getPlaces() {
      if (this.event.page == null) return;

      var url = ['api/page', this.event.page.id, 'place'].join('/');
      this.$http.get(url).then(response => {
        this.places = response.body;
      })
    }
  }
}
</script>