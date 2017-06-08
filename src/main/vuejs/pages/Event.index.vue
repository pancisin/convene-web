<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="table" v-loading="loading">
        <span slot="title">My events</span>
  
        <table class="table table-striped">
          <thead>
            <tr>
              <th>name</th>
              <th class="text-center">date</th>
            </tr>
          </thead>
          <tbody is="transition-group" name="fade">
            <tr v-for="event in paginator.content" :key="event.id">
              <td>
                <router-link :to="'event/' + event.id">
                  {{ event.name }}
                </router-link>
              </td>
              <td class="text-center">{{ event.date | moment('DD.MM.YYYY') }}</td>
            </tr>
            <tr v-if="paginator.content != null && paginator.content.length == 0">
              <td colspan="2" class="text-center">There's nothing to display.</td>
            </tr>
          </tbody>
        </table>
  
        <div class="text-center">
          <Paginator :paginator="paginator" @navigate="paginatorNavigate" />
        </div>
  
        <div class="text-center">
          <router-link to="/admin/event/create" class="btn btn-default btn-rounded text-center">
            Create event
          </router-link>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import Paginator from '../elements/Paginator.vue'
export default {
  name: 'event-index',
  data() {
    return {
      paginator: {},
      loading: false,
    }
  },
  created: function () {
    this.getEvents(0);
  },
  components: {
    Paginator
  },
  methods: {
    getEvents(page) {
      this.loading = true;
      var size = 5;
      var url = ['api/user/event', page, size].join('/');
      this.$http.get(url).then(response => {
        this.loading = false;
        this.paginator = response.body;
        this.events = response.body.content
      })
    },
    paginatorNavigate(e) {
      if (e.direction != null) {
        this.getEvents(this.paginator.number + e.direction)
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    }
  }
}
</script>