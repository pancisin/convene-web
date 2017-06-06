<template>
  <div class="row">
    <div class="col-md-6">
      <panel type="primary">
        <span slot="title">Information</span>
        <div class="form-group">
          <label>{{ $t('user.email') }}</label>
          <input type="email" v-model="user.email" class="form-control" placeholder="Enter email" disabled>
        </div>
        <div class="form-group">
          <label>{{ $t('user.firstName') }}</label>
          <input type="email" v-model="user.firstName" class="form-control" placeholder="First name">
        </div>
        <div class="form-group">
          <label>{{ $t('user.lastName') }}</label>
          <input type="email" v-model="user.lastName" class="form-control" placeholder="Last name">
        </div>
  
        <div class="text-center">
          <a class="btn btn-rounded btn-primary" @click="submit">Update</a>
        </div>
      </panel>
    </div>
    <div class="col-md-6">
      <panel type="default">
        <span slot="title">Settings</span>
      </panel>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
export default {
  name: 'account',
  created() {

  },
  computed: {
    ...mapGetters({
      user: 'getUser'
    }),
  },
  methods: {
    ...mapActions([
      'updateUser'
    ]),
    submit() {
      var data = {
        firstName: this.user.firstName,
        lastName: this.user.lastName
      }

      this.$http.put('api/user', data).then(response => {
        var user = response.body;
        this.updateUser(user);
        this.$success('Success !','User account information has been updated.')
      })
    }
  }
}
</script>