import category from './en-US/category.js';
import admin from './en-US/admin.js';
import client from './en-US/client.js';
export default {
  category, admin, client,
  role: {
    owner: 'Owner',
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
    firstName: 'First name',
    lastName: 'Last name',
    address: {
      street: 'Street',
      number: 'Number',
      zip: 'ZIP',
      city: 'City',
      state: 'State'
    }
  },
  locale: {
    en: 'English',
    sk: 'Slovak'
  },
  error: {
    user_not_found: 'User not found in our database.',
    password: 'Password does not match'
  }
};
