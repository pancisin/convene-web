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
    getLocales() {
      this.$http.get('api/locales').then(response => {
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