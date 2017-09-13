<template>
  <panel type="default">
    <span slot="title">Overview</span>
  
    <div class="row">
      <div :class="{ 'col-md-6' : edit, 'col-xs-12' : !edit }">
        <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
          <label class="control-label">Name</label>
          <input class="form-control required" v-model="event.name" type="text" name="name" v-validate data-vv-rules="required|min:3">
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.has('date') }">
          <label class="control-label">Date</label>
          <date-picker v-model="event.date" v-validate data-vv-rules="required" name="date"></date-picker>
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('date') }}</span>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.has('visibility') }">
          <label class="control-label">Visibility</label>
          <select v-model="event.visibility" class="form-control" name="visibility" v-validate data-vv-rules="required">
            <option :value="option" v-for="(option, index) in visibility_options" v-text="option" :key="index"></option>
          </select>
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('visibility') }}</span>
        </div>
        <div class="form-group">
          <label class="control-label">Place</label>
          <select v-model="event.place" class="form-control">
            <option :value="place" v-for="place in places" v-text="place.name" :key="place.id"></option>
          </select>
        </div>
      </div>
      <div class="col-md-6" v-if="edit">
        <image-upload v-model="event.bannerUrl"></image-upload>
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
import { TextEditor, DatePicker, ImageUpload } from '../../elements';

export default {
  inject: ['provider'],
  props:
  {
    event: {
      type: Object,
      default () {
        return {};
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
    api () {
      return this.provider.api;
    },
    visibility_options: {
      get () {
        return [
          'PUBLIC', 'PRIVATE', 'INVITED', 'AUTHENTICATED'
        ];
      }
    }
  },
  components: {
    TextEditor, DatePicker, ImageUpload
  },
  data () {
    return {
      places: []
    };
  },
  created () {
    this.getPlaces();
  },
  watch: {
    'api': 'getPlaces'
  },
  methods: {
    submit: function () {
      this.$validator.validateAll().then(result => {
        if (result) {
          if (this.edit) {
            let url = ['api/event', this.event.id].join('/');
            this.$http.put(url, this.event).then(response => {
              this.$emit('updated', response.body);
              this.$success('Success !', 'Event ' + this.event.name + ' has been updated.');
            }, response => {
              response.body.fieldErrors.forEach((e) => {
                this.$set(this.errors, e.field, e);
              });
              this.$error('Error !', 'Problem in saving event.');
            });
          } else {
            let url = null;
            if (this.page_id != null) {
              url = ['api/page', this.page_id, 'event'].join('/');
            } else if (this.conference_id != null) {
              url = ['api/conference', this.conference_id, 'event'].join('/');
            } else {
              url = 'api/user/event';
            }

            this.$http.post(url, this.event).then(response => {
              var event = response.body;
              this.$success('Success !', 'Event ' + event.name + ' has been created.');
              this.$router.push('/admin/event/' + event.id);
            }, response => {
              if (response.body != null) {
                response.body.fieldErrors.forEach((e) => {
                  this.$set(this.errors, e.field, e);
                });
                this.$error('Error !', 'Problem in saving event.');
              } else this.$error(response.statusText, response.bodyText);
            });
          }
        }
      });
    },
    getPlaces () {
      if (this.event.author == null) return;

      switch (this.event.author.type) {
        case 'page':
          var url = ['api/page', this.event.author.id, 'place'].join('/');
          this.$http.get(url).then(response => {
            this.places = response.body;
          });
          break;
        case 'user':
          this.$http.get('api/user/place').then(response => {
            this.places = response.body;
          });
          break;
      }
    }
  }
};
</script>