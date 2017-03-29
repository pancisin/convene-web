<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">Users</div>
        <div class="card-body">
  
          <form class="form form-horizontal"
                @submit.prevent="submitDuty">
            <div class="section">
              <div class="section-body">
                <div class="form-group">
                  <label class="col-md-3 control-label">Location</label>
                  <div class="col-md-9">
                    <input type="text"
                           v-model="duty.location"
                           class="form-control"
                           placeholder="">
                  </div>
                </div>
  
                <div class="form-group">
                  <label class="col-md-3 control-label">Start Date</label>
                  <div class="col-md-9">
                    <v-datepicker :value.sync="duty.startDate"
                                  class="form-control"></v-datepicker>
                  </div>
                </div>
  
                <div class="form-group">
                  <label class="col-md-3 control-label">End Date</label>
                  <div class="col-md-9">
                    <datepicker :value.sync="duty.endDate"></datepicker>
                  </div>
                </div>
  
                <div class="form-group">
                  <label class="col-md-3 control-label">Recurrence</label>
                  <div class="col-md-9">
                    <recurrece-input v-model="duty.recurrence"></recurrece-input>
                  </div>
                </div>
              </div>
            </div>
  
            <transition name="fade">
              <div class="section"
                   v-if="edit_mode">
                <div class="section-title">Advanced</div>
                <div class="section-body">
                  <div class="form-group">
                    <label class="col-md-3 control-label">Employees</label>
                    <div class="col-md-9">
                      <v-select label="firstName"
                                :debounce="250"
                                :value.sync="duty.employees"
                                :on-search="getEmployees"
                                :options="employees"
                                multiple></v-select>
                    </div>
                  </div>
                </div>
              </div>
            </transition>
  
            <div class="form-footer">
              <div class="form-group">
                <div class="col-md-9 col-md-offset-3">
                  <button type="submit"
                          class="btn btn-primary">
                    <span v-if="edit_mode">
                                  Update
                              </span>
                    <span v-else>
                                  Create
                              </span>
                  </button>
                  <button type="button"
                          class="btn btn-default"
                          @click="$router.go(-1)">Cancel</button>
                </div>
              </div>
            </div>
          </form>
  
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { datepicker } from 'vue-strap';
import RecurrenceInput from '../elements/RecurrenceInput.vue';

export default {
  name: 'create-duty',
  data: function () {
    return {
      duty: {
        id: null,
        location: null,
        startDate: '',
        endDate: null,
        employees: [],
        tasks: [],
        recurrence: null,
      },
      employees: [],
    }
  },
  components: {
    'datepicker': datepicker,
    'recurrece-input': RecurrenceInput,
  },
  computed: {
    edit_mode: {
      get: function () {
        return this.duty.id != null;
      }
    }
  },
  created: function () {
    var duty_id = this.$route.params.id;
    if (duty_id != null) {
      this.$http.get('/api/duty/' + duty_id).then(response => {
        this.duty = response.body;
      })
    }
  },
  methods: {
    submitDuty: function () {
      if (this.edit_mode) {
        var url = ['/api/duty', this.duty.id].join('/');
        this.$http.put(url, JSON.stringify(this.duty)).then(function (response) {
          this.duty = response.body;
        });
      } else {
        var url = ['/api/company', this.$store.getters.company_id, 'duties'].join('/');
        this.$http.post(url, JSON.stringify(this.duty)).then(function (response) {
          this.duty = response.body;
        });
      }
    },
    getEmployees: function (search, loading) {
      var url = ['/api/company', this.$store.getters.company_id, 'employees'].join('/');
      loading(true)
      this.$http.get(url).then(response => {
        this.employees = response.body;
        loading(false)
      })
    }
  }
}
</script>