  <template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card card-mini">
        <div class="card-body">
          <calendar></calendar>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import later from 'later'
import calendar from '../elements/Calendar.vue'

export default {
  name: 'dashboard',
  components: {
    calendar,
  },
}
</script>
