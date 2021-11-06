import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/producer">
      <Translate contentKey="global.menu.entities.producer" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/tag">
      <Translate contentKey="global.menu.entities.tag" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/equipment">
      <Translate contentKey="global.menu.entities.equipment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/item">
      <Translate contentKey="global.menu.entities.item" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/rental">
      <Translate contentKey="global.menu.entities.rental" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/eq-tag">
      <Translate contentKey="global.menu.entities.eqTag" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
