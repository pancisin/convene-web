<template>
  <div class="row">
    <div class="col-md-6 col-md-offset-3" v-loading="loading">
      <h3>Visible items</h3>

      <table class="table privacy-table">
        <thead>
          <tr>
            <th>
            </th>
            <th v-for="access in accessPolicy" :key="access.name">
              {{ access.label }}
            </th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="(rule, index) in rules"
            :key="index">

            <td>
              {{ rule }}
            </td>
            <td v-for="access in accessPolicy" :key="access.name">
              <div class="radio radio-primary">
                <input 
                  :id="`radio-${index}-${access.name}`" 
                  type="radio" :value="access.name" 
                  v-model="constraints[rule]">

                <label :for="`radio-${index}-${access.name}`"></label>
              </div>
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
        {
          name: 'PUBLIC',
          label: 'public'
        },
        {
          name: 'PRIVATE',
          label: 'private'
        },
        {
          name: 'FRIENDS',
          label: 'friends'
        }
      ];
    }
  },
  methods: {
    submit () {
      this.loading = true;
      UserApi.updatePrivacyConstraints(this.constraints, res => {
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
