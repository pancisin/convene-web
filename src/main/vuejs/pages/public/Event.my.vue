<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4">
        <panel type="table">
          <span slot="title">My events</span>
  
          <table class="table table-striped">
            <thead>
              <tr>
                <th>name</th>
                <th class="text-center">date</th>
                <th class="text-center">Action</th>
              </tr>
            </thead>
            <tbody is="transition-group" name="fade">
              <tr v-for="event in paginator.content" :key="event.id">
                <td>
                  <router-link :to="'event/' + event.id">
                    {{ event.name }}
                  </router-link>
                </td>
                <td class="text-center">{{ event.date | moment('L') }}</td>
                <td class="text-center">
                  <router-link :to="'/create-event/' + event.id" class="btn btn-xs btn-warning btn-rounded">
                    <i class="fa fa-pencil"></i>
                  </router-link>
                </td>
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
  </div>
</template>

<script>
import { Paginator } from 'elements';
export default {
  name: 'event-index',
  data () {
    return {
      paginator: {}
    };
  },
  created: function () {
    this.getEvents(0);
  },
  components: {
    Paginator
  },
  methods: {
    getEvents (page) {
      var size = 5;
      var url = ['api/user/event', page, size].join('/');
      this.$http.get(url).then(response => {
        this.paginator = response.body;
        this.events = response.body.content;
      });
    },
    paginatorNavigate (e) {
      if (e.direction != null) {
        this.getEvents(this.paginator.number + e.direction);
      } else if (e.page != null) {
        this.getEvents(e.page);
      }
    }
  }
};
</script>