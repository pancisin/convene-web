import * as translations from './en-US/index';

export default {
  ...translations,
  role: {
    owner: 'Owner',
    superadmin: 'Super Administrator',
    administrator: 'Administrator',
    reporter: 'Reporter',
    visitor: 'Visitor',
    moderator: 'Moderator'
  },
  administrator: {
    active: 'Active',
    inactive: 'Inactive'
  },
  page: {
    name: 'Name',
    category: 'Category',
    branch: 'Branch',
    summary: 'Summary'
  },
  unit: {
    hour: 'Hour',
    minute: 'Minute',
    squared_meter: 'Squared meter',
    piece: 'Piece'
  },
  subscription: {
    free: 'Free',
    starter: 'Starter',
    professional: 'Premium',
    enterprise: 'Enterprise',

    state: {
      new: 'New',
      active: 'Active',
      expired: 'Expired',
      unpaid: 'Unpaid'
    },
    pay: 'Pay'
  },
  settings: {
    default: 'Settings',
    account: 'Account',
    notifications: 'Notifications',
    license: {
      default: 'License',
      price: 'Price',
      acquired: 'Acquired',
      expires: 'Expires',
      state: 'State'
    }
  },
  user: {
    email: 'Email address',
    name: 'Name',
    firstName: 'First name',
    lastName: 'Last name',
    address: {
      street: 'Street',
      number: 'Number',
      zip: 'ZIP',
      city: 'City',
      state: 'State'
    },
    about_me: 'About me'
  },
  locale: {
    en: 'English',
    sk: 'Slovak'
  },
  error: {
    user_not_found: 'User not found in our database.',
    password: 'Password does not match'
  },
  activity: {
    target: {
      conference: 'conference',
      page: 'page'
    },
    type: {
      following: 'has started to following',
      attending: 'is attending',
      delete: 'has deleted',
      update: 'has updated',
      create_event: 'has created an event to',
      create_service: 'has created service to',
      create_administrator: 'has granted access rights to',
      create_place: 'has created place to',
      create_article: 'has created an article to',
      create_survey: 'has created a survey to'
    }
  },
  venue_editor: {
    objects: {
      squared_table: 'Squared table',
      round_table: 'Round table',
      seats_in_rows: 'Seats in rows',
      seat: 'Seat'
    },
    building: {
      room: 'Room'
    }
  },
  authenticate: {
    login: 'Login',
    sign_up: 'Sign up',
    remember_me: 'Remember me',
    forgot_password: 'Forgot your password?',
    create_account: 'Create an account',
    or: 'OR',
    facebook_login: 'Login with Facebook'
  }
};
