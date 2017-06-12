<template>
  <li class="dropdown hidden-xs" :class="{ 'open' : display }" v-click-outside="closeLanguage">
    <a @click="display = !display" class="dropdown-toggle waves-effect waves-light">
      <i class="fa fa-language"></i>
    </a>
    <ul class="dropdown-menu">
      <li v-for="loc in locales">
        <a @click="locale = loc; closeLanguage()">
          {{ $t(loc.code) }}
        </a>
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
        return Auth.user.authenticated ? this.$store.getters.getLocale : this.$i18n.locale;
      },
      set(value) {
        if (Auth.user.authenticated)
          this.$http.put('api/user/locale', JSON.stringify(value.name)).then(response => {
            Auth.updateUserData(this);
            moment.locale(value.name);
            this.$i18n.locale = value.name;
          })
        else {
          moment.locale(value.name);
          this.$i18n.locale = value.name;
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