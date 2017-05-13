<template>
  <div class="card-box">
    <h4 class="text-dark  header-title m-t-0">Overview</h4>
  
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Name: </label>
          <input class="form-control required" v-model="event.name" type="text">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label">Visibility: </label>
          <select v-model="event.visibility" class="form-control">
            <option :value="option" v-for="option in visibility_options" v-text="option"></option>
          </select>
        </div>
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
    TextEditor
  },
  methods: {
     submit: function () {
      if (this.edit) {
        var url = ['api/event', this.event.id].join('/');
        this.$http.put(url, this.event).then(response => {
          this.event = response.body;
          this.$success('Success !', 'Event ' + this.event.name + ' has been updated.')
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
  }
}
</script>