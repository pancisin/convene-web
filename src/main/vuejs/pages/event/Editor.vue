<template>
  <div v-loading="loading">
    <div class="row">
      <div class="col-md-6">
        <div class="form-group" :class="{ 'has-error' : errors.has('name') }">
          <label class="control-label">Name</label>
          <input class="form-control required" v-model="eventClone.name" type="text" name="name" v-validate data-vv-rules="required|min:3">
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('name') }}</span>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.has('date') }">
          <label class="control-label">Date</label>
          <date-picker v-model="eventClone.date" v-validate data-vv-rules="required" name="date"></date-picker>
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('date') }}</span>
        </div>
        <div class="form-group" :class="{ 'has-error' : errors.has('visibility') }">
          <label class="control-label">Visibility</label>
          <select v-model="eventClone.visibility" class="form-control" name="visibility" v-validate data-vv-rules="required">
            <option :value="option" v-for="(option, index) in visibility_options" v-text="option" :key="index"></option>
          </select>
          <span class="text-danger" v-if="errors.has('name')">{{ errors.first('visibility') }}</span>
        </div>
      <div class="form-group">
        <label class="control-label">Location</label>
        <place-picker v-model="location"></place-picker>
      </div>
      </div>
      <div class="col-md-6">
        <image-upload v-model="eventClone.posterData" :media="event.poster"></image-upload>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label">Summary</label>
      <text-editor v-model="eventClone.summary"></text-editor>
    </div>

    <div class="text-right">
      <button class="btn btn-primary" type="submit" @click="submit">
        Save
      </button>
    </div>
  </div>
</template>

<script>
import {
  TextEditor,
  DatePicker,
  ImageUpload,
  GiphySearch,
  PlacePicker
} from 'elements';

export default {
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
    ImageUpload,
    GiphySearch,
    PlacePicker
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
        posterData: this.event.posterData
      };
    },
    submit () {
      this.$validator.validateAll().then(result => {
        if (result) {
          this.loading = true;

          this.$http.put(`/api/event/${this.event.id}`, this.eventClone, {
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
    }
  }
};
</script> 