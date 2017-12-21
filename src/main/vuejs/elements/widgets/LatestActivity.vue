<template>
  <panel v-loading="loading">
    <span slot="title">
      {{ title }}
    </span>
    <div class="timeline-2">
      <div class="time-item" v-for="activity in paginator.content" :key="activity.id">
        <div class="item-info">
          <small class="text-muted">{{ activity.created | moment('from') }}</small>
          <p>
            {{ activity.user }} {{ $t(activity.type.code) }} {{ $t(`activity.target.${parent}`) }} {{ activity.created | moment('from') }}.
          </p>
        </div>
      </div>
    </div>

    <div class="text-center" slot="footer">
      <paginator :fetch="getActivities" :paginator="paginator" />
    </div>
  </panel>
</template>

<script>
import Paginator from '../Paginator';

export default {
  name: 'latest-activity',
  inject: ['provider'],
  props: {
    title: String,
    parent: {
      validator (value) {
        return ['event', 'page', 'conference'].includes(value);
      },
      default () {
        return 'page';
      }
    }
  },
  data () {
    return {
      paginator: {},
      loading: false
    };
  },
  components: {
    Paginator
  },
  computed: {
    api () {
      if (this.provider != null) {
        return this.provider.api;
      }
    }
  },
  methods: {
    getActivities (page) {
      this.loading = true;
      this.api.getActivities(page, 10, paginator => {
        this.paginator = paginator;
        this.loading = false;
      });
    }
  }
};
</script>
