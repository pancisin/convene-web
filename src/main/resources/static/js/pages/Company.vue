<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-body">
          <form class="form form-horizontal"
                @submit.prevent="submit"
                v-if="company != null">
            <div class="section">
              <div class="section-title">
                Company information
              </div>
              <div class="section-body">
                <div class="row">
                  <div class="col-md-6">
                    <input type="text"
                           v-model="company.ico"
                           class="form-control"
                           placeholder="ICO"
                           disabled>
                  </div>
                  <div class="col-md-6">
                    <input type="text"
                           v-model="company.name"
                           class="form-control"
                           placeholder="Company name">
                  </div>
                </div>
              </div>
            </div>
  
            <div class="form-footer">
              <div class="form-group">
                <div class="col-md-9 col-md-offset-3">
                  <button type="submit"
                          class="btn btn-primary">
                    Update
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
import Auth from '../services/auth.js'
export default {
  data: function () {
    return {
      user: null,
      company: null,
    }
  },
  watch: {
    user: function () {
      this.company = this.user.company;
    }
  },
  created: function () {
    Auth.currentUser(this);
  },
  methods: {
    submit: function () {
      this.$http.put('api/company/' + this.company.id, this.company).then(response => {
        this.company = response.body;
      });
    }
  }
}
</script>