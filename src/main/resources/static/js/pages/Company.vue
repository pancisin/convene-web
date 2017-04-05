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
                  <div class="col-md-4">
                    <img :src="company.logo" class="img-thumbnail"/>
                    <input type="file"
                           @change="onLogoChange"
                           class="form-control"
                           placeholder="Company logo">
  
                  </div>
                  <div class="col-md-8">
                    <input type="text"
                           v-model="company.ico"
                           class="form-control"
                           placeholder="ICO"
                           disabled>
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
    },
    onLogoChange: function (e) {
      var self = this;

      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;

      var file = files[0];

      var image = new Image();
      var reader = new FileReader();

      reader.onload = (e) => {
        self.company.logo = e.target.result;
      };

      reader.readAsDataURL(file);
    }
  }
}
</script>