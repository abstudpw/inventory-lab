import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Equipment from './equipment';
import EquipmentDetail from './equipment-detail';
import EquipmentUpdate from './equipment-update';
import EquipmentDeleteDialog from './equipment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EquipmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EquipmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EquipmentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Equipment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EquipmentDeleteDialog} />
  </>
);

export default Routes;
