import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import CustomerAddr from './customer-addr';
import CustomerAddrDetail from './customer-addr-detail';
import CustomerAddrUpdate from './customer-addr-update';
import CustomerAddrDeleteDialog from './customer-addr-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={CustomerAddrUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={CustomerAddrUpdate} />
      <Route exact path={`${match.url}/:id`} component={CustomerAddrDetail} />
      <Route path={match.url} component={CustomerAddr} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={CustomerAddrDeleteDialog} />
  </>
);

export default Routes;
