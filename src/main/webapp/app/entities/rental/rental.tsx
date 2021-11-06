import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './rental.reducer';
import { IRental } from 'app/shared/model/rental.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Rental = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const rentalList = useAppSelector(state => state.rental.entities);
  const loading = useAppSelector(state => state.rental.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="rental-heading" data-cy="RentalHeading">
        <Translate contentKey="inventoryLabApp.rental.home.title">Rentals</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inventoryLabApp.rental.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inventoryLabApp.rental.home.createLabel">Create new Rental</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {rentalList && rentalList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="inventoryLabApp.rental.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.rental.from">From</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.rental.to">To</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.rental.active">Active</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.rental.rentedBy">Rented By</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.rental.rentedItem">Rented Item</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {rentalList.map((rental, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${rental.id}`} color="link" size="sm">
                      {rental.id}
                    </Button>
                  </td>
                  <td>{rental.from ? <TextFormat type="date" value={rental.from} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{rental.to ? <TextFormat type="date" value={rental.to} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{rental.active ? 'true' : 'false'}</td>
                  <td>{rental.rentedBy ? rental.rentedBy.login : ''}</td>
                  <td>{rental.rentedItem ? <Link to={`item/${rental.rentedItem.id}`}>{rental.rentedItem.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${rental.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${rental.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${rental.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="inventoryLabApp.rental.home.notFound">No Rentals found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Rental;
