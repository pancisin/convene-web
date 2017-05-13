<template>
  <div>
    <div class="card-box p-b-0">
      <h4 class="text-dark  header-title m-t-0">Create event</h4>
      <p class="text-muted m-b-25 font-13">
        This basic wizard have no form validation and allows you to skip to another step by clicking on the tab.
      </p>
  
      <div id="basicwizard" class=" pull-in">
        <ul class="nav nav-tabs navtab-custom nav-justified bg-muted">
          <li class="active">
            <a href="#tab1" data-toggle="tab" aria-expanded="false">Basic information</a>
          </li>
          <li>
            <a href="#tab2" data-toggle="tab" aria-expanded="true">Programme</a>
          </li>
          <li>
            <a href="#tab3" data-toggle="tab">Invitations</a>
          </li>
        </ul>
        <div class="tab-content">
          <div class="tab-pane m-t-10 active in fade" id="tab1">
  
            <div class="col-md-6">
              <div class="form-group">
                <label class="control-label">Name:</label>
                <input class="form-control required" v-model="event.name" type="text">
              </div>
            </div>
  
            <div class="col-md-6">
              <div class="form-group">
                <label class="control-label">Visibility:</label>
                <select v-model="event.visibility" class="form-control">
                  <option :value="option" v-for="option in visibility_options" v-text="option"></option>
                </select>
              </div>
            </div>
  
            <div class="col-xs-12">
              <div class="form-group clearfix">
                <label class="control-label">Summary:</label>
                <text-editor v-model="event.summary"></text-editor>
              </div>
            </div>
  
          </div>
          <div class="tab-pane m-t-10 fade in" id="tab2">
            <div class="col-xs-12">
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
          </div>
          <div class="tab-pane m-t-10 fade" id="tab3">
            <div class="form-group clearfix">
              <div class="checkbox checkbox-primary">
                <input id="checkbox-h" type="checkbox">
                <label for="checkbox-h">
                  I agree with the Terms and Conditions.
                </label>
              </div>
            </div>
          </div>
        </div>
  
        <div class="text-center m-b-10">
          <button class="btn btn-rounded btn-lg btn-inverse btn-primary" type="submit" @click="submit">
            <span v-if="submitted">Save</span>
            <span v-else>Submit</span> {{ event.name }}</button>
        </div>
      </div>
  
    </div>
  </div>
</template>

<script>
import TextEditor from '../elements/TextEditor.vue'
export default {
  name: 'event-create',
  data() {
    return {
      event: new Object(),
      visibility_options: [
        'PUBLIC', 'PRIVATE', 'INVITED', 'AUTHENTICATED'
      ],
      new_programme: {
        time: null,
        description: null
      }
    }
  },
  components: {
    TextEditor
  },
  computed: {
    submitted() {
      return this.event.id != null;
    }
  },
  created() {
    var event_id = this.$route.params.id;
    if (event_id != null) {
      this.$http.get('api/event/' + event_id).then(response => {
        this.event = response.body;
      })
    }
  },
  methods: {
    submit: function () {
      this.$http.post('api/user/event', this.event).then(response => {
        this.event = response.body;
      })
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