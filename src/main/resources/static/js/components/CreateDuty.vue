<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">{{ $t('duty.create_update') }}</div>
        <div class="card-body">
  
          <form class="form form-horizontal"
                @submit.prevent="submitDuty">
            <div class="section">
              <div class="section-body">
                <div class="row">
                  <div class="col-md-6">
                    <v-select label="name"
                              v-model="duty.customer"
                              :options="customers"
                              :placeholder="$tc('customer.default', 1)"></v-select>
                    <input type="text"
                           v-model="duty.location"
                           class="form-control"
                           :placeholder="$t('duty.location')">
                    <textarea v-model="duty.description"
                              rows="3"
                              class="form-control"
                              :placeholder="$t('duty.description')"></textarea>
                  </div>
                  <div class="col-md-6">
                    <datepicker v-model="duty.startDate"
                                class="form-control"
                                :placeholder="$t('duty.start_date')"></datepicker>
                    <datepicker v-model="duty.endDate"
                                class="form-control"
                                :placeholder="$t('duty.end_date')"></datepicker>
                  </div>
                </div>
              </div>
            </div>
  
            <div class="section">
              <div class="section-title">
                {{ $t('duty.recurrence') }}
                <a class="btn btn-link"
                   @click="display.recurrence = !display.recurrence">
                  <i class="fa fa-lg fa-chevron-up"
                     aria-hidden="true"
                     v-if="display.recurrence"></i>
                  <i class="fa fa-lg fa-chevron-down"
                     aria-hidden="true"
                     v-else></i>
                </a>
              </div>
              <transition name="fade">
                <div class="section-body"
                     v-show="display.recurrence">
                  <recurrence-input :recurrence="duty.recurrence"></recurrence-input>
                </div>
              </transition>
            </div>
  
            <div class="section"
                 v-show="edit_mode">
              <div class="section-title">{{ $t('duty.advanced') }}
                <a class="btn btn-link"
                   @click="display.advanced = !display.advanced">
                  <i class="fa fa-lg fa-chevron-up"
                     aria-hidden="true"
                     v-if="display.advanced"></i>
                  <i class="fa fa-lg fa-chevron-down"
                     aria-hidden="true"
                     v-else></i>
                </a>
              </div>
              <transition name="fade">
                <div class="section-body"
                     v-show="display.advanced">
                  <div class="form-group">
                    <label class="col-md-3 control-label">{{ $t('duty.employees') }}</label>
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
              </transition>
            </div>
  
            <div class="form-footer">
              <div class="form-group">
                <div class="col-md-9 col-md-offset-3">
                  <button type="submit"
                          class="btn btn-primary">
                    <span v-if="edit_mode">{{ $t('actions.update_close') }}</span>
                    <span v-else>{{ $t('actions.create') }}</span>
                  </button>
                  <button type="button"
                          class="btn btn-default"
                          @click="$router.go(-1)">{{ $t('actions.cancel') }}</button>
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
import datepicker from '../elements/DatePicker.vue';
import recurrenceInput from '../elements/RecurrenceInput.vue';

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
        customer: null,
        tasks: [],
        recurrence: {
          minute: [0],
          hour: [0],
          day: [],
          month: [],
          dayOfWeek: [],
          weekOfMonth: []
        },
      },
      customers: [],
      employees: [],
      display: {
        recurrence: false,
        advanced: true,
      }
    }
  },
  components: {
    datepicker: datepicker,
    recurrenceInput: recurrenceInput,
  },
  computed: {
    edit_mode: {
      get: function () {
        return this.duty.id != null;
      }
    }
  },
  created: function () {
    this.getCustomers();
    var duty_id = this.$route.params.id;
    if (duty_id != null) {
      this.$http.get('api/duty/' + duty_id).then(response => {
        this.duty = response.body;
      })
    }
  },
  methods: {
    submitDuty: function () {
      if (this.edit_mode) {
        var url = ['api/duty', this.duty.id].join('/');
        this.$http.put(url, JSON.stringify(this.duty)).then(response => {
          this.duty = response.body;
        });
        this.$router.go(-1);
      } else {
        var url = ['api/company', this.$store.getters.company_id, 'duties'].join('/');
        this.$http.post(url, JSON.stringify(this.duty)).then(response => {
          this.duty = response.body;
        });
      }
    },
    getCustomers: function () {
      var url = ['api/company', this.$store.getters.company_id, 'customers'].join('/');
      this.$http.get(url).then(response => {
        this.customers = response.body;
      })
    },
    getEmployees: function (search, loading) {
      var url = ['api/company', this.$store.getters.company_id, 'employees'].join('/');
      loading(true)
      this.$http.get(url).then(response => {
        this.employees = response.body;
        loading(false)
      })
    }
  }
}
</script>
<style scoped>
.section .section-title {
  justify-content: space-between;
}
</style>