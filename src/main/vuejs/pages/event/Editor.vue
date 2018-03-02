<template>
  <form class="form" v-loading="loading" @submit.prevent="submit">
    <div class="row">
      <div class="col-md-6">
        <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
          <label class="control-label">Name</label>
          <input class="form-control" 
            v-model="eventClone.name" 
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

        <div class="form-group" :class="{ 'has-error' : errors.has('date') }">
          <label class="control-label">Date</label>
          <date-time-picker 
            v-model="eventClone.date" 
            v-validate 
            data-vv-rules="required" 
            name="date">
          </date-time-picker>

          <span 
            class="text-danger" 
            v-if="errors.has('name')">
            {{ errors.first('date') }}
          </span>
        </div>

        <div class="form-group">
          <label class="control-label">Location</label>
          <place-picker 
            v-model="location" 
            name="location" 
            v-validate
            data-vv-rules="required">
          </place-picker>

          <span 
            class="text-danger" 
            v-if="errors.has('location')">
            {{ errors.first('location') }}
          </span>
        </div>

        <div class="form-group">
          <label class="form-label">Tags</label>
          <tag-input v-model="eventClone.tags" />
          
          <span 
            class="text-danger" 
            v-if="errors.has('tags')">
            {{ errors.first('tags') }}
          </span>
        </div>
      </div>
      <div class="col-md-6">
        <image-upload 
          v-model="eventClone.posterData" 
          :media="event.poster">
        </image-upload>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label">Summary</label>
      <text-editor v-model="eventClone.summary"></text-editor>
    </div>

    <div class="text-right">
      <button 
        type="button"
        class="btn btn-default" 
        @click="togglePublished" 
        :class="{ 'btn-danger': event.state == 'PUBLISHED' }">
        
        {{ event.state == 'PUBLISHED' ? 'Deactivate' : 'Publish' }}
      </button>
  
      <button 
        class="btn btn-primary" 
        type="submit">

        Save
      </button>
    </div>
  </form>
</template>

<script>
import {
  TextEditor,
  DatePicker,
  DateTimePicker,
  ImageUpload,
  GiphySearch,
  PlacePicker,
  TagInput
} from 'elements';
import EventApi from 'api/event.api';

export default {
  props:
  {
    event: {
      type: Object,
      default () {
        return {};
      }
    },
    compact: Boolean
  },
  computed: {
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
        this.eventClone = {
          ...this.eventClone,
          latitude: value.lat,
          longitude: value.lng
        };
      }
    }
  },
  components: {
    TextEditor,
    DatePicker,
    DateTimePicker,
    ImageUpload,
    GiphySearch,
    PlacePicker,
    TagInput
  },
  data () {
    return {
      loading: false,
      eventClone: {}
    };
  },
  watch: {
    event: {
      handler (newVal) {
        this.initialize();
      },
      deep: true
    }
  },
  created () {
    this.initialize();
  },
  methods: {
    initialize () {
      this.eventClone = {
        name: this.event.name,
        banner: this.event.banner,
        date: this.event.date,
        summary: this.event.summary,
        visibility: this.event.visibility,
        posterData: this.event.posterData,
        latitude: this.event.latitude,
        longitude: this.event.longitude,
        tags: this.event.tags || []
      };
    },
    submit () {
      this.$validator.validateAll().then(result => {
        if (result) {
          this.loading = true;

          this.$http.put(`/api/v1/event/${this.event.id}`, this.eventClone, {
            progress (e) {
              if (e.lengthComputable) {
                this.loading = (e.loaded / e.total) * 100;
              }
            }
          }).then(response => {
            this.$emit('updated', response.body);

            this.$success('notification.event.updated', this.event.name);
            this.loading = false;
          }, response => {
            if (response.body.fieldErrors) {
              response.body.fieldErrors.forEach((e) => {
                this.$set(this.errors, e.field, e);
              });
            };

            this.$error('notification.event.error.updated', this.event.name);
            this.loading = false;
          });
        }
      });
    },
    togglePublished () {
      EventApi.togglePublished(this.event.id, event => {
        this.event.state = event.state;
        this.$emit('updated', this.event);
      });
    }
  }
};
</script> 