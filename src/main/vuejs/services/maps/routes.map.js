const MainRoutes = [
  {
    name: 'admin.dashboard',
    code: 'admin.menu.dashboard',
    icon: 'dashboard'
  },
  {
    name: 'admin.event',
    code: 'admin.menu.events',
    icon: 'event'
  },
  // {
  //   name: 'admin.media-manager',
  //   code: 'admin.menu.media-manager',
  //   icon: 'picture-o'
  // },
  {
    name: 'admin.notification',
    code: 'admin.menu.notifications',
    icon: 'notifications_none'
  }
];

const PageRoutes = [
  {
    name: 'page.overview',
    code: 'admin.page.overview'
  },
  {
    name: 'page.events',
    code: 'admin.page.events'
  },
  // {
  //   name: 'page.places',
  //   code: 'admin.page.places'
  // },
  {
    name: 'page.services',
    code: 'admin.page.services'
  },
  {
    name: 'page.requests',
    code: 'admin.page.requests'
  },
  {
    name: 'page.gallery',
    code: 'admin.page.gallery'
  },
  {
    name: 'page.administrators',
    code: 'admin.page.administrators'
  },
  {
    name: 'page.settings',
    code: 'admin.page.settings'
  }
];

const ConferenceRoutes = [
  {
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
    code: 'admin.conference.settings'
  }
];

const FooterRoutes = [
  {
    name: 'faq',
    code: 'admin.menu.faq',
    icon: 'help_outline'
  },
  {
    name: 'terms',
    code: 'admin.menu.terms',
    icon: 'lock'
  },
  {
    name: 'privacy-policy',
    code: 'admin.menu.privacy',
    icon: 'pan_tool'
  }
];

export default {
  main: MainRoutes,
  page: PageRoutes,
  conference: ConferenceRoutes,
  footer: FooterRoutes
};
