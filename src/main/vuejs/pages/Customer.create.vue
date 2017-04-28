<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-mini">
        <div class="card-header">{{ $t('customer.create') }}</div>
        <div class="card-body">
          <form class="form form-horizontal"
                @submit.prevent="submitCustomer">
  
            <div class="row">
              <div class="col-md-6">
                <input type="text"
                       v-model="customer.name"
                       class="form-control"
                       :placeholder="$t('customer.name')">
                <input type="text"
                       v-model="customer.address"
                       class="form-control"
                       :placeholder="$t('customer.address')">
                <textarea v-model="customer.description"
                          class="form-control"
                          :placeholder="$t('customer.description')"></textarea>
              </div>
              <div class="col-md-6">
                <div class="section">
                  <div class="section-title">
                    {{ $t('customer.person.default') }}
                  </div>
                  <div class="section-body">
                    <input type="text"
                           v-model="customer.person"
                           class="form-control"
                           :placeholder="$t('customer.person.name')">
                    <input type="email"
                           v-model="customer.email"
                           class="form-control"
                           :placeholder="$t('customer.person.email')">
                    <div class="checkbox">
                      <input type="checkbox"
                             v-model="customer.report.active"
                             id="report-active-checkbox">
                      <label for="report-active-checkbox">
                        {{ $t('customer.report.active') }}
                      </label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
  
            <div class="section"
                 v-if="customer.report.active">
              <div class="section-title">
                {{ $t('customer.report.default') }}
              </div>
              <div class="section-body">
                <div class="row">
                  <div class="col-md-2">
                    <div class="radio"
                         v-for="period in periods">
                      <input type="radio"
                             :id="'period-radio-' + period"
                             v-model="customer.report.interval"
                             :value="period">
                      <label :for="'period-radio-' + period">
                        {{ $t('period.' + period) }}
                      </label>
                    </div>
                  </div>
                  <div class="col-md-10">
                    <recurrence-input :recurrence="customer.report.recurrence"></recurrence-input>
                  </div>
                </div>
              </div>
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
import recurrenceInput from '../elements/RecurrenceInput.vue'

export default {
  name: 'create-customer',
  data: function () {
    return {
      customer: {
        name: null,
        person: null,
        email: null,
        address: null,
        description: null,
        report: null,
      },
      periods: [],
    }
  },
  components: {
    recurrenceInput: recurrenceInput,
  },
  computed: {
    edit_mode: {
      get: function () {
        return this.customer.id != null;
      }
    },
  },
  created: function () {
    var customer_id = this.$route.params.id;
    if (customer_id != null) {
      this.$http.get('api/customer/' + customer_id).then(response => {
        this.customer = response.body;
      })
    };

    if (this.customer.report == null)
      this.customer.report = {
        active: false,
        recurrence: {
          minute: [0],
          hour: [0],
          day: [],
          month: [],
          dayOfWeek: [],
          weekOfMonth: []
        },
      }
    this.fetchPeriods();
  },
  methods: {
    submitCustomer: function () {
      if (this.edit_mode) {
        var url = ['api/customer', this.customer.id].join('/');
        this.$http.put(url, JSON.stringify(this.customer)).then(response => {
          this.customer = response.body;
        });
        this.$router.go(-1);
      } else {
        var url = ['api/company', this.$store.getters.company_id, 'customers'].join('/');
        this.$http.post(url, JSON.stringify(this.customer)).then(response => {
          this.customer = response.body;
        });
      }
    },
    fetchPeriods: function () {
      this.$http.get('api/report/periods').then(response => {
        this.periods = response.body;
      });
    }
  }
}
</script>