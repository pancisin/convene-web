<template>
  <div class="card-box">
    <h4 class="text-dark  header-title m-t-0">Overview</h4>
  
    <div class="row">
      <div :class="{ 'col-md-6' : edit, 'col-xs-12' : !edit }">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input class="form-control required" v-model="event.name" type="text">
        </div>
        <div class="form-group">
          <label class="control-label">Date: </label>
          <date-picker v-model="event.date" />
        </div>
        <div class="form-group">
          <label class="control-label">Visibility: </label>
          <select v-model="event.visibility" class="form-control">
            <option :value="option" v-for="option in visibility_options" v-text="option"></option>
          </select>
        </div>
      </div>
      <div class="col-md-6" v-if="edit">
        <img :src="event.bannerUrl" class="img-thumbnail" />
        <input type="file" @change="onLogoChange" class="form-control" placeholder="Banner logo">
      </div>
    </div>
  
    <div class="form-group">
      <label class="control-label">Summary: </label>
      <text-editor v-model="event.summary"></text-editor>
    </div>
  
    <div class="text-center">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ event.name }}</button>
    </div>
  </div>
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
    }
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
      errors: [],
    }
  },
  methods: {
    submit: function () {
      if (this.edit) {
        var url = ['api/event', this.event.id].join('/');
        this.$http.put(url, this.event).then(response => {
          this.event = response.body;
          this.$success('Success !', 'Event ' + this.event.name + ' has been updated.')
        }, response => {
          this.errors = response.body.fieldErrors;
        });
      } else {
        this.$http.post('api/user/event', this.event).then(response => {
          this.event = response.body;
          this.$router.push('/admin/event/' + this.event.id);
          this.$success('Success !', 'Event ' + this.event.name + ' has been created.')
          this.edit = true;
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
    }
  }
}
</script>