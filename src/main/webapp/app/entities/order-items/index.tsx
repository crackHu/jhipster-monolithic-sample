import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import OrderItems from './order-items';
import OrderItemsDetail from './order-items-detail';
import OrderItemsUpdate from './order-items-update';
import OrderItemsDeleteDialog from './order-items-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={OrderItemsUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={OrderItemsUpdate} />
      <Route exact path={`${match.url}/:id`} component={OrderItemsDetail} />
      <Route path={match.url} component={OrderItems} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={OrderItemsDeleteDialog} />
  </>
);

export default Routes;
