<template>
  <div id="wrapper">
    <toast-container></toast-container>
    <header-component></header-component>
  
    <div class="left side-menu">
      <div id="sidebar-menu">
        <ul>
          <li class="menu-title">{{ $t('admin.menu.main') }}</li>
  
          <li>
            <router-link to="/admin/dashboard" class="waves-effect">
              <i class="material-icons">dashboard</i>
              <span>{{ $t('admin.menu.dashboard') }}</span>
            </router-link>
          </li>
  
          <li>
            <router-link to="/admin/event" class="waves-effect">
              <i class="material-icons">event</i>
              <span>{{ $t('admin.menu.events') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.pages') }}</li>
  
          <li v-for="page in pages">
            <router-link :to="'/admin/page/' + page.id" class="waves-effect">
              <i class="material-icons">work</i>
              <span v-text="page.name"></span>
            </router-link>
          </li>
  
          <li>
            <router-link to="/admin/page/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.page_create') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.conferences') }}
            <span class="label label-warning pull-right">Enterprise</span>
          </li>
  
          <li v-for="conference in conferences">
            <router-link :to="'/admin/conference/' +conference.id" class="waves-effect">
              <i class="material-icons">work</i>
              <span v-text="conference.name"></span>
            </router-link>
          </li>
  
          <li>
            <router-link to="/admin/conference/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.conference_create') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.about') }}</li>
  
          <li>
            <router-link to="/admin/faq" class="waves-effect">
              <i class="fa fa-question" aria-hidden="true"></i>
              <span>{{ $t('admin.menu.faq') }}</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/terms" class="waves-effect">
              <i class="material-icons">lock</i>
              <span>{{ $t('admin.menu.terms') }}</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/privacy-policy" class="waves-effect">
              <i class="material-icons">lock</i>
              <span>{{ $t('admin.menu.privacy') }}</span>
            </router-link>
          </li>
          <!--<li>
            <router-link to="/contact" class="waves-effect">
              <i class="material-icons">business</i>
              <span>{{ $t('admin.menu.contact') }}</span>
            </router-link>
          </li>-->
  
        </ul>
        <div class="clearfix"></div>
      </div>
  
      <div class="clearfix"></div>
  
      <div class="user-detail" v-if="$store.state.user != null">
        <div class="dropup">
          <a href="" class="dropdown-toggle profile" data-toggle="dropdown" aria-expanded="true">
            <img src="https://static1.squarespace.com/static/56ba4348b09f95db7f71a726/t/58d7f267ff7c50b172895560/1490547315597/justin.jpg" alt="user-img" class="img-circle">
            <span class="user-info-span">
              <h5 class="m-t-0 m-b-0">{{ $store.state.user.firstName }} {{ $store.state.user.lastName }}</h5>
              <p class="text-muted m-b-0">
                <small>
                  <i class="fa fa-circle text-success"></i>
                  <span>Online</span>
                </small>
              </p>
            </span>
          </a>
          <ul class="dropdown-menu">
            <li>
              <a href="javascript:void(0)">
                <i class="md md-face-unlock"></i> Profile</a>
            </li>
            <li>
              <a href="javascript:void(0)">
                <i class="md md-settings"></i> Settings</a>
            </li>
            <li>
              <a href="javascript:void(0)">
                <i class="md md-lock"></i> Lock screen</a>
            </li>
            <li>
              <a href="javascript:void(0)">
                <i class="md md-settings-power"></i> Logout</a>
            </li>
          </ul>
  
        </div>
      </div>
    </div>
  
    <div class="content-page">
      <div class="content">
        <transition name="fade-down" mode="out-in">
          <router-view></router-view>
        </transition>
        <footer-component></footer-component>
      </div>
    </div>
  
    <div class="side-bar right-bar">
      <div class="nicescroll" style="overflow: hidden; outline: none;" tabindex="5001">
        <ul class="nav nav-tabs tabs" style="width: 100%;">
          <li class="active tab" style="width: 33.3333%;">
            <a href="#home-2" data-toggle="tab" aria-expanded="false" class="active">
              <span class="visible-xs">
                <i class="fa fa-home"></i>
              </span>
              <span class="hidden-xs">Activity</span>
            </a>
          </li>
          <li class="tab" style="width: 33.3333%;">
            <a href="#profile-2" data-toggle="tab" aria-expanded="false">
              <span class="visible-xs">
                <i class="fa fa-user"></i>
              </span>
              <span class="hidden-xs">Chat</span>
            </a>
          </li>
          <li class="tab" style="width: 33.3333%;">
            <a href="#messages-2" data-toggle="tab" aria-expanded="true">
              <span class="visible-xs">
                <i class="fa fa-envelope-o"></i>
              </span>
              <span class="hidden-xs">Settings</span>
            </a>
          </li>
          <div class="indicator" style="right: 181px; left: 0px;"></div>
        </ul>
        <div class="tab-content">
          <div class="tab-pane fade in active" id="home-2">
            <div class="timeline-2">
              <div class="time-item">
                <div class="item-info">
                  <small class="text-muted">5 minutes ago</small>
                  <p>
                    <strong>
                      <a href="#" class="text-info">John Doe</a>
                    </strong> Uploaded a photo
                    <strong>"DSC000586.jpg"</strong>
                  </p>
                </div>
              </div>
  
              <div class="time-item">
                <div class="item-info">
                  <small class="text-muted">30 minutes ago</small>
                  <p>
                    <a href="" class="text-info">Lorem</a> commented your post.</p>
                  <p>
                    <em>"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam laoreet tellus ut tincidunt euismod. "</em>
                  </p>
                </div>
              </div>
  
              <div class="time-item">
                <div class="item-info">
                  <small class="text-muted">59 minutes ago</small>
                  <p>
                    <a href="" class="text-info">Jessi</a> attended a meeting with
                    <a href="#" class="text-success">John Doe</a>.</p>
                  <p>
                    <em>"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam laoreet tellus ut tincidunt euismod. "</em>
                  </p>
                </div>
              </div>
  
              <div class="time-item">
                <div class="item-info">
                  <small class="text-muted">1 hour ago</small>
                  <p>
                    <strong>
                      <a href="#" class="text-info">John Doe</a>
                    </strong>Uploaded 2 new photos</p>
                </div>
              </div>
  
              <div class="time-item">
                <div class="item-info">
                  <small class="text-muted">3 hours ago</small>
                  <p>
                    <a href="" class="text-info">Lorem</a> commented your post.</p>
                  <p>
                    <em>"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam laoreet tellus ut tincidunt euismod. "</em>
                  </p>
                </div>
              </div>
  
              <div class="time-item">
                <div class="item-info">
                  <small class="text-muted">5 hours ago</small>
                  <p>
                    <a href="" class="text-info">Jessi</a> attended a meeting with
                    <a href="#" class="text-success">John Doe</a>.</p>
                  <p>
                    <em>"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam laoreet tellus ut tincidunt euismod. "</em>
                  </p>
                </div>
              </div>
            </div>
          </div>
  
          <div class="tab-pane fade" id="profile-2" style="display: none;">
            <div class="contact-list nicescroll" style="overflow: hidden; outline: none;" tabindex="5002">
              <ul class="list-group contacts-list">
                <li class="list-group-item">
                  <a href="#">
                    <div class="avatar">
                      <!--<img src="assets/images/users/avatar-1.jpg" alt="">-->
                    </div>
                    <span class="name">Chadengle</span>
                    <i class="fa fa-circle online"></i>
                  </a>
                  <span class="clearfix"></span>
                </li>
              </ul>
            </div>
          </div>
  
          <div class="tab-pane fade" id="messages-2" style="display: none;">
  
            <div class="row m-t-20">
              <div class="col-xs-8">
                <h5 class="m-0">Notifications</h5>
                <p class="text-muted m-b-0">
                  <small>Do you need them?</small>
                </p>
              </div>
              <div class="col-xs-4 text-right">
                <input type="checkbox" checked="" data-plugin="switchery" data-color="#3bafda" data-size="small" data-switchery="true" style="display: none;">
                <span class="switchery switchery-small" style="background-color: rgb(59, 175, 218); border-color: rgb(59, 175, 218); box-shadow: rgb(59, 175, 218) 0px 0px 0px 0px inset; transition: border 0.4s, box-shadow 0.4s, background-color 1.2s;">
                  <small style="left: 13px; background-color: rgb(255, 255, 255); transition: background-color 0.4s, left 0.2s;"></small>
                </span>
              </div>
            </div>
  
            <div class="row m-t-20">
              <div class="col-xs-8">
                <h5 class="m-0">API Access</h5>
                <p class="m-b-0 text-muted">
                  <small>Enable/Disable access</small>
                </p>
              </div>
              <div class="col-xs-4 text-right">
                <input type="checkbox" checked="" data-plugin="switchery" data-color="#3bafda" data-size="small" data-switchery="true" style="display: none;">
                <span class="switchery switchery-small" style="background-color: rgb(59, 175, 218); border-color: rgb(59, 175, 218); box-shadow: rgb(59, 175, 218) 0px 0px 0px 0px inset; transition: border 0.4s, box-shadow 0.4s, background-color 1.2s;">
                  <small style="left: 13px; background-color: rgb(255, 255, 255); transition: background-color 0.4s, left 0.2s;"></small>
                </span>
              </div>
            </div>
  
            <div class="row m-t-20">
              <div class="col-xs-8">
                <h5 class="m-0">Auto Updates</h5>
                <p class="m-b-0 text-muted">
                  <small>Keep up to date</small>
                </p>
              </div>
              <div class="col-xs-4 text-right">
                <input type="checkbox" checked="" data-plugin="switchery" data-color="#3bafda" data-size="small" data-switchery="true" style="display: none;">
                <span class="switchery switchery-small" style="background-color: rgb(59, 175, 218); border-color: rgb(59, 175, 218); box-shadow: rgb(59, 175, 218) 0px 0px 0px 0px inset; transition: border 0.4s, box-shadow 0.4s, background-color 1.2s;">
                  <small style="left: 13px; background-color: rgb(255, 255, 255); transition: background-color 0.4s, left 0.2s;"></small>
                </span>
              </div>
            </div>
  
            <div class="row m-t-20">
              <div class="col-xs-8">
                <h5 class="m-0">Online Status</h5>
                <p class="m-b-0 text-muted">
                  <small>Show your status to all</small>
                </p>
              </div>
              <div class="col-xs-4 text-right">
                <input type="checkbox" checked="" data-plugin="switchery" data-color="#3bafda" data-size="small" data-switchery="true" style="display: none;">
                <span class="switchery switchery-small" style="background-color: rgb(59, 175, 218); border-color: rgb(59, 175, 218); box-shadow: rgb(59, 175, 218) 0px 0px 0px 0px inset; transition: border 0.4s, box-shadow 0.4s, background-color 1.2s;">
                  <small style="left: 13px; background-color: rgb(255, 255, 255); transition: background-color 0.4s, left 0.2s;"></small>
                </span>
              </div>
            </div>
  
          </div>
        </div>
      </div>
    </div>
  
  </div>
</template>

<script> 
import HeaderComponent from './Header.vue'
import FooterComponent from './Footer.vue'
import ToastContainer from '../elements/ToastContainer.vue'

export default {
  name: 'app',
  computed: {
    pages() {
      return this.$store.state.pages;
    },
    conferences() {
      return this.$store.state.conferences;
    }
  },
  created() {
    this.$http.get('api/user/page').then(response => {
      this.$store.dispatch('initPages', response.body);
    })
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (!vm.$store.getters.isAdmin)
        next("/");
    })
  },
  components: {
    HeaderComponent,
    FooterComponent,
    ToastContainer
  },
}
</script>