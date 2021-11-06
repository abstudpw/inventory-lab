import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Producer from './producer';
import ProducerDetail from './producer-detail';
import ProducerUpdate from './producer-update';
import ProducerDeleteDialog from './producer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProducerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProducerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProducerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Producer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProducerDeleteDialog} />
  </>
);

export default Routes;
