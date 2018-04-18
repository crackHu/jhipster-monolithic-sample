import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Orders from './orders';
import OrdersDetail from './orders-detail';
import OrdersUpdate from './orders-update';
import OrdersDeleteDialog from './orders-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={OrdersUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={OrdersUpdate} />
      <Route exact path={`${match.url}/:id`} component={OrdersDetail} />
      <Route path={match.url} component={Orders} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={OrdersDeleteDialog} />
  </>
);

export default Routes;
