<template>
  <panel>
    <span slot="title">
      {{ title }}
    </span>
    <div class="timeline-2">
      <div class="time-item" v-for="activity in activities" :key="activity.id">
        <div class="item-info">
          <small class="text-muted">{{ activity.created | moment('from') }}</small>
          <p>
            {{ activity.user }} {{ $t(activity.type.code) }} {{ $t('activity.target.conference') }} {{ activity.created | moment('from') }}.
          </p>
        </div>
      </div>
    </div>
  </panel>
</template>

<script>
export default {
  name: 'latest-activity',
  inject: ['provider'],
  props: ['title'],
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
