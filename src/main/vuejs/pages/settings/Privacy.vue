<template>
  <div class="row">
    <div class="col-md-4 col-md-offset-4">
      <h3>Visible items</h3>
      <hr />

      <div 
        class="form-group" 
        v-for="(rule, index) in rules" 
        :key="index">

        <div class="checkbox checkbox-primary">

          <input 
            :id="`checkbox-${index}`" 
            type="checkbox" 
            checked="checked" 
            value="PUBLIC"
            v-model="constraints[rule]">
          
          <label :for="`checkbox-${index}`">
            {{ rule }}
          </label>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserApi from 'api/user.api';

export default {
  name: 'privacy-settings',
  data () {
    return {
      constraints: []
    };
  },
  created () {
    UserApi.getPrivacyConstraints(constraints => {
      this.constraints = constraints;
    });
  },
  computed: {
    rules () {
      return [
        'attending-events',
        'events'
      ];
    }
  }
};
</script>

<style>

</style>
