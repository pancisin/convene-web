export default {
  event: {
    created: {
      title: 'Podujatie vytvorené !',
      message: 'v {subject} bola vytvorená úspešne.'
    },
    updated: {
      title: 'Podujatie aktualizované !',
      message: 'Podujatie {subject} bolo aktualizované úspešne.'
    },
    delete_prompt: {
      title: 'Deleting event.',
      message: 'Are you sure you want to delete permanently this event ?'
    },
    error: {
      created: {
        title: 'Error !',
        message: 'There is problem in creating {subject}. Please correct your data.'
      },
      updated: {
        title: 'Error !',
        message: 'There is problem in updating {subject}. Please correct your data.'
      }
    }
  },
  page: {
    created: {
      title: 'Page created !',
      message: 'Page {subject} has been created successfuly.'
    },
    updated: {
      title: 'Page updated !',
      message: 'Page {subject} has been updated successfuly.'
    },
    new_follower: {
      title: 'You have a new follower',
      message: '{subject} has just started to following one of your pages.'
    },
    event_created: {
      title: '{subject}',
      message: 'Event has just been created in one of your followed pages.'
    },
    service_created: {
      title: '{subject}',
      message: 'New service is available right now.'
    }
  },
  conference: {
    created: {
      title: 'Conference created !',
      message: 'Conference {subject} has been created successfuly.'
    },
    updated: {
      title: 'Conference updated !',
      message: 'Conference {subject} has been updated successfuly.'
    },
    new_attender: {
      title: 'Person has joined conference !',
      message: '{subject} has joined one of your conference conference.'
    },
    event_created: {
      title: '{subject}',
      message: 'Conference has a new event created.'
    },
    survey_created: {
      title: '{subject}',
      message: 'Conference has new survey created. Please complete this survey.'
    },
    article_created: {
      title: '{subject}',
      message: 'Conference has new article created that you might be interested in.'
    }
  },
  article: {
    updated: {
      title: 'Článok aktualizovaný !',
      message: '{subject} bol aktualizovaný úspešne.'
    },
    saved: {
      title: 'Článok uložený !',
      message: '{subject} bol uložený úspešne.'
    },
    delete_prompt: {
      title: 'Vymazanie článku.',
      message: 'Ste si istý, že chcete permanetne vymazať článok ?'
    }
  },
  service: {
    delete_prompt: {
      title: 'Deleting service',
      message: 'Are you sure you want to delete this service permanently ?'
    }
  },
  meta_field: {
    created: {
      title: 'Meta field created !',
      message: 'Meta field {subject} has been created successfuly.'
    },
    updated: {
      title: 'Meta field updated',
      message: 'Meta field {subject} has been updated successfuly.'
    }
  },
  place: {
    created: {
      title: 'Place created !',
      message: 'Place {subject} has been created successfuly.'
    },
    updated: {
      title: 'Place updated !',
      message: 'Place {subject} has been updated successfuly.'
    },
    delete_prompt: {
      title: 'Deleting place.',
      message: 'Are you sure you want to delete this place permanently ?'
    }
  },
  venue: {
    saved: {
      title: 'Venue saved !',
      message: 'Venue for {subject} has been saved successfuly.'
    }
  },
  payment: {
    successful: {
      title: 'Payment successful !',
      message: ''
    },
    failed: {
      title: 'Payment failed !',
      message: ''
    }
  },
  survey: {
    created: {
      title: 'Survey created !',
      message: 'Survey {subject} has been created successfuly.'
    },
    updated: {
      title: 'Survey updated !',
      message: 'Survey {subject} has been updated successfuly.'
    },
    completed: {
      title: 'Survey completed !',
      message: 'Thank you.'
    },
    leave_prompt: {
      title: 'You have unsaved changes !',
      message: 'Are you sure you want to leave and discard recent changes ?'
    },
    delete_prompt: {
      title: 'Deleting survey.',
      message: 'Are you sure you want to detele this survey permanently ?'
    }
  },
  account: {
    updated: {
      title: 'Account updated !',
      message: ''
    }
  },
  dashboard: {
    saved: {
      title: 'Dashboard saved !',
      message: ''
    },
    leave_prompt: {
      title: 'You have unsaved changes !',
      message: 'Are you sure you want to leave and discard recent changes ?'
    }
  },
  media: {
    delete_prompt: {
      title: 'Delete image ?',
      message: 'You are going to delete this image completely. Are you sure ?'
    }
  }
};
