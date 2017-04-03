 <template>
  <nav class="navbar navbar-default"
       id="navbar">
    <div class="container-fluid">
      <div class="navbar-collapse collapse in">
        <ul class="nav navbar-nav navbar-mobile">
          <li>
            <button type="button"
                    class="sidebar-toggle">
              <i class="fa fa-bars"></i>
            </button>
          </li>
          <li class="logo">
            <a class="navbar-brand"
               href="#">
              <span class="highlight">Flat v3</span> Admin
            </a>
          </li>
          <li>
            <button type="button"
                    class="navbar-toggle">
            </button>
          </li>
        </ul>
        <ul class="nav navbar-nav navbar-left">
          <li class="navbar-title"
              v-if="$store.state.user != null">
            <span class="highlight"
                  v-text="$store.state.user.company.name"></span>
          </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
  
          <li class="dropdown notification">
            <a href="#"
               class="dropdown-toggle"
               data-toggle="dropdown">
              <div class="icon">
                <i class="fa fa-bell"
                   aria-hidden="true"></i>
              </div>
              <div class="title">Unread Messages</div>
              <div class="count">99</div>
            </a>
            <div class="dropdown-menu">
              <ul>
                <li class="dropdown-header">Message</li>
                <li>
                  <a href="#">
                    <span class="badge badge-warning pull-right">10</span>
                    <div class="message">
                      <div class="content">
                        <div class="title">"Payment Confirmation.."</div>
                        <div class="description">Alan Anderson</div>
                      </div>
                    </div>
                  </a>
                </li>
                <li>
                  <a href="#">
                    <span class="badge badge-warning pull-right">5</span>
                    <div class="message">
                      <div class="content">
                        <div class="title">"Hello World"</div>
                        <div class="description">Marco Harmon</div>
                      </div>
                    </div>
                  </a>
                </li>
                <li>
                  <a href="#">
                    <span class="badge badge-warning pull-right">2</span>
                    <div class="message">
                      <img class="profile"
                           src="https://placehold.it/100x100">
                      <div class="content">
                        <div class="title">"Order Confirmation.."</div>
                        <div class="description">Brenda Lawson</div>
                      </div>
                    </div>
                  </a>
                </li>
                <li class="dropdown-footer">
                  <a href="#">View All <i class="fa fa-angle-right" aria-hidden="true"></i></a>
                </li>
              </ul>
            </div>
          </li>
  
          <li class="dropdown profile"
              v-if="$store.state.user != null">
            <a href="#"
               class="dropdown-toggle"
               data-toggle="dropdown">
              <div class="icon">
                <i class="fa fa-cog fa-2x"
                   aria-hidden="true"></i>
              </div>
              <div class="title">Settings</div>
            </a>
  
            <div class="dropdown-menu">
              <div class="profile-info">
                <h4 class="username">{{ $store.state.user.firstName }} {{ $store.state.user.lastName }} <br> <small v-text="$store.state.user.company.name"></small></h4>
              </div>
              <ul class="action">
                <!--<li>
                  <router-link to="/licenses"> License & Billing </router-link>
                </li>-->
                <li>
                  <a href="#"> Setting </a>
                </li>
                <li>
                  <a href="javascript:;"
                     @click="logout"> Logout </a>
                </li>
              </ul>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
  
<script>
import Auth from '../services/auth.js';
export default {
  name: 'header',
  methods: {
    logout: function () {
      Auth.logout(this, '/login')
    }
  }
}
</script>
