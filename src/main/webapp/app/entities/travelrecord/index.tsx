import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Travelrecord from './travelrecord';
import TravelrecordDetail from './travelrecord-detail';
import TravelrecordUpdate from './travelrecord-update';
import TravelrecordDeleteDialog from './travelrecord-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={TravelrecordUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={TravelrecordUpdate} />
      <Route exact path={`${match.url}/:id`} component={TravelrecordDetail} />
      <Route path={match.url} component={Travelrecord} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={TravelrecordDeleteDialog} />
  </>
);

export default Routes;
