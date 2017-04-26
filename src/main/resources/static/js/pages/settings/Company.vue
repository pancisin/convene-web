<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-mini">
        <div class="card-body">
          <form class="form form-horizontal"
                @submit.prevent="submit"
                v-if="company != null">
  
            <div class="row">
              <div class="col-md-4">
                <img :src="company.logo"
                     class="img-thumbnail" />
                <input type="file"
                       @change="onLogoChange"
                       class="form-control"
                       placeholder="Company logo">
  
              </div>
              <div class="col-md-8">
                <div class="section">
                  <div class="section-title">
                    <i class="icon fa fa-info"
                       aria-hidden="true"></i> {{ $t('company.information') }}
                  </div>
                  <div class="section-body">
                    <input type="text"
                           v-model="company.ico"
                           class="form-control"
                           :placeholder="$t('company.ico')"
                           disabled>
                    <input type="text"
                           v-model="company.name"
                           class="form-control"
                           :placeholder="$t('company.name')">
                  </div>
                </div>
  
                <div class="section">
                  <div class="section-title">
                    <i class="icon fa fa-home"
                       aria-hidden="true"></i> {{ $t('company.residence') }}
                  </div>
                  <div class="section-body">
                    <div class="row">
                      <div class="col-xs-10">
                        <input type="text"
                               v-model="company.address.street"
                               class="form-control"
                               :placeholder="$t('company.street')">
                      </div>
                      <div class="col-xs-2">
                        <input type="text"
                               v-model="company.address.number"
                               class="form-control"
                               :placeholder="$t('company.number')">
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-xs-4">
                        <input type="text"
                               v-model="company.address.zip"
                               class="form-control"
                               :placeholder="$t('company.zip')">
                      </div>
                      <div class="col-xs-8">
                        <input type="text"
                               v-model="company.address.city"
                               class="form-control"
                               :placeholder="$t('company.city')">
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
  
            <div class="row">
              <div class="col-xs-12">
                <div class="section">
                  <div class="section-title">
                    <i class="icon fa fa-phone"
                       aria-hidden="true"></i> {{ $t('company.contacts')}}
                  </div>
                  <div class="section-body">
                  </div>
                </div>
  
                <input type="text"
                       v-model="company.email"
                       class="form-control"
                       :placeholder="$t('company.email')">
                <input type="text"
                       v-model="company.phoneNumber"
                       class="form-control"
                       :placeholder="$t('company.phone')">
              </div>
            </div>
  
            <div class="text-center">
              <button type="submit"
                      class="btn btn-primary">
                {{ $t('actions.update') }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  
    <modal :show.sync="display.modal"
           @close="display.modal = false">
      <h4 slot="header">Success !</h4>
      <p slot="body">Your company information have been updated successfuly.</p>
    </modal>
  </div>
</template>

<script>
import Auth from '../../services/auth.js'
export default {
  name: 'company',
  data: function () {
    return {
      company: null,
      users: [],
      display: {
        modal: false,
      }
    }
  },
  created: function () {
    Auth.currentUser(this).then((user) => {
      this.company = user.company;
    });
  },
  methods: {
    submit: function () {
      this.$http.put('api/company/' + this.company.id, this.company).then(response => {
        this.company = response.body;
        this.display.modal = true;
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