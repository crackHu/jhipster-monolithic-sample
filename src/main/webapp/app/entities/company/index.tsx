import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Company from './company';
import CompanyDetail from './company-detail';
import CompanyUpdate from './company-update';
import CompanyDeleteDialog from './company-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={CompanyUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={CompanyUpdate} />
      <Route exact path={`${match.url}/:id`} component={CompanyDetail} />
      <Route path={match.url} component={Company} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={CompanyDeleteDialog} />
  </>
);

export default Routes;
