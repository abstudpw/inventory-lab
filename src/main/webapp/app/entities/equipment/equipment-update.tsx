import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProducer } from 'app/shared/model/producer.model';
import { getEntities as getProducers } from 'app/entities/producer/producer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './equipment.reducer';
import { IEquipment } from 'app/shared/model/equipment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EquipmentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const producers = useAppSelector(state => state.producer.entities);
  const equipmentEntity = useAppSelector(state => state.equipment.entity);
  const loading = useAppSelector(state => state.equipment.loading);
  const updating = useAppSelector(state => state.equipment.updating);
  const updateSuccess = useAppSelector(state => state.equipment.updateSuccess);

  const handleClose = () => {
    props.history.push('/equipment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProducers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...equipmentEntity,
      ...values,
      producer: producers.find(it => it.id.toString() === values.producerId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...equipmentEntity,
          producerId: equipmentEntity?.producer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inventoryLabApp.equipment.home.createOrEditLabel" data-cy="EquipmentCreateUpdateHeading">
            <Translate contentKey="inventoryLabApp.equipment.home.createOrEditLabel">Create or edit a Equipment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="equipment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inventoryLabApp.equipment.name')}
                id="equipment-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('inventoryLabApp.equipment.model')}
                id="equipment-model"
                name="model"
                data-cy="model"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('inventoryLabApp.equipment.photo')}
                id="equipment-photo"
                name="photo"
                data-cy="photo"
                type="text"
              />
              <ValidatedField
                label={translate('inventoryLabApp.equipment.description')}
                id="equipment-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('inventoryLabApp.equipment.url')} id="equipment-url" name="url" data-cy="url" type="text" />
              <ValidatedField
                id="equipment-producer"
                name="producerId"
                data-cy="producer"
                label={translate('inventoryLabApp.equipment.producer')}
                type="select"
                required
              >
                <option value="" key="0" />
                {producers
                  ? producers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/equipment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EquipmentUpdate;
