<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">Users</div>
        <div class="card-body">
          
        <form class="form form-horizontal" @submit.prevent="submitDuty">
            <div class="section">
                <div class="section-body">
                    <div class="form-group">
                        <label class="col-md-3 control-label">Location</label>
                        <div class="col-md-9">
                        <input type="text" v-model="duty.location" class="form-control" placeholder="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">Start Date</label>
                        <div class="col-md-9">
                        <input type="text" v-model="duty.startDate" class="form-control" placeholder="">
                        </div>
                    </div>

                     <div class="form-group">
                        <label class="col-md-3 control-label">End Date</label>
                        <div class="col-md-9">
                        <input type="text" v-model="duty.endDate" class="form-control" placeholder="">
                        </div>
                    </div>

                    <div class="form-group">
                    <label class="col-md-3 control-label">Employees</label>
                    <div class="col-md-9">
                        <v-select label="firstName" :debounce="250" :value.sync="duty.employees" :on-search="getEmployees" :options="employees" multiple></v-select>
                    </div>
                    </div>
                </div>
            </div>
            <div class="form-footer">
                <div class="form-group">
                <div class="col-md-9 col-md-offset-3">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-default">Cancel</button>
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
 export default {
    name: 'create-duty',
    data: function() {
        return {
            duty: {
                id: null,
                location: null,
                startDate: null,
                endDate: null,
                employees: [],
                tasks: [],
            },
            employees: [],
            items: [
                {
                    id: 4,
                    title: "ahoj",
                },
                {
                    id: 5,
                    title: "Ako sa mas"
                }
            ] 
        }
    },
    methods: {
        submitDuty: function() {
            var url = ['/api/company', this.$store.getters.company_id, 'duties'].join('/');

            this.$http.post(url, JSON.stringify(this.duty)).then(function() {
                alert('success');
            })
        },
        getEmployees: function(search, loading) {
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