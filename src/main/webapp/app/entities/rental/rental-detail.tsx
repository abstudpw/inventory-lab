import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './rental.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RentalDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rentalEntity = useAppSelector(state => state.rental.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rentalDetailsHeading">
          <Translate contentKey="inventoryLabApp.rental.detail.title">Rental</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rentalEntity.id}</dd>
          <dt>
            <span id="from">
              <Translate contentKey="inventoryLabApp.rental.from">From</Translate>
            </span>
          </dt>
          <dd>{rentalEntity.from ? <TextFormat value={rentalEntity.from} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="to">
              <Translate contentKey="inventoryLabApp.rental.to">To</Translate>
            </span>
          </dt>
          <dd>{rentalEntity.to ? <TextFormat value={rentalEntity.to} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="inventoryLabApp.rental.active">Active</Translate>
            </span>
          </dt>
          <dd>{rentalEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="inventoryLabApp.rental.rentedBy">Rented By</Translate>
          </dt>
          <dd>{rentalEntity.rentedBy ? rentalEntity.rentedBy.login : ''}</dd>
          <dt>
            <Translate contentKey="inventoryLabApp.rental.rentedItem">Rented Item</Translate>
          </dt>
          <dd>{rentalEntity.rentedItem ? rentalEntity.rentedItem.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/rental" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rental/${rentalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RentalDetail;
