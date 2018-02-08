<template>
  <div class="row">
    <div class="col-md-6 col-md-offset-3" v-loading="loading">
      <h3>Visible items</h3>

      <table class="table privacy-table">
        <thead>
          <tr>
            <th>
            </th>
            <th>
              {{ $t('settings.privacy.access.public') }}
            </th>
            <th v-for="access in accessPolicy" :key="access">
              {{ $t(`settings.privacy.access.${access}`) }}
            </th>
            <th>
              Description
            </th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="(rule, index) in rules"
            :key="index">

            <td>
              {{ $t(`settings.privacy.constraint.${rule}`) }}
            </td>
            <td>
              <div class="radio radio-primary">
                <input 
                  id="radio-public" 
                  type="radio" 
                  :checked="constraints[rule] == null"
                  @change="$delete(constraints, rule)">

                <label for="radio-public"></label>
              </div>
            </td>
            <td v-for="access in accessPolicy" :key="access">
              <div class="radio radio-primary">
                <input 
                  :id="`radio-${index}-${access}`" 
                  type="radio" 
                  :checked="constraints[rule] == access.toUpperCase()"
                  @change="$set(constraints, rule, access.toUpperCase());">

                <label :for="`radio-${index}-${access}`"></label>
              </div>
            </td>
            <td>
              {{ $t(`settings.privacy.description.${rule}`) }}
            </td>
          </tr>
        </tbody>
      </table>

      <div class="text-center">
        <button 
          type="button" 
          @click="submit" 
          class="btn btn-default">Save</button>
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
      constraints: [],
      loading: false
    };
  },
  created () {
    this.loading = true;
    UserApi.getPrivacyConstraints(constraints => {
      this.constraints = constraints;
      this.loading = false;
    });
  },
  watch: {
    constraints (newVal) {
      console.log(newVal);
    }
  },
  computed: {
    rules () {
      return [
        'profile',
        'attending-events',
        'events'
      ];
    },
    accessPolicy () {
      return [
        'private', 'friends'
      ];
    }
  },
  methods: {
    submit () {
      this.loading = true;
      UserApi.updatePrivacyConstraints(this.constraints, res => {
        this.constraints = res;
        this.loading = false;
      });
    }
  }
};
</script>

<style lang="less">
.privacy-table {
  th, td {
    text-align: center;
    vertical-align: middle !important; 
  }
}
</style>
