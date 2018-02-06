export default {
  path: 'conference/:id',
  component: resolve => require(['pages/Conference.vue'], resolve),
  redirect: '/admin/conference/:id/overview',
  children: [
    {
      path: 'overview',
      name: 'conference.overview',
      component: resolve => require(['pages/templates/Dashboard.vue'], resolve),
      meta: {
        title: 'Overview'
      }
    },
    {
      path: 'events',
      name: 'conference.events',
      component: resolve => require(['pages/templates/Events.vue'], resolve),
      meta: {
        title: 'Events'
      }
    },
    {
      path: 'attendees',
      name: 'conference.attendees',
      component: resolve => require(['pages/conference/Attendees.vue'], resolve),
      meta: {
        title: 'Attendees'
      }
    },
    {
      path: 'administrators',
      name: 'conference.administrators',
      component: resolve => require(['pages/templates/Administrators.vue'], resolve),
      meta: {
        title: 'Administrators'
      }
    },
    {
      path: 'settings',
      name: 'conference.settings',
      component: resolve => require(['pages/conference/Settings.vue'], resolve),
      redirect: '/admin/conference/:id/settings/information',
      children: [
        {
          path: 'information',
          name: 'conference.settings.information',
          component: resolve => require(['pages/conference/settings/Information.vue'], resolve),
          meta: {
            title: 'Conference information settings'
          }
        },
        {
          path: 'registration',
          name: 'conference.settings.registration',
          component: resolve => require(['pages/conference/settings/Registration.vue'], resolve),
          meta: {
            title: 'Conference registration settings'
          }
        },
        {
          path: 'partners',
          name: 'conference.settings.partners',
          component: resolve => require(['pages/conference/settings/Partners.vue'], resolve),
          meta: {
            title: 'Conference partners settings'
          }
        },
        {
          path: 'delete',
          name: 'conference.settings.deletion',
          component: resolve => require(['pages/conference/settings/Delete.vue'], resolve),
          meta: {
            titie: 'Conference deletion'
          }
        }
      ]
    },
    {
      path: 'articles',
      name: 'conference.articles',
      component: resolve => require(['pages/templates/Articles.vue'], resolve),
      meta: {
        title: 'Articles'
      }
    },
    {
      path: 'surveys',
      name: 'conference.surveys',
      component: resolve => require(['pages/templates/Surveys.vue'], resolve),
      meta: {
        title: 'Surveys'
      }
    },
    {
      path: 'places',
      name: 'conference.places',
      component: resolve => require(['pages/templates/Places.vue'], resolve),
      meta: {
        title: 'Places'
      }
    },
    {
      path: 'create-place',
      name: 'conference.place.create',
      component: resolve => require(['pages/place/Overview.vue'], resolve),
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
      component: resolve => require(['pages/Survey.create.vue'], resolve),
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
};
