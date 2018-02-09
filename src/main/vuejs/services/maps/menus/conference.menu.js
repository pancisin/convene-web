export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
    name: 'conference.overview',
    code: 'admin.conference.overview'
  },
  {
    name: 'conference.events',
    code: 'admin.conference.events'
  },
  {
    name: 'conference.articles',
    code: 'admin.conference.articles'
  },
  {
    name: 'conference.surveys',
    code: 'admin.conference.surveys'
  },
  {
    name: 'conference.places',
    code: 'admin.conference.places'
  },
  {
    name: 'conference.administrators',
    code: 'admin.conference.administrators'
  },
  {
    name: 'conference.attendees',
    code: 'admin.conference.attendees'
  },
  {
    name: 'conference.settings',
    code: 'admin.conference.settings.default'
  }
  ]
};
