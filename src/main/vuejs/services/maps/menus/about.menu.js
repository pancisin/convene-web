export default {
  hasPermission: user => {
    return true;
  },
  routes: [
    {
      name: 'about',
      code: 'client.footer.about'
    },
    // {
    //   name: 'about',
    //   code: 'client.footer.features'
    // },
    {
      name: 'pricing',
      code: 'client.footer.pricing'
    },
    {
      name: 'faq',
      code: 'client.footer.faq'
    },
    {
      name: 'terms',
      code: 'client.footer.terms'
    },
    {
      name: 'privacy-policy',
      code: 'client.footer.privacy'
    }
  ]
};
