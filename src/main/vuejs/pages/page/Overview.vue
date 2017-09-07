<template>
  <div>
    <div class="row">
      <div class="col-lg-6 col-lg-offset-3">
        <div class="panel panel-default" v-loading="loading_activities">
          <div class="panel-heading">
            <h3 class="panel-title">Latest activity</h3>
          </div>
          <div class="panel-body">
            <table class="table">
              <thead>
                <th>User</th>
              </thead>
              <tbody>
                <tr v-for="activity in activities" :key="activity.id">
                  <td>{{ activity.user }} {{ $t(activity.type.code) }} {{ $t('activity.target.conference') }} {{ activity.created | moment('from') }}.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PageApi from 'api/page.api';
export default {
  name: 'page-overview',
  props: ['page'],
  data () {
    return {
      activities: [],
      loading_activities: false
    };
  },
  watch: {
    '$route': 'getActivity'
  },
  created () {
    this.getActivity();
  },
  methods: {
    getActivity () {
      this.loading_activities = true;
      PageApi.getActivity(this.page.id, activities => {
        this.activities = activities;
        this.loading_activities = false;
      });
    }
  }
};
</script>
