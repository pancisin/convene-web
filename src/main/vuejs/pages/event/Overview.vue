<template>
  <div>
    <div class="row">
      <div class="col-lg-4 col-md-6">
        <div 
          class="form-group" 
          :class="{ 'has-error' : errors.has('name') }">

          <label class="control-label">Name</label>
          <input 
            class="form-control required" 
            v-model="event.name" 
            type="text" 
            name="name" 
            v-validate 
            data-vv-rules="required|min:3">

          <span 
            class="text-danger" 
            v-if="errors.has('name')">
            {{ errors.first('name') }}
          </span>
        </div>

        <div 
          class="form-group" 
          :class="{ 'has-error' : errors.has('date') }">
          
          <label class="control-label">Date</label>
          <date-time-picker 
            v-model="event.date" 
            required>
          </date-time-picker>

          <span 
            class="text-danger" 
            v-if="errors.has('name')">
            {{ errors.first('date') }}
          </span>
        </div>

        <div 
          class="form-group" 
          :class="{ 'has-error' : errors.has('visibility') }">

          <label class="control-label">Visibility</label>
          <select 
            v-model="event.visibility" 
            class="form-control" 
            name="visibility" 
            v-validate 
            data-vv-rules="required">

            <option 
              :value="option" 
              v-for="(option, index) in visibility_options" 
              v-text="option" 
              :key="index">
            </option>
          </select>

          <span 
            class="text-danger" 
            v-if="errors.has('name')">
            {{ errors.first('visibility') }}
          </span>
        </div>

        <div class="form-group">
          <label class="control-label">Location</label>
          <place-picker v-model="location" ></place-picker>
        </div>

        <div class="form-group">
          <label>Banner</label>
          <giphy-search 
            v-model="event.banner" 
            upload>
          </giphy-search>
        </div>

      </div>
      <div class="col-lg-4 col-md-6">
        <div class="form-group">
          <image-upload 
            v-model="event.posterData" 
            :media="event.poster">
          </image-upload>
        </div>

        <div class="form-group">
          <label class="form-label">Tags</label>
          <tag-input v-model="event.tags" />
          
          <span 
            class="text-danger" 
            v-if="errors.has('tags')">
            {{ errors.first('tags') }}
          </span>
        </div>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label">Summary</label>
      <text-editor v-model="event.summary"></text-editor>
    </div>

    <div class="text-right">
      <button 
        class="btn btn-danger" 
        @click="togglePublished" 
        v-if="event.state == 'PUBLISHED'">
        Deactivate
      </button>
      <button 
        class="btn btn-success" 
        @click="togglePublished" 
        v-if="event.state == 'DEACTIVATED'">
        Publish
      </button>
      <button 
        class="btn btn-primary" 
        type="submit" 
        @click="submit">
        Save
      </button>
    </div>
  </div>
</template>

<script>
import {
  TextEditor,
  DateTimePicker,
  ImageUpload,
  GiphySearch,
  PlacePicker,
  TagInput
} from 'elements';

export default {
  inject: ['provider'],
  props:
  {
    event: {
      type: Object,
      default () {
        return {};
      }
    }
  },
  computed: {
    api () {
      return this.provider.api;
    },
    visibility_options: {
      get () {
        return [
          'PUBLIC',
          'PRIVATE',
          'INVITED',
          'AUTHENTICATED'
        ];
      }
    },
    location: {
      get () {
        return {
          lat: this.event.latitude,
          lng: this.event.longitude
        };
      },
      set (value) {
        this.event = {
          ...this.event,
          latitude: value.lat,
          longitude: value.lng
        };
      }
    }
  },
  components: {
    TextEditor,
    DateTimePicker,
    ImageUpload,
    GiphySearch,
    PlacePicker,
    TagInput
  },
  data () {
    return {
      places: [],
      loading: false
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

          this.loading = true;
          this.api.putEvent(this.event, event => {
            this.$emit('updated', event);
            this.$success('notification.event.updated', event.name);
            this.loading = false;
          }, ({ fieldErrors }) => {
            fieldErrors.forEach((e) => {
              this.$set(this.errors, e.field, e);
            });
            this.$error('notification.event.error.updated', this.event.name);
            this.loading = false;
          });
        }
      });
    },
    getPlaces () {
      if (this.event.author == null) return;

      switch (this.event.author.type) {
        case 'page':
          var url = ['/api/page', this.event.author.id, 'place'].join('/');
          this.$http.get(url).then(response => {
            this.places = response.body;
          });
          break;
        case 'user':
          this.$http.get('/api/v1/user/me/place').then(response => {
            this.places = response.body;
          });
          break;
      }
    },
    togglePublished () {
      this.api.togglePublished(event => {
        this.event.state = event.state;
      });
    }
  }
};
</script>