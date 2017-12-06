import ShareUrl from 'share-url';

export default {
  facebook: {
    url: data => {
      return ShareUrl.facebook({
        u: window.location
      });
    },
    class: 'share-panel-facebook',
    icon: 'fa-facebook',
    color: '#3b5998',
    content: 'Share'
  },
  googlePlus: {
    url: data => {
      return ShareUrl.googlePlus({
        url: window.location
      });
    },
    class: 'share-panel-google',
    icon: 'fa-google',
    color: '#dd4b39',
    content: 'Share'
  },
  pinterest: {
    url: data => {
      return ShareUrl.pinterest({
        url: window.location,
        media: data.media,
        description: data.description
      });
    },
    class: 'share-panel-pinterest',
    icon: 'fa-pinterest',
    color: '#cb2027',
    content: 'Pin'
  }
};
