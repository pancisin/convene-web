<template>
  <div class="container">
    <div class="card-box p-b-0">
      <h4 class="text-dark  header-title m-t-0">Create event</h4>
      <p class="text-muted m-b-25 font-13">
        This basic wizard have no form validation and allows you to skip to another step by clicking on the tab.
      </p>
  
      <form @submit.prevent="submit">
        <div id="basicwizard" class=" pull-in">
          <ul class="nav nav-tabs navtab-custom nav-justified bg-muted">
            <li class="active"><a href="#tab1" data-toggle="tab" aria-expanded="false">Basic information</a></li>
            <li><a href="#tab2" data-toggle="tab" aria-expanded="true">Programme</a></li>
            <li><a href="#tab3" data-toggle="tab">Attendees</a></li>
          </ul>
          <div class="tab-content bx-s-0 m-b-0">
            <div class="tab-pane m-t-10 active in fade" id="tab1">
              <div class="row">
  
                <div class="form-group clearfix">
                  <label class="col-md-3 control-label">Name *</label>
                  <div class="col-md-9">
                    <input class="form-control required" v-model="event.name" type="text">
                  </div>
                </div>
  
              </div>
  
            </div>
            <div class="tab-pane m-t-10 fade in" id="tab2">
              <table class="table table-striped">
                <tbody>
                  <tr v-for="programme in event.programme">
                    <td v-text="programme.time"></td>
                    <td v-text="programme.description"></td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="tab-pane m-t-10 fade" id="tab3">
              <div class="row">
                <div class="form-group clearfix">
                  <div class="col-lg-12">
                    <div class="checkbox checkbox-primary">
                      <input id="checkbox-h" type="checkbox">
                      <label for="checkbox-h">
                        I agree with the Terms and Conditions.
                      </label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
  
          <div class="text-center m-b-10">
            <button class="btn btn-rounded btn-lg btn-inverse btn-primary" type="submit">
              <span v-if="submitted">Save</span><span v-else>Submit</span> {{ event.name }}</button>
          </div>
        </div>
      </form>
  
    </div>
  </div>
</template>

<script>
export default {
  name: 'event-create',
  data() {
    return {
      event: {
        name: null,
        date: null,
        visibility: null,
        programme: [],
      }
    }
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
    }
  }
}
</script>