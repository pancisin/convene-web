package com.pancisin.bookster.model.enums

enum class PageState {

  /*
   * PUBLISHED: Visible to public.
   */
  PUBLISHED,

  /*
   * DEACTIVATED : Hidden for public (prepared for publish, management is
   * allowed)
   */
  DEACTIVATED,

  /*
   * BLOCKED : Management is blocked due to upaid invoices but visible for
   * public
   */
  BLOCKED,

  /*
   * BANNED : Page is completely banned - not visible to anybody except super
   * administrator.
   */
  BANNED,

  /*
   * DETELED : Page is completely deleted - not visible to anyone
   */
  DELETED
}
