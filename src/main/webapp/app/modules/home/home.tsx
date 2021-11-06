import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import {IInventory} from "app/shared/model/inventory";
import Inventory from "app/modules/equipment/inventory";


export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const itemList = useAppSelector(state => state.inventory.entities) as IInventory[];

  return (
    <Inventory rows={itemList}/>
  );
};

export default Home;
