export default {
  path: 'list/:list_id',
  component: resolve => require(['pages/ArticlesList.vue'], resolve),
  name: 'system.list',
  meta: {
    title: 'List of articles'
  },
  redirect: {
    name: 'system.list.articles'
  },
  children: [
    {
      path: 'articles',
      name: 'system.list.articles',
      component: resolve => require(['pages/templates/Articles.vue'], resolve),
      props: {
        editable: true,
        insertable: true
      }
    },
    {
      path: 'bots',
      name: 'system.list.bots',
      component: resolve => require(['pages/templates/ArticleBots.vue'], resolve),
      props: {

      },
      meta: {
        title: 'Article bots'
      }
    },
    {
      path: 'create-bot',
      name: 'system.list.create-bot',
      component: resolve => require(['pages/ArticleBot.vue'], resolve),
      props: {
        edit: false
      }
    },
    {
      path: 'settings',
      name: 'system.list.settings',
      component: resolve => require(['pages/articles-list/Settings.vue'], resolve),
      meta: {
        title: 'List settings'
      }
    }
  ]
};
