<template>
  <div v-loading="loading">
    <div class="row">
      <div class="col-sm-4 col-md-3">
        <div class="form-group">
          <image-upload v-model="user.profilePictureData" :media="user.profilePicture"></image-upload>
        </div>
      </div>
      <div class="col-sm-8 col-md-6">
        <div class="form-group">
          <label>{{ $t('user.email') }}</label>

          <div class="input-group" :class="{ 'has-error' : !user.verified }">
            <span class="input-group-addon">
              @
            </span>
            <input type="email" v-model="user.email" class="form-control" placeholder="Username" disabled>
            <span class="input-group-btn" v-if="!user.verified">
              <a class="btn waves-effect btn-danger">Verify</a>
            </span>
            <span class="input-group-btn" v-else>
              <a class="btn btn-success">
                <i class="fa fa-check"></i>
              </a>
            </span>
          </div>
        </div>
        <div class="form-group">
          <label>{{ $t('user.firstName') }}</label>
          <input 
            type="email" 
            v-model="user.firstName" 
            class="form-control" 
            placeholder="First name">
        </div>
        <div class="form-group">
          <label>{{ $t('user.lastName') }}</label>
          <input 
            type="email" 
            v-model="user.lastName" 
            class="form-control" 
            placeholder="Last name">
        </div>
      </div>
      <div class="col-sm-12 col-md-3">
        <a 
          class="btn btn-default btn-block" 
          @click="showChangePasswordModal = true">
          Change password
        </a>
      </div>
    </div>

    <div class="form-group">
      <label>{{ $t('user.about_me') }}</label>
      <textarea 
        v-model="user.metadata.about" 
        class="form-control" 
        placeholder="About me"
        rows="5">
      </textarea>
    </div>

    <div v-if="user">
      <h3>Billing address</h3>
      <hr />
      <div class="row">
        <div class="form-group col-xs-8">
          <label>{{ $t('user.address.street') }}</label>
          <input type="email" v-model="user.address.street" class="form-control" placeholder="Street">
        </div>
        <div class="form-group col-xs-4">
          <label>{{ $t('user.address.number') }}</label>
          <input type="email" v-model="user.address.number" class="form-control" placeholder="Number">
        </div>
      </div>
      <div class="row">
        <div class="form-group col-xs-6">
          <label>{{ $t('user.address.zip') }}</label>
          <input type="email" v-model="user.address.zip" class="form-control" placeholder="Zip code">
        </div>
        <div class="form-group col-xs-6">
          <label>{{ $t('user.address.city') }}</label>
          <input type="email" v-model="user.address.city" class="form-control" placeholder="City">
        </div>
      </div>

      <div class="form-group">
        <label>{{ $t('user.address.state') }}</label>
        <input type="email" v-model="user.address.state" class="form-control" placeholder="State">
      </div>
    </div>
  
    <div class="text-right m-b-20">
      <a class="btn btn-primary" @click="submit">
        Save
      </a>
    </div>

    <modal :show.sync="showChangePasswordModal">
      <span slot="header">Change password</span>
      <div slot="body">
        <div class="row">
          <div class="col-md-6 col-md-offset-3">
            <password-change @updated="showChangePasswordModal = false" />
          </div>
        </div>
      </div>
    </modal>
  </div>
</template>

<script>
import {
  mapGetters,
  mapActions
} from 'vuex';

import {
  ImageUpload,
  PasswordChange
} from 'elements';

export default {
  name: 'account',
  data () {
    return {
      showChangePasswordModal: false,
      loading: false
    };
  },
  computed: {
    ...mapGetters(['user'])
  },
  components: {
    ImageUpload,
    PasswordChange
  },
  methods: {
    ...mapActions([
      'updateUser'
    ]),
    submit () {
      var data = {
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        address: this.user.address,
        profilePictureData: this.user.profilePictureData,
        metadata: this.user.metadata
      };

      this.loading = true;
      this.updateUser(data).then(user => {
        this.$success('notification.account.updated');
        this.loading = false;
      });
    }
  }
};
</script>