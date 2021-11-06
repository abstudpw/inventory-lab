import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './equipment.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EquipmentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const equipmentEntity = useAppSelector(state => state.equipment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="equipmentDetailsHeading">
          <Translate contentKey="inventoryLabApp.equipment.detail.title">Equipment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="inventoryLabApp.equipment.name">Name</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.name}</dd>
          <dt>
            <span id="model">
              <Translate contentKey="inventoryLabApp.equipment.model">Model</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.model}</dd>
          <dt>
            <span id="photo">
              <Translate contentKey="inventoryLabApp.equipment.photo">Photo</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.photo}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="inventoryLabApp.equipment.description">Description</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.description}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="inventoryLabApp.equipment.url">Url</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.url}</dd>
          <dt>
            <Translate contentKey="inventoryLabApp.equipment.producer">Producer</Translate>
          </dt>
          <dd>{equipmentEntity.producer ? equipmentEntity.producer.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/equipment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/equipment/${equipmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EquipmentDetail;
