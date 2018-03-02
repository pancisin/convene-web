<template>
  <form class="form" @submit.prevent="submit">
    <vue-input
      name="currentPassword"
      type="password" 
      label="Current password"
      v-model.trim="currentPassword" />

    <vue-input
      name="newPassword"
      type="password"
      label="New Password"
      v-model.trim="newPassword" />

    <!-- <vue-input
      name="confirmPassword"
      type="password"
      label="Confirm password"
      rules="required|confirmed:newPassword" /> -->

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
      <button 
        type="submit" 
        class="btn btn-primary">
        Update
      </button>
    </div>
  </form>
</template>

<script>
import { VueInput } from 'elements';
import UserApi from 'api/user.api';
export default {
  name: 'password-change',
  data () {
    return {
      currentPassword: null,
      newPassword: null
    };
  },
  components: {
    VueInput
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
