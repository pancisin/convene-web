import GoogleMapsApiLoader from 'google-maps-api-loader';

const GoogleMapsApi = {};
GoogleMapsApi.install = (Vue, options) => {
  let googleApi = null;
  let loadingPromise = false;

  const initializeApi = () => {
    if (!loadingPromise) {
      loadingPromise = new Promise((resolve, reject) => {
        if (googleApi) {
          resolve(googleApi);
        } else {
          GoogleMapsApiLoader({
            apiKey: 'AIzaSyBKua_eTxYYK4hJf7sRKeH666HdcH3UlAg',
            libraries: [ 'places' ]
          }).then(google => {
            googleApi = google;
            resolve(googleApi);
          }, err => {
            reject();
            console.error(err);
          });
        }
      });
    }

    return loadingPromise;
  };

  const googleMapApiContext = {
    map (element, options) {
      return new googleApi.maps.Map(element, {
        zoom: 16,
        ...options
              // styles: gmapStyle
      });
    },
    autocomplete (element, changeCallback) {
      const atcmpl = new googleApi.maps.places.Autocomplete(element);

      googleApi.maps.event.addListener(atcmpl, 'place_changed', () => {
        const location = atcmpl.getPlace().geometry.location;
        if (changeCallback) {
          changeCallback({
            lat: location.lat(),
            lng: location.lng()
          });
        }
      });

      return atcmpl;
    },

    geocode (location, callback) {
      const geocoder = new googleApi.maps.Geocoder();

      geocoder.geocode({
        location
      }, (result, status) => {
        if (status !== 'OK') return;

        const location = {
          lat: result[0].geometry.location.lat(),
          lng: result[0].geometry.location.lng(),
          address: result[0].formatted_address
        };

        if (callback) {
          callback(location);
        }
      });
    }
  };

  Vue.prototype.$googleMapApi = {
    load: (callback) => {
      initializeApi().then(() => {
        callback(googleMapApiContext);
      });
    }
  };
};

export default GoogleMapsApi;
