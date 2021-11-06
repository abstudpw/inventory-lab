import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EqTag from './eq-tag';
import EqTagDetail from './eq-tag-detail';
import EqTagUpdate from './eq-tag-update';
import EqTagDeleteDialog from './eq-tag-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EqTagUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EqTagUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EqTagDetail} />
      <ErrorBoundaryRoute path={match.url} component={EqTag} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EqTagDeleteDialog} />
  </>
);

export default Routes;
