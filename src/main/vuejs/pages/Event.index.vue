<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="table">
        <span slot="title">My events</span>
  
        <table class="table table-striped">
          <thead>
            <tr>
              <th>name</th>
              <th class="text-center">date</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in paginator.content">
              <td>
                <router-link :to="'event/' + event.id">
                  {{ event.name }}
                </router-link>
              </td>
              <td class="text-center">{{ event.date | moment('DD.MM.YYYY') }}</td>
            </tr>
            <tr v-if="paginator.content.length == 0">
              <td colspan="2" class="text-center">There's nothing to display.</td>
            </tr>
          </tbody>
        </table>
  
        <div class="text-center">
          <ul class="pagination">
            <li v-if="!paginator.first">
              <a @click="paginatorNavigate(-1)">
                <i class="fa fa-angle-left"></i>
              </a>
            </li>
            <li v-for="(page, index) in paginator.totalPages" :class="{'active' : paginator.number == index}">
              <a v-text="page" @click="getEvents(index)"></a>
            </li>
            <li v-if="!paginator.last">
              <a @click="paginatorNavigate(1)">
                <i class="fa fa-angle-right"></i>
              </a>
            </li>
          </ul>
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
export default {
  name: 'event-index',
  data() {
    return {
      paginator: {},
    }
  },
  created: function () {
    this.getEvents(0);
  },
  methods: {
    getEvents(page) {
      var size = 5;
      var url = ['api/user/event', page, size].join('/');
      this.$http.get(url).then(response => {
        this.paginator = response.body;
        this.events = response.body.content
      })
    },
    paginatorNavigate(direction) {
      this.getEvents(this.paginator.number + direction)
    }
  }
}
</script>