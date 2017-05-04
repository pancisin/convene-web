  <template>
  <div class="container">
  
    <!-- Page-Title -->
    <div class="row">
      <div class="col-sm-12">
        <div class="page-title-box">
          <ol class="breadcrumb pull-right">
            <li><a href="#">Minton</a></li>
            <li><a href="#">UI Kit</a></li>
            <li class="active">Panels</li>
          </ol>
          <h4 class="page-title">Panels</h4>
        </div>
      </div>
    </div>
  
    <div class="row">
      <div class="col-lg-4">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Panel Default</h3>
            <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
          </div>
          <div class="panel-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="panel panel-color panel-primary">
          <div class="panel-heading">
            <h3 class="panel-title">Panel Primary</h3>
            <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
          </div>
          <div class="panel-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="panel panel-color panel-success">
          <div class="panel-heading">
            <h3 class="panel-title">Panel Success</h3>
            <p class="panel-sub-title font-13 text-muted">Sub title goes here with small font</p>
          </div>
          <div class="panel-body">
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
          </div>
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
