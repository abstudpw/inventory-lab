import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './equipment.reducer';
import { IEquipment } from 'app/shared/model/equipment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Equipment = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const equipmentList = useAppSelector(state => state.equipment.entities);
  const loading = useAppSelector(state => state.equipment.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="equipment-heading" data-cy="EquipmentHeading">
        <Translate contentKey="inventoryLabApp.equipment.home.title">Equipment</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inventoryLabApp.equipment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inventoryLabApp.equipment.home.createLabel">Create new Equipment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {equipmentList && equipmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.model">Model</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.photo">Photo</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.url">Url</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.equipment.producer">Producer</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {equipmentList.map((equipment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${equipment.id}`} color="link" size="sm">
                      {equipment.id}
                    </Button>
                  </td>
                  <td>{equipment.name}</td>
                  <td>{equipment.model}</td>
                  <td>{equipment.photo}</td>
                  <td>{equipment.description}</td>
                  <td>{equipment.url}</td>
                  <td>{equipment.producer ? <Link to={`producer/${equipment.producer.id}`}>{equipment.producer.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${equipment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${equipment.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${equipment.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="inventoryLabApp.equipment.home.notFound">No Equipment found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Equipment;
