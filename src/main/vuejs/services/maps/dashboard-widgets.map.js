import * as widgets from 'elements/widgets';
import Notepad from 'assets/img/notepad.jpg';

export default [
  {
    name: 'LATEST_ACTIVITY',
    code: 'admin.widgets.latest_activity',
    thumbnail: Notepad,
    component: widgets.LatestActivity
  },
  {
    name: 'FOLLOWERS',
    code: 'admin.widgets.followers',
    thumbnail: Notepad,
    component: widgets.Followers
  },
  {
    name: 'ACTIVE_EVENTS',
    code: 'admin.widgets.active_events',
    thumbnail: Notepad,
    component: widgets.ActiveEvents
  },
  {
    name: 'BUTTON_WIDGET',
    code: 'admin.widgets.button_widget',
    thumbnail: Notepad,
    component: widgets.ButtonWidget
  },
  {
    name: 'SERVICE_REQUESTS',
    code: 'admin.widgets.service_requests',
    thumbnail: Notepad,
    component: widgets.ServiceRequests
  }
];
