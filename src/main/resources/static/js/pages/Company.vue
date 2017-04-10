<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">
          Company information
        </div>
        <div class="card-body">
          <form class="form form-horizontal"
                @submit.prevent="submit"
                v-if="company != null">
            <div class="section">
              <div class="section-body">
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
                    <input type="text"
                           v-model="company.ico"
                           class="form-control"
                           :placeholder="$t('company.ico')"
                           disabled>
                    <input type="text"
                           v-model="company.name"
                           class="form-control"
                           :placeholder="$t('company.name')">
  
                    <div class="section">
                      <div class="section-title">
                        {{ $t('company.contact') }}
                      </div>
                      <div class="section-body">
                        <input type="text"
                               v-model="company.email"
                               class="form-control"
                               :placeholder="$t('company.email')">
                        <input type="text"
                               v-model="company.phoneNumber"
                               class="form-control"
                               :placeholder="$t('company.phone')">
  
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
  
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
  
            <div class="form-footer">
              <div class="form-group">
                <div class="col-md-9 col-md-offset-4">
                  <button type="submit"
                          class="btn btn-primary">
                    {{ $t('actions.update') }}
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">
          Users
        </div>
        <div class="card-body no-padding">
          <table class="table">
            <thead>
              <tr>
                <th>#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
              </tr>
            </thead>
            <tbody v-for="user in users">
              <tr>
                <th scope="row"
                    v-text="user.id">1</th>
                <td v-text="user.firstName"></td>
                <td v-text="user.lastName"></td>
                <td v-text="user.email"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <modal :show="display.modal" @close="display.modal = false">
      <h4 slot="header">Success !</h4>
      <p slot="body">Your company information have been updated successfuly.</p>
    </modal>
  </div>
</template>

<script>
import Auth from '../services/auth.js'
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
      this.fetchUsers();
    });
  },
  methods: {
    submit: function () {
      this.$http.put('api/company/' + this.company.id, this.company).then(response => {
        this.company = response.body;
        this.display.modal = true;
      });
    },
    fetchUsers: function () {
      this.$http.get('api/company/' + this.company.id + '/users').then(response => {
        this.users = response.body;
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