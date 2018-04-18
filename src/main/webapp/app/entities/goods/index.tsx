import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Goods from './goods';
import GoodsDetail from './goods-detail';
import GoodsUpdate from './goods-update';
import GoodsDeleteDialog from './goods-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={GoodsUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={GoodsUpdate} />
      <Route exact path={`${match.url}/:id`} component={GoodsDetail} />
      <Route path={match.url} component={Goods} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={GoodsDeleteDialog} />
  </>
);

export default Routes;
