import store from 'store/index';
export default [
  {
    path: 'dashboard',
    name: 'admin.dashboard',
    component: resolve => require(['../pages/Dashboard.vue'], resolve),
    meta: {
      title: 'Dashboard'
    }
  },
  {
    path: 'event',
    name: 'admin.event',
    component: resolve => require(['../pages/Event.index.vue'], resolve),
    meta: {
      title: 'Events'
    }
  },
  {
    path: 'pages',
    name: 'admin.pages',
    component: resolve => require(['../pages/Pages.vue'], resolve),
    meta: {
      title: 'Pages'
    }
  },
  {
    path: 'media-manager',
    name: 'admin.media-manager',
    component: resolve => require(['../pages/MediaManager.vue'], resolve),
    meta: {
      title: 'Media manager'
    }
  },
  {
    path: 'users',
    name: 'system.users',
    component: resolve => require(['../pages/system/Users.vue'], resolve)
  },
  {
    path: 'maintenance',
    name: 'system.maintenance',
    component: resolve => require(['../pages/system/Maintenance.vue'], resolve)
  },
  {
    path: 'page-import',
    name: 'system.page-import',
    component: resolve => require(['../pages/system/PageImport.vue'], resolve)
  },
  {
    path: 'conference/create',
    component: resolve => require(['../pages/Conference.create.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.license;
      if (license && license.subscription.conferenceLimit > store.getters.conferences.length) {
        next();
      } else {
        next('/pricing');
      }
    },
    meta: {
      title: 'Create conference'
    }
  },
  {
    path: 'conference/:id',
    component: resolve => require(['../pages/Conference.vue'], resolve),
    redirect: '/admin/conference/:id/overview',
    children: [
      {
        path: 'overview',
        name: 'conference.overview',
        component: resolve => require(['../pages/templates/Dashboard.vue'], resolve),
        meta: {
          title: 'Overview'
        }
      },
      {
        path: 'events',
        name: 'conference.events',
        component: resolve => require(['../pages/templates/Events.vue'], resolve),
        meta: {
          title: 'Events'
        }
      },
      {
        path: 'attendees',
        name: 'conference.attendees',
        component: resolve => require(['../pages/conference/Attendees.vue'], resolve),
        meta: {
          title: 'Attendees'
        }
      },
      {
        path: 'administrators',
        name: 'conference.administrators',
        component: resolve => require(['../pages/templates/Administrators.vue'], resolve),
        meta: {
          title: 'Administrators'
        }
      },
      {
        path: 'settings',
        name: 'conference.settings',
        component: resolve => require(['../pages/conference/Settings.vue'], resolve),
        redirect: '/admin/conference/:id/settings/information',
        children: [
          {
            path: 'information',
            name: 'conference.settings.information',
            component: resolve => require(['../pages/conference/settings/Information.vue'], resolve),
            meta: {
              title: 'Conference information settings'
            }
          },
          {
            path: 'registration',
            name: 'conference.settings.registration',
            component: resolve => require(['../pages/conference/settings/Registration.vue'], resolve),
            meta: {
              title: 'Conference registration settings'
            }
          },
          {
            path: 'delete',
            name: 'conference.settings.deletion',
            component: resolve => require(['../pages/conference/settings/Delete.vue'], resolve),
            meta: {
              titie: 'Conference deletion'
            }
          }
        ]
      },
      {
        path: 'articles',
        name: 'conference.articles',
        component: resolve => require(['../pages/templates/Articles.vue'], resolve),
        meta: {
          title: 'Articles'
        }
      },
      {
        path: 'create-article',
        name: 'conference.article.create',
        component: resolve => require(['../pages/Article.vue'], resolve),
        meta: {
          title: 'Create article'
        }
      },
      {
        path: 'surveys',
        name: 'conference.surveys',
        component: resolve => require(['../pages/templates/Surveys.vue'], resolve),
        meta: {
          title: 'Surveys'
        }
      },
      {
        path: 'places',
        name: 'conference.places',
        component: resolve => require(['../pages/templates/Places.vue'], resolve),
        meta: {
          title: 'Places'
        }
      },
      {
        path: 'create-place',
        name: 'conference.place.create',
        component: resolve => require(['../pages/place/Overview.vue'], resolve),
        props: (route) => (
          {
            conference_id: route.params.id,
            edit: false
          }
        ),
        meta: {
          titlle: 'Create place'
        }
      },
      {
        path: 'survey-create',
        name: 'conference.survey.create',
        component: resolve => require(['../pages/Survey.create.vue'], resolve),
        props: (route) => (
          {
            conference_id: route.params.id,
            edit: false
          }
        ),
        meta: {
          title: 'Create survey'
        }
      }
    ]
  },
  {
    path: 'survey/:survey_id',
    name: 'survey',
    component: resolve => require(['../pages/Survey.vue'], resolve)
  },
  {
    path: 'event/:id',
    component: resolve => require(['../pages/Event.vue'], resolve),
    redirect: '/admin/event/:id/overview',
    name: 'event',
    children: [
      {
        path: 'overview',
        name: 'event.overview',
        component: resolve => require(['../pages/event/Overview.vue'], resolve),
        meta: {
          titlle: 'Overview'
        }
      },
      {
        path: 'programme',
        name: 'event.programme',
        component: resolve => require(['../pages/event/Programme.vue'], resolve),
        meta: {
          titlle: 'Programme'
        }
      },
      {
        path: 'attendees',
        name: 'event.attendees',
        component: resolve => require(['../pages/templates/Attendees.vue'], resolve),
        meta: {
          title: 'Attendees'
        }
      },
      {
        path: 'gallery',
        name: 'event.gallery',
        component: resolve => require(['../pages/templates/Gallery.vue'], resolve),
        meta: {
          title: 'Gallery'
        }
      },
      {
        path: 'advanced',
        name: 'event.advanced',
        component: resolve => require(['../pages/event/Advanced.vue'], resolve),
        meta: {
          title: 'Advanced'
        }
      }
    ]
  },
  {
    path: 'article/:article_id',
    name: 'article',
    component: resolve => require(['../pages/Article.vue'], resolve),
    meta: {
      titlle: 'Article'
    }
  },
  {
    path: 'page/create',
    component: resolve => require(['../pages/Page.create.vue'], resolve),
    beforeEnter: (to, from, next) => {
      var license = store.getters.license;
      if (license && license.subscription.pageLimit > store.getters.pages.length) {
        next();
      } else {
        next('/pricing');
      }
    },
    meta: {
      titlle: 'Create page'
    }
  },
  {
    path: 'page/:id',
    component: resolve => require(['../pages/Page.vue'], resolve),
    redirect: '/admin/page/:id/overview',
    name: 'page',
    children: [
      {
        path: 'overview',
        name: 'page.overview',
        component: resolve => require(['../pages/templates/Dashboard.vue'], resolve),
        meta: {
          titlle: 'Page Dashboard'
        }
      },
      {
        path: 'settings',
        name: 'page.settings',
        component: resolve => require(['../pages/page/Compose.vue'], resolve),
        meta: {
          titlle: 'Settings'
        }
      },
      {
        path: 'events',
        name: 'page.events',
        component: resolve => require(['../pages/templates/EventsCalendar.vue'], resolve),
        meta: {
          titlle: 'Page events'
        }
      },
      {
        path: 'services',
        name: 'page.services',
        component: resolve => require(['../pages/templates/Services.vue'], resolve),
        meta: {
          titlle: 'Services'
        }
      },
      {
        path: 'requests',
        name: 'page.requests',
        component: resolve => require(['../pages/templates/Requests.vue'], resolve),
        meta: {
          titlle: 'Requests'
        }
      },
      {
        path: 'administrators',
        name: 'page.administrators',
        component: resolve => require(['../pages/templates/Administrators.vue'], resolve),
        meta: {
          titlle: 'Administrators'
        }
      },
      {
        path: 'places',
        name: 'page.places',
        component: resolve => require(['../pages/templates/Places.vue'], resolve),
        meta: {
          titlle: 'Places'
        }
      },
      {
        path: 'create-place',
        name: 'page.create-place',
        component: resolve => require(['../pages/place/Overview.vue'], resolve),
        props: (route) => (
          {
            page_id: route.params.id,
            edit: false
          }
        ),
        meta: {
          titlle: 'Create place'
        }
      },
      {
        path: 'create-service',
        name: 'page.create-service',
        component: resolve => require(['../pages/Service.vue'], resolve)
      },
      {
        path: 'gallery',
        name: 'page.gallery',
        component: resolve => require(['../pages/templates/Gallery.vue'], resolve),
        props: {
          columns: 4,
          limit: 1024 * 1024 * 20
        },
        meta: {
          title: 'Gallery'
        }
      },
      {
        path: 'create-event-bot',
        name: 'page.create-event-bot',
        component: resolve => require(['../pages/EventBot.vue'], resolve),
        props: (route) => ({
          edit: false
        }),
        meta: {
          title: 'Create event bot'
        }
      },
      {
        path: 'bots',
        name: 'page.bots',
        component: resolve => require(['../pages/templates/Bots.vue'], resolve),
        meta: {
          title: 'Bots'
        }
      }
    ]
  },
  {
    path: 'service/:service_id',
    component: resolve => require(['../pages/Service.vue'], resolve),
    name: 'service'
  },
  {
    path: 'place/:place_id',
    component: resolve => require(['../pages/Place.vue'], resolve),
    redirect: '/admin/place/:place_id/overview',
    name: 'place',
    children: [
      {
        path: 'overview',
        name: 'place.overview',
        component: resolve => require(['../pages/place/Overview.vue'], resolve),
        meta: {
          titlle: 'Overview'
        }
      },
      {
        path: 'gallery',
        name: 'place.gallery',
        component: resolve => require(['../pages/templates/Gallery.vue'], resolve),
        meta: {
          titlle: 'Gallery'
        }
      },
      {
        path: 'venue',
        name: 'place.venue',
        component: resolve => require(['../pages/place/VenueEditor.vue'], resolve),
        meta: {
          title: 'Venue editor'
        }
      }
    ]
  },
  {
    path: 'event-bot/:bot_id',
    component: resolve => require(['../pages/EventBot.vue'], resolve),
    name: 'event-bot'
  },
  {
    path: 'notifications',
    name: 'admin.notification',
    component: resolve => require(['../pages/Notifications.vue'], resolve),
    meta: {
      titlle: 'Notifications'
    }
  },
  {
    path: 'list/:list_id',
    component: resolve => require(['../pages/ArticlesList.vue'], resolve),
    name: 'system.list',
    meta: {
      title: 'List of articles'
    },
    redirect: {
      name: 'system.list.articles'
    },
    children: [
      {
        path: 'create-article',
        name: 'system.list.create-article',
        component: resolve => require(['../pages/Article.vue'], resolve),
        props: route => ({
          edit: false
        }),
        meta: {
          title: 'Create article to list'
        }
      },
      {
        path: 'articles',
        name: 'system.list.articles',
        component: resolve => require(['../pages/templates/Articles.vue'], resolve),
        props: {
          editable: true,
          insertable: true
        }
      },
      {
        path: 'bots',
        name: 'system.list.bots',
        component: resolve => require(['../pages/templates/ArticleBots.vue'], resolve),
        props: {

        },
        meta: {
          title: 'Article bots'
        }
      },
      {
        path: 'create-bot',
        name: 'system.list.create-bot',
        component: resolve => require(['../pages/ArticleBot.vue'], resolve),
        props: {
          edit: false
        }
      },
      {
        path: 'settings',
        name: 'system.list.settings',
        component: resolve => require(['../pages/articles-list/Settings.vue'], resolve),
        meta: {
          title: 'List settings'
        }
      }
    ]
  },
  {
    path: 'lists',
    component: resolve => require(['../pages/ArticlesLists.vue'], resolve),
    name: 'system.lists'
  },
  {
    path: 'article-bot/:article_bot_id',
    component: resolve => require(['../pages/ArticleBot.vue'], resolve),
    name: 'admin.article-bot',
    props: {
      edit: true
    }
  },
  {
    path: '*',
    component: resolve => require(['../pages/error/404.vue'], resolve)
  }
];
