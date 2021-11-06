import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './eq-tag.reducer';
import { IEqTag } from 'app/shared/model/eq-tag.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EqTag = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const eqTagList = useAppSelector(state => state.eqTag.entities);
  const loading = useAppSelector(state => state.eqTag.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="eq-tag-heading" data-cy="EqTagHeading">
        <Translate contentKey="inventoryLabApp.eqTag.home.title">Eq Tags</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="inventoryLabApp.eqTag.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="inventoryLabApp.eqTag.home.createLabel">Create new Eq Tag</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {eqTagList && eqTagList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="inventoryLabApp.eqTag.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.eqTag.equipment">Equipment</Translate>
                </th>
                <th>
                  <Translate contentKey="inventoryLabApp.eqTag.tag">Tag</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {eqTagList.map((eqTag, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${eqTag.id}`} color="link" size="sm">
                      {eqTag.id}
                    </Button>
                  </td>
                  <td>{eqTag.equipment ? <Link to={`equipment/${eqTag.equipment.id}`}>{eqTag.equipment.name}</Link> : ''}</td>
                  <td>{eqTag.tag ? <Link to={`tag/${eqTag.tag.id}`}>{eqTag.tag.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${eqTag.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${eqTag.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${eqTag.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="inventoryLabApp.eqTag.home.notFound">No Eq Tags found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default EqTag;
