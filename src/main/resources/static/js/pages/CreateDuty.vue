<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-tab card-mini">
        <div class="card-header">
          <ul class="nav nav-tabs">
            <li role="tab1"
                v-for="tab in tabs"
                :class="{ 'active' : currentTab == tab.name }">
              <a aria-controls="tab1"
                 @click="currentTab = tab.name"
                 role="tab"
                 data-toggle="tab"
                 aria-expanded="false">{{ $t(tab.title) }}</a>
            </li>
          </ul>
        </div>
  
        <div class="card-body">
          <form class="form form-horizontal"
                @submit.prevent="submitDuty">
  
            <transition-group name="fade"
                              mode="out-in">
              <div class="row"
                   v-show="currentTab == 'duty'"
                   key="duty">
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
  
              <div v-show="currentTab == 'tasks'"
                   key="tasks">
  
                <table class="table table-striped"
                       v-if="attributes != null">
                  <thead>
                    <tr>
                      <th v-for="attr in attributes"
                          v-text="attr.name">
                      </th>
                      <th>{{ $t('duty.task.note') }}</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody is="transition-group"
                         name="fade">
                    <tr v-for="t in tasks"
                        :key="t.id">
                      <td v-for="attr in attributes">
                        <ul v-if="getTaskAttribute(t, attr.id) != null"
                            class="list-unstyled">
                          <li v-for="option in getTaskAttribute(t, attr.id).values"
                              v-text="option.value"></li>
                        </ul>
                      </td>
                      <td v-text="t.note">
                      </td>
                      <td>
                        <a @click="deleteTask(t)">{{ $t('actions.remove') }}</a>
                        <!--<a @click=""
                           class="btn btn-link">edit</a>-->
                      </td>
                    </tr>
                  </tbody>
                </table>
  
                <div class="section">
                  <div class="section-title">{{ $t('duty.task.add') }}</div>
  
                  <table class="table">
                    <thead>
                      <tr>
                        <th v-for="attr in attributes"
                            v-text="attr.name">
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td v-for="(attr, index) in attributes">
                          <v-checkbox v-for="opt in attr.options"
                                      :option="opt"
                                      v-model="task.attributes[index].values" />
                        </td>
                      </tr>
                    </tbody>
                  </table>
  
                  <label>{{ $t('duty.task.note') }}</label>
                  <input type="text"
                         v-model="task.note"
                         class="form-control" />
                  <div class="text-right">
                    <a @click="submitTask"
                       class="btn btn-primary">{{ $t('duty.task.add') }}</a>
                  </div>
                </div>
              </div>
  
              <div v-show="currentTab == 'recurrence'"
                   key="recurrence">
                <recurrence-input :recurrence="duty.recurrence"></recurrence-input>
              </div>
  
              <div v-show="edit_mode && currentTab == 'employees'"
                   key="employees">
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
            </transition-group>
  
            <div class="form-footer">
              <div class="form-group text-center">
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
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import datepicker from '../elements/DatePicker.vue'
import recurrenceInput from '../elements/RecurrenceInput.vue'
import vCheckbox from '../elements/Checkbox.vue'

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
        recurrence: {
          minute: [0],
          hour: [0],
          day: [],
          month: [],
          dayOfWeek: [],
          weekOfMonth: []
        },
      },
      attributes: [],
      task: null,
      tasks: [],
      customers: [],
      employees: [],
      tabs: [
        {
          name: 'duty',
          title: 'duty.default'
        },
        {
          name: 'tasks',
          title: 'duty.tasks'
        },
        {
          name: 'recurrence',
          title: 'duty.recurrence'
        },
        {
          name: 'employees',
          title: 'duty.employees'
        }
      ],
      currentTab: 'duty',
    }
  },
  components: {
    datepicker, recurrenceInput, vCheckbox
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
    this.getAttributes();

    var duty_id = this.$route.params.id;
    if (duty_id != null) {
      this.$http.get('api/duty/' + duty_id).then(response => {
        this.duty = response.body;
        this.getTasks();
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
    submitTask: function () {
      var url = ['api/duty', this.duty.id, 'tasks'].join('/');

      this.$http.post(url, JSON.stringify(this.task)).then(response => {
        this.tasks.push(response.body);
        this.initializeTask();
      });
    },
    deleteTask: function (task) {
      var url = ['api/task', task.id].join('/');

      this.$http.delete(url).then(response => {
        this.tasks = this.tasks.filter(t => {
          return t.id != task.id;
        })
      });
    },
    getCustomers: function () {
      var url = ['api/company', this.$store.getters.company_id, 'customers'].join('/');
      this.$http.get(url).then(response => {
        this.customers = response.body;
      })
    },
    getAttributes: function () {
      var url = ['api/company', this.$store.getters.company_id, 'attributes'].join('/');
      this.$http.get(url).then(response => {
        this.attributes = response.body;
        this.initializeTask();
      })
    },
    initializeTask: function () {
      this.task = {
        attributes: [],
        note: null,
      }

      this.attributes.forEach(x => {
        this.task.attributes.push({
          id: x.id,
          name: x.name,
          values: []
        });
      })
    },
    getEmployees: function (search, loading) {
      var url = ['api/company', this.$store.getters.company_id, 'employees'].join('/');
      loading(true)
      this.$http.get(url).then(response => {
        this.employees = response.body;
        loading(false)
      })
    },
    getTasks: function () {
      var url = ['api/duty', this.duty.id, 'tasks'].join('/');

      this.$http.get(url).then(response => {
        this.tasks = response.body;
      })
    },
    getTaskAttribute: function (task, id) {
      return task.attributes.find(x => {
        return x.id == id;
      });
    }
  }
}
</script>
<style scoped>
.section .section-title {
  justify-content: space-between;
}
</style>