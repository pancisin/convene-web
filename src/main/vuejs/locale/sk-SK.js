import category from './sk-SK/category.js'
import admin from './sk-SK/admin.js'
import client from './sk-SK/client.js'
export default {
  category, admin, client,
  role: {
    owner: "Vlastník",
    administrator: "Administrátor",
    reporter: "Prispievateľ",
    visitor: "Navštevník"
  },
  administrator: {
    active: "Aktívny",
    inactive: "Neaktívny"
  },
  page: {
    name: 'Názov',
    category: 'Kategória',
    branch: 'Vetva',
    summary: 'Popis'
  },
  unit: {
    hour: 'Hodina',
    minute: 'Minúta',
    squared_meter: 'Štvorcový meter',
    piece: 'Kus'
  },
  subscription: {
    starter: 'Jednoduchá',
    professional: 'Prémium',
    enterprise: 'Firemná',
    state: {
      new: 'Nová',
      active: 'Aktívna',
      expired: 'Ukončená',
      unpaid: 'Nevyplatená'
    },
    pay: 'Vyplatiť'
  }
}