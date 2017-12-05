<template>
  <form class="form" @submit.prevent="submit">
    <div class="form-group" :class="{ 'has-error' : errors.has('currentPassword') }">
      <label>Current password</label>
      <input 
        name="currentPassword" 
        type="password" 
        class="form-control" 
        v-model.trim="currentPassword" 
        v-validate 
        :data-vv-rules="'required'"
      >
      <span class="text-danger" v-if="errors.has('currentPassword')">
        {{ errors.first('currentPassword') }}
      </span>
    </div>
    <div class="form-group" :class="{ 'has-error' : errors.has('newPassword') }">
      <label>New password</label>
      <input 
        name="newPassword"
        type="password" 
        class="form-control" 
        v-model.trim="newPassword"
        v-validate 
        :data-vv-rules="'required'"
      >
      <span class="text-danger" v-if="errors.has('newPassword')">
        {{ errors.first('newPassword') }}
      </span>
    </div>
    <div class="form-group" :class="{ 'has-error' : errors.has('confirmPassword') }">
      <label>Confirm password</label>
      <input
        name="confirmPassword" 
        type="password" 
        class="form-control"
        v-validate
        data-vv-rules="required|confirmed:newPassword"
      >
      <span class="text-danger" v-if="errors.has('confirmPassword')">
        {{ errors.first('confirmPassword') }}
      </span>
    </div>

    <div class="text-right">
      <button type="submit" class="btn btn-primary">Update</button>
    </div>
  </form>
</template>

<script>
import UserApi from 'api/user.api';
export default {
  name: 'password-change',
  data () {
    return {
      currentPassword: null,
      newPassword: null
    };
  },
  methods: {
    submit () {
      this.$validator.validateAll().then(valid => {
        if (valid) {
          UserApi.changePassword({
            currentPassword: this.currentPassword,
            newPassword: this.newPassword
          }, user => {
            this.$emit('updated', user);
          });
        }
      });
    }
  }
};
</script>
