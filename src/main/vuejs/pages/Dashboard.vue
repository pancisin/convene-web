  <template>
  <div>
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
  
    <div class="row">
      <div class="col-xs-12">
        <tab-container>
          <tab title="Contenttab1">
            <p>Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt.Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim.</p>
            <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim.</p>
          </tab>
          <tab title="TAb2">
            Tab 2
          </tab>
          <tab title="tab3">
            Tab 3
          </tab>
        </tab-container>
      </div>
    </div>
  </div>
</template>
  
<script>
import later from 'later'
import calendar from '../elements/Calendar.vue'
import TabContainer from '../elements/TabContainer.vue'
import Tab from '../elements/Tab.vue'

export default {
  name: 'dashboard',
  components: {
    calendar, TabContainer, Tab
  },
}
</script>
