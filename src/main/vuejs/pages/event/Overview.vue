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
          <date-picker v-model="event.date"></date-picker>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.visibility }">
          <label class="control-label">Visibility</label>
          <select v-model="event.visibility" class="form-control">
            <option :value="option" v-for="(option, index) in visibility_options" v-text="option" :key="index"></option>
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
        <image-upload v-model="event.bannerUrl" />
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
import ImageUpload from '../../elements/ImageUpload.vue'

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
    TextEditor, DatePicker, ImageUpload
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
          this.$emit('updated', response.body);
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
          var event = response.body;
          this.$success('Success !', 'Event ' + event.name + ' has been created.')
          this.$router.push('/admin/event/' + event.id);
        }, response => {
          if (response.body != null) {
            response.body.fieldErrors.forEach((e) => {
              this.$set(this.errors, e.field, e);
            });
            this.$error('Error !', 'Problem in saving event.');
          } else this.$error(response.statusText, response.bodyText)
        })
      }
    },
    getPlaces() {
      if (this.event.author == null) return;

      switch (this.event.author.type) {
        case "page":
          var url = ['api/page', this.event.author.id, 'place'].join('/');
          this.$http.get(url).then(response => {
            this.places = response.body;
          })
          break;
        case "user":
          this.$http.get('api/user/place').then(response => {
            this.places = response.body;
          });
          break;
      }
    }
  }
}
</script>