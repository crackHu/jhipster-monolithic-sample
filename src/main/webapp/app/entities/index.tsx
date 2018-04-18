import * as React from 'react';
// tslint:disable-next-line:no-unused-variable
import { Route, Switch } from 'react-router-dom';

import Travelrecord from './travelrecord';
import Company from './company';
import Goods from './goods';
import Hotnews from './hotnews';
import Employee from './employee';
import Customer from './customer';
import Orders from './orders';
import OrderItems from './order-items';
import CustomerAddr from './customer-addr';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <Route path={`${match.url}/travelrecord`} component={Travelrecord}/>
      <Route path={`${match.url}/company`} component={Company} />
      <Route path={`${match.url}/goods`} component={Goods} />
      <Route path={`${match.url}/hotnews`} component={Hotnews} />
      <Route path={`${match.url}/employee`} component={Employee} />
      <Route path={`${match.url}/customer`} component={Customer} />
      <Route path={`${match.url}/orders`} component={Orders} />
      <Route path={`${match.url}/order-items`} component={OrderItems} />
      <Route path={`${match.url}/customer-addr`} component={CustomerAddr} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
