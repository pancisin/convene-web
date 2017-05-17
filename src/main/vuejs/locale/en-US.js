import category from './category/category.en-US.js'
export default {
  category: category,
  menu: {
    dashboard: "Dashboard",
    work_management: "Work management",
    employees: "Employees",
    customers: "Customers",
  },
  settings: {
    default: "Settings",
    account: "Account",
    company: "Company",
    license: "Licese",
    logout: "Logout",
    task: 'Task attributes',
    users: 'Users',
  },
  account: {
    firstName: "First name",
    lastName: "Last name",
    email: "Email",
    password: "Password",
    confirm_password: "Confirm password",
  },
  register: {
    already: "Do you already have an account ?",
    not_yet: "Still haven't got an account ?"
  },
  actions: {
    update_close: "Update and close",
    create: "Create",
    update: "Update",
    cancel: "Cancel",
    logging: "Logging in...",
    login: "Sign in",
    register: "Sign up",
    registering: "Registering...",
    remove: "Remove"
  },
  calendar: {
    next: "Next",
    prev: "Previous",
  },
  company: {
    default: "Company",
    ico: "ico",
    name: "Name",
    contact: "Contact information",
    email: "Email",
    phone: "Mobile phone number",
    zip: "Zip code",
    city: "City",
    street: "Street",
    number: "Number",
    information: "Company information",
    residence: "Residence",
    contacts: "Contacts",
  },
  customer: {
    default: "Customer | Customers",
    name: "Name",
    address: "Address",
    person: {
      default: "Contact person",
      name: "Firstname and lastname",
      email: "Email"
    },
    email: "Contact email",
    description: "Description",
    report: {
      default: "Automatic report settings",
      active: "Send automatic report via email."
    },
    create: "Create or update customer",
  },
  duty: {
    default: "Duty | Duties",
    location: "Location",
    description: "Description",
    start_date: "Start date",
    end_date: "End date",

    closest_occ: "Closest occurrence",
    tasks: "Tasks",

    create_update: "Create or update duty",
    recurrence: "Recurrence",
    advanced: "Advanced",
    employees: "Employees",
    task: {
      add: "Add task",
      note: "Note"
    }
  },
  employee: {
    default: "Employee | Employees",
    name: "Firstname and lastname",
    create: "Create employee",
    firstName: "Firstname",
    lastName: "lastName",
  },
  instance: {
    update: "Update instance of duty",

  },
  notification: {
    default: "Notification | Notifications",
    empty: "There's nothing to display."
  },
  chat: {
    conversations: 'Conversations',
    send: "Send",
    placeholder: "Type something...",
  },
  user: {
    default: "User | Users"
  },
  period: {
    DAILY: "Daily",
    WEEKLY: "Weekly",
    MONTHLY: "Monthly",
  }
}