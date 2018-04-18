import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
import travelrecord from 'app/entities/travelrecord/travelrecord.reducer';
import company from 'app/entities/company/company.reducer';
import goods from 'app/entities/goods/goods.reducer';
import hotnews from 'app/entities/hotnews/hotnews.reducer';
import employee from 'app/entities/employee/employee.reducer';
import customer from 'app/entities/customer/customer.reducer';
import orders from 'app/entities/orders/orders.reducer';
import orderItems from 'app/entities/order-items/order-items.reducer';
import customerAddr from 'app/entities/customer-addr/customer-addr.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export default combineReducers({
  authentication,
  locale,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  travelrecord,
  company,
  goods,
  hotnews,
  employee,
  customer,
  orders,
  orderItems,
  customerAddr,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});
