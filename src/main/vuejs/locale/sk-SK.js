import * as translations from './sk-SK/index';

export default {
  ...translations,
  role: {
    owner: 'Vlastník',
    superadmin: 'Super Administrátor',
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
    },
    about_me: 'O mne'
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
  },
  venue_editor: {
    objects: {
      squared_table: 'Štvorcový stôl',
      round_table: 'Okruhlý stôl',
      seats_in_rows: 'Sedadlá v radoch',
      seat: 'Miesto na sedenie'
    },
    building: {
      room: 'Miestnosť'
    }
  },
  authenticate: {
    login: 'Prihlásiť sa',
    sign_up: 'Registrovať',
    remember_me: 'Zapamätaj si ma',
    forgot_password: 'Zabudli ste heslo ?',
    create_account: 'Vytvoriť úćet',
    or: 'Alebo',
    facebook_login: 'Prihlásiť sa pomocou Facebooku'
  }
};
