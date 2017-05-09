<template>
  <div class="row">
  
    <div class="col-md-4">
      <div class="form-group">
        <label class="control-label">Name:</label>
        <input type="text" class="form-control" v-model="event.name">
      </div>
  
      <div class="form-group">
        <label class="control-label">Visibility: </label>
        <select v-model="event.visibility" class="form-control">
          <option :value="option" v-for="option in visibility_options" v-text="option"></option>
        </select>
      </div>
  
      <div class="form-group">
        <label class="control-label">Summary:</label>
        <input type="text" class="form-control" v-model="event.summary">
      </div>
  
    </div>
  
    <div class="col-md-8">
      <table class="table table-striped table-bordered">
        <thead>
          <tr>
            <th>time</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="programme in event.programme">
            <td v-text="programme.time"></td>
            <td v-text="programme.description"></td>
          </tr>
          <tr>
            <td>
              <input type="text" v-model="new_programme.time" class="form-control" />
            </td>
            <td>
              <input type="text" v-model="new_programme.description" class="form-control" />
              <a @click="submitProgramme" class="btn bnt-link">
                <i class="fa fa-save" />
              </a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  
    <div class="text-center col-xs-12">
      <button class="btn btn-rounded btn-lg btn-inverse btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ event.name }}</button>
    </div>
  
  </div>
</template>
<script>
export default {
  name: 'event-form',
  props: {
    event: Object,
    default: new Object()
  },
  data() {
    return {
      new_programme: new Object(),
    }
  },
  computed: {
    visibility_options: {
      get() {
        return [
          'PUBLIC', 'PRIVATE', 'INVITED', 'AUTHENTICATED'
        ]
      }
    },
    edit: {
      get() {
        return this.event.id != null;
      }
    }
  },
  methods: {
    submit() {
      if (this.edit) {
        this.$http.put('api/event/' + this.event.id, this.event).then(response => {
          this.event = response.body;
        });
      } else {
        this.$http.post('api/user/event', this.event).then(response => {
          this.event = response.body;
        })
      }
    },
    submitProgramme: function () {
      var url = ['api/event', this.event.id, 'programme'].join('/');

      this.$http.post(url, this.new_programme).then(response => {
        this.event.programme.push(response.body);
        this.new_programme = {
          time: null,
          description: null
        }
      })
    }
  }
}
</script>