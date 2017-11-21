export default {
  hasPermission: (user) => {
    return user.role.name === 'ROLE_SUPERADMIN';
  },
  routes: [
    {
      name: 'system.users',
      code: 'admin.system.users',
      icon: 'supervisor_account'
    },
    // {
    //   name: 'system.support',
    //   code: 'admin.system.support',
    //   icon: 'idk'
    // }
    {
      name: 'system.lists',
      code: 'admin.system.news',
      icon: 'inbox'
    },
    {
      name: 'system.page-import',
      code: 'admin.system.page_import',
      icon: 'accessibility'
    },
    {
      name: 'system.maintenance',
      code: 'admin.system.maintenance',
      icon: 'build'
    }
  ]
};
