import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    user: null
  },
  mutations: {
    setUser(state, { user }) {
      state.user = user;
    }
  },
  getters: {
    company_id: state => {
      // return state.user.company.id;
      return 0;
    }
  }
});

export default store;