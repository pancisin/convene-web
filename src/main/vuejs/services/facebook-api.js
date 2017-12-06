const FacebookApi = {};

FacebookApi.install = (Vue, options) => {
  let facebookApi = null;
  let loadingPromise = false;

  const initializeApi = () => {
    if (!loadingPromise) {
      loadingPromise = new Promise((resolve, reject) => {
        if (facebookApi) {
          resolve(facebookApi);
        } else {
          window.fbAsyncInit = function () {
            FB.init({
              appId: '1890071151268219',
              cookie: true,
              xfbml: true,
              version: 'v2.9'
            });

            resolve(FB);
          };

          (function (d, s, id) {
            var js;
            var fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {return;}
            js = d.createElement(s); js.id = id;
            js.src = 'https://connect.facebook.net/en_US/sdk.js';
            fjs.parentNode.insertBefore(js, fjs);
          }(document, 'script', 'facebook-jssdk'));

        }
      });
      return loadingPromise;
    } else {
      return loadingPromise;
    }
  };

  const facebookApiContext = {
    getLoginStatus (callback) {
      facebookApi.getLoginStatus(callback);
    },
    login (callback) {
      facebookApi.login(callback, {
        scope: 'email'
      });
    },
    ui (options, callback) {
      facebookApi.ui(options, callback);
    }
  };

  Vue.prototype.$facebookApi = {
    load: (callback) => {
      initializeApi().then(api => {
        facebookApi = api;
        callback(facebookApiContext);
      });
    }
  };
};

export default FacebookApi;
