import category from './sk-SK/category.js';
import admin from './sk-SK/admin.js';
import client from './sk-SK/client.js';
export default {
  category, admin, client,
  role: {
    owner: 'Vlastník',
    administrator: 'Administrátor',
    reporter: 'Prispievateľ',
    visitor: 'Navštevník',
    moderator: 'Moderátor'
  },
  administrator: {
    active: 'Aktívny',
    inactive: 'Neaktívny'
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
    free: 'Zdarma',
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
  },
  settings: {
    default: 'Nastavenia',
    account: 'Účet',
    notifications: 'Oznámenia',
    license: {
      default: 'Licencia',
      price: 'Cena',
      acquired: 'Získaná',
      expires: 'Končí',
      state: 'Stav'
    }
  },
  user: {
    email: 'Emailová adresa',
    firstName: 'Krstné meno',
    lastName: 'Priezvisko',
    address: {
      street: 'Ulica',
      number: 'Číslo',
      zip: 'PSČ',
      city: 'Mesto',
      state: 'Štát'
    }
  },
  locale: {
    en: 'Anglický',
    sk: 'Slovenský'
  },
  error: {
    user_not_found: 'Používateľ sa nenašiel v našej databáze',
    password: 'Nesprávne heslo'
  },
  activity: {
    target: {
      conference: 'konferencie',
      page: 'stránky'
    },
    type: {
      following: 'začal následovať',
      attending: 'sa zúčastní',
      delete: 'zmazal',
      update: 'aktualizoval dáta',
      create_event: 'vytvoril udalosť do',
      create_service: 'vytvoril službu do',
      create_administrator: 'pridelil prístupové práva do',
      create_place: 'vytvoril miesto do',
      create_article: 'vytvoril článok do',
      create_survey: 'vytvoril prieskum do'
    }
  }
};
