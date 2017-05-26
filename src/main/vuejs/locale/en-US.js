import category from './en-US/category.js'
import admin from './en-US/admin.js'
import client from './en-US/client.js'
export default {
  category, admin, client,
  role: {
    owner: "Owner",
    administrator: "Administrator",
    reporter: "Reporter",
    visitor: "Visitor"
  },
  administrator: {
    active: "Active",
    inactive: "Inactive"
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
  }
}