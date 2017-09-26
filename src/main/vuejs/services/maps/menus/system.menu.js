export default {
  hasPermission: (user) => {
    return user.role.name === 'ROLE_SUPERADMIN';
  },
  routes: [
    {
      name: 'system.users',
      code: 'admin.system.users',
      icon: 'supervisor_account'
    }
    // {
    //   name: 'system.support',
    //   code: 'admin.system.support',
    //   icon: 'idk'
    // }
  ]
};
