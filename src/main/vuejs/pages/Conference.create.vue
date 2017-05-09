<template>
  <div class="container">
    <div class="card-box p-b-0">
      <h4 class="text-dark  header-title m-t-0">
        <span v-if="edit" v-text="conference.name"></span>
        <span v-else> Create conference</span>
      </h4>
      <p class="text-muted m-b-25 font-13">
        This basic wizard have no form validation and allows you to skip to another step by clicking on the tab.
      </p>
  
      <ul class="nav nav-tabs navtab-custom nav-justified bg-muted">
        <li class="active">
          <a href="#tab1" data-toggle="tab">Basic information</a>
        </li>
        <li>
          <a href="#tab2" data-toggle="tab">Events</a>
        </li>
        <li>
          <a href="#tab3" data-toggle="tab">Invitations</a>
        </li>
      </ul>
      <div class="tab-content bx-s-0 m-b-0">
        <div class="tab-pane m-t-10 active in fade" id="tab1">
          <div class="row">
            <div class="form-group clearfix">
              <label class="col-md-3 control-label">Name *</label>
              <div class="col-md-9">
                <input class="form-control required" v-model="conference.name" type="text">
              </div>
            </div>
  
            <div class="form-group clearfix">
              <label class="col-md-3 control-label">Visibility *</label>
              <div class="col-md-9">
                <select v-model="conference.visibility" class="form-control">
                  <option :value="option" v-for="option in visibility_options" v-text="option"></option>
                </select>
              </div>
            </div>
  
            <div class="form-group clearfix">
              <label class="col-md-3 control-label">Summary *</label>
              <div class="col-md-9">
                <input class="form-control required" v-model="conference.summary" type="text">
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane m-t-10 fade in" id="tab2">
          <table class="table table-striped table-bordered">
            <thead>
              <tr>
                <th>Name</th>
                <th>Date</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="event in conference.events">
                <td>
                  <a @click="selectedEvent = event; display.modalEvent = true" v-text="event.name">
                  </a>
  
                </td>
                <td v-text="event.date"></td>
                <td v-text="event.summary"></td>
              </tr>
            </tbody>
          </table>
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
        <button class="btn btn-rounded btn-primary" type="submit" @click="submit">
          <span v-if="edit">Save</span>
          <span v-else>Submit</span> {{ conference.name }}</button>
      </div>
  
    </div>
  
    <modal :show.sync="display.modalEvent" @close="display.modalEvent = false">
      <h4 slot="header">Event</h4>
      <p slot="body">
        <event-form :event="selectedEvent"></event-form>
      </p>
    </modal>
  </div>
</template>

<script>
import Modal from '../elements/Modal.vue'
import EventForm from './include/Event.form.vue'
export default {
  name: 'conference',
  data() {
    return {
      conference: new Object(),
      edit: false,
      display: {
        modalEvent: false,
      },
      selectedEvent: null,
    }
  },
  components: {
    Modal, EventForm
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
  watch: {
    '$route': 'getConference'
  },
  created() {
    this.getConference();
  },
  methods: {
    submit() {
      if (this.edit) {
        var url = ['api/conference', this.conference.id].join('/');
        this.$http.put(url, this.conference).then(response => {
          this.conference = response.body;
          this.$success('Success !', 'conference updated');
        })
      } else {
        this.$http.post('api/user/conference', this.conference).then(response => {
          this.edit = true;
          this.conference = response.body;
        })
      }
    },
    getConference() {
      var conference_id = this.$route.params.id;
      if (conference_id != null) {
        this.edit = true;
        this.$http.get('api/conference/' + conference_id).then(response => {
          this.conference = response.body;

          var url = ['api/conference', conference_id, 'events'].join('/');
          this.$http.get(url).then(response => {
            this.conference.events = response.body;
            this.$forceUpdate();
          });
        })
      }
    },
  }
}
</script>