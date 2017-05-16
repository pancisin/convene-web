 <template>
  <div class="topbar">
  
    <div class="topbar-left">
      <div class="text-center">
        <router-link to="/" class="logo">
          <i class="fa fa-book"></i>
          <span>Bookster</span>
        </router-link>
      </div>
    </div>
  
    <div class="navbar navbar-default" role="navigation">
      <div class="container">
        <div class="">
          <div class="pull-left">
            <button class="button-menu-mobile open-left waves-effect">
              <i class="md md-menu"></i>
            </button>
            <span class="clearfix"></span>
          </div>
  
          <ul class="nav navbar-nav navbar-right pull-right">
            <notifications />
            <li class="hidden-xs">
              <select class="form-control" v-model="locale">
                <option v-for="locale in locales" :value="locale" v-text="locale.name"></option>
              </select>
            </li>
            <li class="hidden-xs">
              <a class="right-bar-toggle waves-effect waves-light">
                <i class="material-icons">settings</i>
              </a>
            </li>
  
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import Auth from '../services/auth.js'
import moment from "moment"
import Notifications from '../elements/Notifications.vue'

export default {
  name: 'header',
  data() {
    return {
      locales: [],
    }
  },
  created() {
    this.getLocales();
  },
  computed: {
    locale: {
      get() {
        return this.$store.getters.locale;
      },
      set(value) {
        this.$http.put('api/user/locale', value).then(response => {
          Auth.updateUserData(this);
          moment.locale(value.code);
          this.$i18n.locale = value.code;
        })
      }
    }
  },
  methods: {
    logout: function () {
      Auth.logout(this, '/login')
    },
    getLocales() {
      this.$http.get('api/locales').then(response => {
        this.locales = response.body;
      })
    }
  },
  components: {
    Notifications,
  },
}
</script>