export default {
  hasPermission: user => {
    return true;
  },
  routes: [{
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
  ]
};
