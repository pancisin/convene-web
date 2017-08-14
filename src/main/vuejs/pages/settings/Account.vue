<template>
  <div class="row">
    <div class="col-md-6">
      <panel type="primary">
        <span slot="title">Information</span>
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
          <input type="email" v-model="user.firstName" class="form-control" placeholder="First name">
        </div>
        <div class="form-group">
          <label>{{ $t('user.lastName') }}</label>
          <input type="email" v-model="user.lastName" class="form-control" placeholder="Last name">
        </div>
      </panel>
    </div>
    <div class="col-md-6">
      <panel type="default">
        <span slot="title">Billing address</span>
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
      </panel>
    </div>
  
    <div class="text-center col-xs-12">
      <a class="btn btn-rounded btn-primary" @click="submit">Update</a>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'account',
  created () {

  },
  computed: {
    ...mapGetters(['user'])
  },
  methods: {
    ...mapActions([
      'updateUser'
    ]),
    submit () {
      var data = {
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        address: this.user.address
      };

      this.$http.put('api/user', data).then(response => {
        var user = response.body;
        this.updateUser(user);
        this.$success('Success !', 'User account information has been updated.');
      });
    }
  }
};
</script>