<template>
  <div class="row" v-if="event != null">
  
    <div class="col-md-4">
      <div class="form-group">
        <label class="control-label">Name:</label>
        <input type="text" class="form-control" v-model="event.name">
      </div>
  
      <div class="form-group">
        <label class="control-label">Visibility: </label>
        <select v-model="event.visibility" class="form-control" :disabled="conference != null">
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
            <th>Time</th>
            <th>Name</th>
            <th>Description</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="programme in event.programme">
            <td v-text="programme.time"></td>
            <td v-text="programme.name"></td>
            <td v-text="programme.description"></td>
            <td>
              <a @click="removeProgramme(programme.id)" class="btn btn-danger">
                <i class="fa fa-trash" />
              </a>
            </td>
          </tr>
          <tr>
            <td>
              <input type="text" v-model="new_programme.time" class="form-control" />
            </td>
            <td>
              <input type="text" v-model="new_programme.name" class="form-control" />
            </td>
            <td>
              <input type="text" v-model="new_programme.description" class="form-control" />
            </td>
            <td>
              <a @click="submitProgramme" class="btn btn-success">
                <i class="fa fa-save" />
              </a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  
    <div class="text-center col-xs-12 m-t-10">
      <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
        <span v-if="edit">Save</span>
        <span v-else>Submit</span> {{ event.name }}</button>
    </div>
  
  </div>
</template>
<script>
export default {
  name: 'event-form',
  props: {
    event: {
      type: Object,
      default: new Object()
    },
    conference: Object,
    page: Object
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
          this.$success('Success !', 'Event ' + this.event.name + ' has been updated.');
        });
      } else {

        var entity = this.page != null ? 'page/' + this.page.id : 'user';
        entity = this.conference != null ? 'conference/' + this.conference.id : entity;

        var url = ['api', entity, 'event'].join('/');

        this.$http.post(url, this.event).then(response => {
          this.event = response.body;
          this.$emit('updated', this.event);
          this.$success('Success !', 'Event ' + this.event.name + ' has been created.');
        })
      }
    },
    submitProgramme() {
      var url = ['api/event', this.event.id, 'programme'].join('/');

      this.$http.post(url, this.new_programme).then(response => {
        this.event.programme.push(response.body);
        this.new_programme = {
          time: null,
          description: null
        }
      })
    },
    removeProgramme(programme_id) {
      var url = ['api/programme', programme_id].join('/');

      this.$http.delete(url).then(response => {
        this.event.programme = this.event.programme.filter(p => {
          return p.id != programme_id;
        })
      })
    }
  }
}
</script>