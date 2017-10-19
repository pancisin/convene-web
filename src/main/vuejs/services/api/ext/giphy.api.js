import Vue from 'vue';

const GIPHY_API_URL = 'https://api.giphy.com/v1/gifs';
const api_key = '5cb7pazJk8y5DCLMRFZWQGYnYg83b6Js';
const defaultParams = {
  rating: 'G',
  lang: 'en'
};

export default {

  /**
   * Search gifs by keyword
   * @param {*} keyword - searched keyword
   * @param {*} limit - limit gifs to
   * @param {*} success - success callback function
   */
  search (keyword, page, size, success) {
    Vue.http.get(`${GIPHY_API_URL}/search`, {
      before (request) {
        request.headers.delete('Authorization');
      },
      params: {
        api_key,
        q: keyword,
        offset: page,
        limit: size,
        ...defaultParams
      }
    }).then(response => {
      console.warn(response.data.data);
      const gifs = response.data.data.map(g => {
        return {
          path: g.images.downsized.url,
          thumbnail: g.images.preview_gif.url
        };
      });

      success(gifs);
    });
  }
};
