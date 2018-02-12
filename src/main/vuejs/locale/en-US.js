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
    },
    privacy: {
      default: 'Privacy',
      constraint: {
        profile: 'User profile',
        'attending-events': 'Attending events',
        events: 'Created events'
      },
      description: {
        profile: 'Your profile page',
        'attending-events': 'Events that you are attending in future.',
        events: 'Events created by you'
      },
      access: {
        public: 'Public',
        private: 'Only me',
        friends: 'Close friends'
      }
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
