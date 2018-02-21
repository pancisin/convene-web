import UserApi from 'api/user.api';
import { Observable } from 'rxjs';
import * as types from 'store/mutation-types';

const state = {
  activityFeed: {}
};

const getters = {
  activityFeed: state => state.activityFeed
};

const actions = {
  initializeActivityFeed ({ commit }) {
    console.log('fdsadasd');

  }
  // loadMoreActivityFeed: observableAction((action, { commit }) => {
  //   console.log('dsadsadsa');

  //   action.throttleTime(400)
  //   .mapTo(1)
  //   .scan((acc, cur) => acc + cur, 0)
  //   // .do(() => { this.loading = true; })
  //   .flatMap(page => Observable.create(ob => {
  //     UserApi.getActivityFeed(page, 10, paginator => {
  //       ob.next(paginator);
  //     });
  //   }))
  //   .map(p => {
  //     const items = this.paginator.content || [];

  //     return {
  //       ...p,
  //       content: items.concat(p.content)
  //     };
  //   })
  //   // .do(() => { this.loading = false; })
  //   .startWith({})
  //   .subscribe(paginator => {
  //     commit(types.SET_ACTIVITY_FEED, { paginator });
  //   });
  // })
};

const mutations = {
  [types.SET_ACTIVITY_FEED] (state, { paginator }) {
    state = {
      ...state,
      activityFeed: paginator
    };
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};
