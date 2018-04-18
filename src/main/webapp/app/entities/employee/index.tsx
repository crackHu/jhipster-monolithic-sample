import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Employee from './employee';
import EmployeeDetail from './employee-detail';
import EmployeeUpdate from './employee-update';
import EmployeeDeleteDialog from './employee-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={EmployeeUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={EmployeeUpdate} />
      <Route exact path={`${match.url}/:id`} component={EmployeeDetail} />
      <Route path={match.url} component={Employee} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={EmployeeDeleteDialog} />
  </>
);

export default Routes;
