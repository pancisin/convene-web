<template>
  <div>
    <ul class="list-unstyled p-0">
      <li v-for="(date, key) in sorted_events" :key="key">
        <h4>{{ key }}</h4>

        <ul class="events-list">
          <li v-for="event in date" :key="event.id">
            <router-link :to="{ name: 'event.public', params: { id: event.id } }">

              <img :src="event.poster.path" v-if="event.poster != null">
              <div class="event-content" :class="{ 'detail' : event.poster == null }">
                <h4>
                  {{ event.name }}
                </h4>
                <small class="text-muted">
                  {{ event.date | luxon('ff') }} 
                  <!-- - {{ event.startsAt }} -->
                </small>
                <p v-if="event.summary != null" v-strip="event.summary.substring(0, 200)">
                </p>
              </div>
            </router-link>
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'events-list',
  props: {
    events: Array
  },
  data () {
    return {
      sorted_events: []
    };
  },
  watch: {
    events (newVal) {
      let data = {};
      newVal.forEach(e => {
        const key = parseInt(e.date, 10);
        if (data[key] == null) {
          data[key] = [];
        }

        data[key].push(e);
      });

      this.sorted_events = data;
    }
  }
};
</script>

<style lang="less">
ul.events-list {
  list-style-type: none;
  margin: 0;
  padding: 0;

  li {
    a {
      color: #fff;
    }

    &~li {
      margin-top: 2px;
    }

    h4 {
      margin: 0;
    }

    height: 150px;
    overflow: hidden;
    position: relative;
    img {
      width: 100%;
    }

    &:hover .event-content,
    .event-content.detail {
      top: 0;
      opacity: 1;

      small,
      p {
        opacity: 1;
      }
    }

    .event-content {
      background: #333;
      position: absolute;
      bottom: 0;
      width: 100%;
      opacity: .8;
      padding: 10px;
      transition: all .5s ease;
      top: 110px;

      small,
      p {
        opacity: 0;
        transition: all 1s ease;
      }

      p {
        font-size: 12px;
        color: #bbb;
      }

      h4 {
        color: #fff;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
      }
    }
  }
}
</style>
