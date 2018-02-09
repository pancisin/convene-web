export default {
  hasPermission: (user) => {
    return true;
  },
  routes: [{
    name: 'conference.settings.information',
    code: 'admin.conference.settings.edit',
    icon: 'dashboard'
  },
  {
    name: 'conference.settings.registration',
    code: 'admin.conference.settings.registration',
    icon: 'list'
  },
  {
    name: 'conference.settings.partners',
    code: 'admin.conference.settings.partners',
    icon: 'picture-o'
  },
  {
    name: 'conference.settings.deletion',
    code: 'admin.conference.settings.deletion',
    icon: 'users'
  }]
};
