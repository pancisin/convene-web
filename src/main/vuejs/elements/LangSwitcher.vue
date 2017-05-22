<template>
  <li class="dropdown hidden-xs" :class="{ 'open' : display }" v-click-outside="closeLanguage">
    <a @click="display = !display" class="dropdown-toggle waves-effect waves-light">
      <i class="fa fa-language"></i>
    </a>
    <ul class="dropdown-menu">
      <li v-for="loc in locales">
        <a @click="locale = loc; closeLanguage()" v-text="loc.name"></a>
      </li>
    </ul>
  </li>
</template>

<script>
import Auth from '../services/auth.js'
import moment from "moment"
export default {
  name: 'language-switcher',
  data() {
    return {
      locales: [],
      display: false
    }
  },
  created() {
    this.getLocales();
  },
  computed: {
    locale: {
      get() {
        return Auth.user.authenticated ? this.$store.getters.locale : this.$i18n.locale;
      },
      set(value) {
        if (Auth.user.authenticated)
          this.$http.put('api/user/locale', value).then(response => {
            Auth.updateUserData(this);
            moment.locale(value.code);
            this.$i18n.locale = value.code;
          })
        else {
          moment.locale(value.code);
          this.$i18n.locale = value.code;
        }
      }
    }
  },
  methods: {
    getLocales() {
      this.$http.get('public/locales').then(response => {
        this.locales = response.body;
      })
    },
    closeLanguage(e) {
      if (this.display)
        this.display = false;
    }
  }
}
</script>