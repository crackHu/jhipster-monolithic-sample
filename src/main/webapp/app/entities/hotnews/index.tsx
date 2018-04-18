import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Hotnews from './hotnews';
import HotnewsDetail from './hotnews-detail';
import HotnewsUpdate from './hotnews-update';
import HotnewsDeleteDialog from './hotnews-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={HotnewsUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={HotnewsUpdate} />
      <Route exact path={`${match.url}/:id`} component={HotnewsDetail} />
      <Route path={match.url} component={Hotnews} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={HotnewsDeleteDialog} />
  </>
);

export default Routes;
