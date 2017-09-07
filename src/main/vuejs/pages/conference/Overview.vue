<template>
  <div>
    <div class="row">
      <div class="col-lg-6 col-lg-offset-3">
        <div class="panel panel-default">
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
import ConferenceApi from 'api/conference.api';
export default {
  name: 'dashboard',
  props: ['conference'],
  data () {
    return {
      activities: []
    };
  },
  watch: {
    '$route': 'getActivities'
  },
  created () {
    this.getActivities();
  },
  methods: {
    getActivities () {
      ConferenceApi.getActivities(this.conference.id, activities => {
        this.activities = activities;
        this.activities.sort((a, b) => {
          if (a.created === b.created) return 0;
          return a.created < b.created ? 1 : -1;
        });
      });
    }
  }
};
</script>
