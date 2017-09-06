  <template>
  <div>
    <div class="row">
      <div class="col-lg-4 col-lg-offset-4">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Latest activity</h3>
          </div>
          <div class="panel-body">
            <table class="table">
              <thead>
                <th>User</th>
                <th>Type</th>
                <th>Time</th>
              </thead>
              <tbody>
                <tr v-for="activity in activities" :key="activity.id">
                  <td>{{activity.user}}</td>
                  <td>{{activity.type.name}}</td>
                  <th>{{activity.created | moment('L LT') }}</th>
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
      });
    }
  }
};
</script>
