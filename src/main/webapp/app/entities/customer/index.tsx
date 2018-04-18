import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Customer from './customer';
import CustomerDetail from './customer-detail';
import CustomerUpdate from './customer-update';
import CustomerDeleteDialog from './customer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={CustomerUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={CustomerUpdate} />
      <Route exact path={`${match.url}/:id`} component={CustomerDetail} />
      <Route path={match.url} component={Customer} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={CustomerDeleteDialog} />
  </>
);

export default Routes;
