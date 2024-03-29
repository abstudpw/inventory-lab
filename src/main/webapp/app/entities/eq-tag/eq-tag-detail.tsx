import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './eq-tag.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EqTagDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const eqTagEntity = useAppSelector(state => state.eqTag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eqTagDetailsHeading">
          <Translate contentKey="inventoryLabApp.eqTag.detail.title">EqTag</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eqTagEntity.id}</dd>
          <dt>
            <Translate contentKey="inventoryLabApp.eqTag.equipment">Equipment</Translate>
          </dt>
          <dd>{eqTagEntity.equipment ? eqTagEntity.equipment.name : ''}</dd>
          <dt>
            <Translate contentKey="inventoryLabApp.eqTag.tag">Tag</Translate>
          </dt>
          <dd>{eqTagEntity.tag ? eqTagEntity.tag.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/eq-tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/eq-tag/${eqTagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EqTagDetail;
