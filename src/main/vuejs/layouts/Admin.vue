<template>
  <div id="wrapper">
    <chat-container />
    <toast-container />
    <header-component />
  
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
  
          <drop-down v-for="page in pages" :key="page.id">
            <i class="material-icons" slot="title">work</i>
            <span v-text="page.name" slot="title"></span>
            <li slot="item">
              <router-link :to="{ name: 'page.overview', params: { id : page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.overview') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.events', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.events') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.places', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.places') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.administrators', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.administrators') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.services', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.services') }}
              </router-link>
            </li>
            <li slot="item">
              <router-link :to="{ name: 'page.requests', params: { id: page.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.requests') }}
              </router-link>
            </li>
          </drop-down>
  
          <li>
            <router-link to="/admin/page/create" class="waves-effect text-muted">
              <i class="material-icons">add</i>
              <span>{{ $t('admin.menu.page_create') }}</span>
            </router-link>
          </li>
  
          <li class="menu-title">{{ $t('admin.menu.conferences') }}
            <span class="label label-warning pull-right">Enterprise</span>
          </li>
  
          <drop-down v-for="conference in conferences" :key="conference.id">
            <i class="material-icons" slot="title">work</i>
            <span v-text="conference.name" slot="title"></span>
            <li slot="item">
              <router-link :to="{ name: 'conference.overview', params: { id : conference.id }}" class="list-group-item waves-effect">
                {{ $t('admin.page.overview') }}
              </router-link>
            </li>
          </drop-down>
  
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
  </div>
</template>

<script> 
import HeaderComponent from './Admin.header.vue'
import FooterComponent from './Footer.vue'
import ToastContainer from '../elements/ToastContainer.vue'
import VerticalMenuDrop from '../elements/VerticalMenuDrop.vue'
import ChatContainer from '../elements/ChatContainer.vue'

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
    this.$http.get('api/user/conference').then(response => {
      this.$store.dispatch('initConferences', response.body);
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
    ToastContainer,
    DropDown: VerticalMenuDrop,
    ChatContainer
  },
}
</script>