import * as menus from './menus';

const validateRoutes = (routes) => {
  routes.forEach((r, index) => {
    let {
      hasPermission
    } = r;

    if (hasPermission == null) {
      routes.splice(index, 1, {
        ...r,
        hasPermission: () => {
          return true;
        }
      });
    }
  });

  return routes;
};

const validateMenu = menu => {
  let {
    hasPermission
  } = menu;

  if (hasPermission == null) {
    menu = {
      ...menu,
      hasPermission: () => {
        return true;
      }
    };
  }

  validateRoutes(menu.routes);
  return menu;
};

export default {
  main: validateMenu(menus.MainMenu),
  page: validateMenu(menus.PageMenu),
  conference: validateMenu(menus.ConferenceMenu),
  footer: validateMenu(menus.FooterMenu),
  system: validateMenu(menus.SystemMenu),
  client: validateMenu(menus.ClientMenu)
};
