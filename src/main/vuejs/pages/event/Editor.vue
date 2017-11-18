<template>
  <div v-loading="loading">
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
          <label>Banner</label>
          <giphy-search v-model="event.banner" upload></giphy-search>
        </div>

      </div>
      <div class="col-md-6" v-if="edit">
        <image-upload v-model="event.posterData" :media="event.poster"></image-upload>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label">Summary</label>
      <text-editor v-model="event.summary"></text-editor>
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
  GiphySearch 
} from 'elements';

export default {
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
    }
  },
  components: {
    TextEditor, 
    DatePicker, 
    ImageUpload, 
    GiphySearch
  },
  data () {
    return {
      loading: false
    };
  },
  methods: {
    submit: function () {
      this.$validator.validateAll().then(result => {
        if (result) {
          this.loading = true;

          this.$http.put(`api/event/${this.event.id}`, {
            name: this.event.name,
            banner: this.event.banner,
            date: this.event.date,
            summary: this.event.summary,
            visibility: this.event.visibility,
            posterData: this.event.posterData
          }, {
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
            response.body.fieldErrors.forEach((e) => {
              this.$set(this.errors, e.field, e);
            });
            this.$error('notification.event.error.updated', this.event.name);
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>