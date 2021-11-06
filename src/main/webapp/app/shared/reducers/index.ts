import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
import sessions from 'app/modules/account/sessions/sessions.reducer';
// prettier-ignore
import producer from 'app/entities/producer/producer.reducer';
// prettier-ignore
import tag from 'app/entities/tag/tag.reducer';
// prettier-ignore
import equipment from 'app/entities/equipment/equipment.reducer';
// prettier-ignore
import item from 'app/entities/item/item.reducer';
// prettier-ignore
import rental from 'app/entities/rental/rental.reducer';
// prettier-ignore
import eqTag from 'app/entities/eq-tag/eq-tag.reducer';
import inventory from 'app/modules/equipment/inventory-reducer'
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  producer,
  tag,
  equipment,
  item,
  rental,
  eqTag,
  inventory,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
