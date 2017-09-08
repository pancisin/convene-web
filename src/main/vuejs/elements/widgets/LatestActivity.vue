<template>
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
</template>

<script>
export default {
  name: 'latest-activity',
  inject: ['provider'],
  data () {
    return {
      activities: []
    };
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  watch: {
    'api': 'getActivities'
  },
  created () {
    this.getActivities();
  },
  methods: {
    getActivities () {
      this.api.getActivities(activities => {
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
