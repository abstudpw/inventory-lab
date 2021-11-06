import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rental.reducer';
import { IRental } from 'app/shared/model/rental.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RentalUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const items = useAppSelector(state => state.item.entities);
  const rentalEntity = useAppSelector(state => state.rental.entity);
  const loading = useAppSelector(state => state.rental.loading);
  const updating = useAppSelector(state => state.rental.updating);
  const updateSuccess = useAppSelector(state => state.rental.updateSuccess);

  const handleClose = () => {
    props.history.push('/rental');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getItems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.from = convertDateTimeToServer(values.from);
    values.to = convertDateTimeToServer(values.to);

    const entity = {
      ...rentalEntity,
      ...values,
      rentedBy: users.find(it => it.id.toString() === values.rentedById.toString()),
      rentedItem: items.find(it => it.id.toString() === values.rentedItemId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          from: displayDefaultDateTime(),
          to: displayDefaultDateTime(),
        }
      : {
          ...rentalEntity,
          from: convertDateTimeFromServer(rentalEntity.from),
          to: convertDateTimeFromServer(rentalEntity.to),
          rentedById: rentalEntity?.rentedBy?.id,
          rentedItemId: rentalEntity?.rentedItem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="inventoryLabApp.rental.home.createOrEditLabel" data-cy="RentalCreateUpdateHeading">
            <Translate contentKey="inventoryLabApp.rental.home.createOrEditLabel">Create or edit a Rental</Translate>
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
                  id="rental-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('inventoryLabApp.rental.from')}
                id="rental-from"
                name="from"
                data-cy="from"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('inventoryLabApp.rental.to')}
                id="rental-to"
                name="to"
                data-cy="to"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('inventoryLabApp.rental.active')}
                id="rental-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="rental-rentedBy"
                name="rentedById"
                data-cy="rentedBy"
                label={translate('inventoryLabApp.rental.rentedBy')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="rental-rentedItem"
                name="rentedItemId"
                data-cy="rentedItem"
                label={translate('inventoryLabApp.rental.rentedItem')}
                type="select"
                required
              >
                <option value="" key="0" />
                {items
                  ? items.filter(item => !item.rented)
                    .map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rental" replace color="info">
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

export default RentalUpdate;
