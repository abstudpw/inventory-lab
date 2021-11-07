import React, {Component, useEffect} from 'react'
import ReactDOM from 'react-dom'
import MaterialTable from 'material-table'
import { forwardRef } from 'react';
import {IInventory} from "app/shared/model/inventory";
import {Column} from "@material-table/core";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {getEntities} from "app/modules/equipment/inventory-reducer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";

interface InventoryProps {
  rows: IInventory[];
}

function Inventory(props: InventoryProps) {

  window.Object.freeze = function(obj) { return obj }

  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  const dispatch = useAppDispatch();
  const list = props.rows.map(x =>Object.assign({}, x))

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const columns =
    [
      { title: 'Photo',
        filtering: false,
      render: (rowData) => <img src={rowData.equipment.photo} style={{maxWidth: '100px'}} alt={'new'}/>
      },
      { title: 'Name', field: 'equipment.name', filtering: false,  },
      { title: 'Model', field: 'equipment.producer.name', filtering: false },
      { title: 'Model', field: 'equipment.model', filtering: false },
      { title: 'Url',
        cellStyle:{fontSize: '10px'},
        render: (rowData) => <text><a href={rowData.equipment.url}>Link</a></text>
      },
      { title: 'Tags',
        filtering: true,
        width: '15%',
        cellStyle:{fontSize: '10px'},
        customFilterAndSearch: (filter, rowData) => rowData.tags.map(tag => tag.name.toLowerCase()).find(name => name.includes(filter.toLowerCase())),
        render: (rowData) => <text>{rowData.tags.map(tag => tag.name.toString()).join(', ')}</text>
        },
      { title: 'Description', field: 'equipment.description', filtering: false, width: '25%', cellStyle:{fontSize: '12px'},},
      { title: 'Availability',
        filtering: false,
        cellStyle: {fontSize: '12px'},
        render: (rowData) => {
          const all = rowData.items.length;
          const av = rowData.items.filter(item => !item.rented).length;
          return (
            <text>{av}/{all}</text>
          )
        }
      },
    ] as Column<IInventory>[];


    return (
      <MaterialTable
        columns={columns}
        data={list}
        options={{
          filtering: true,
        }}
        title="Laboratory inventory"
        detailPanel={rowData => {
          return isAuthenticated && (
                <Table size="small" aria-label="purchases">
                  <TableHead>
                    <TableRow>
                      <TableCell>Id</TableCell>
                      <TableCell>Is Rented</TableCell>
                      <TableCell>Rented by</TableCell>
                      <TableCell>From</TableCell>
                      <TableCell>To</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {rowData.items.map((item) => {
                      const name = item && item.rental && item.rental.rentedBy ? item.rental.rentedBy.login : '-';
                      const from = item && item.rental && item.rental.from ? item.rental.from : '-';
                      const to =item && item.rental && item.rental.to ? item.rental.to : '-';
                      return (<TableRow key={item.id}>
                        <TableCell component="th" scope="row">
                          {item.id.toString()}
                        </TableCell>
                        <TableCell component="th" scope="row">
                          {item.rented.toString()}
                        </TableCell>
                        <TableCell>{name.toString()}</TableCell>
                        <TableCell>{from.toString()}</TableCell>
                        <TableCell>{to.toString()}</TableCell>

                      </TableRow>)
                    })}
                  </TableBody>
                </Table>
              )
            }
        }
        onRowClick={(event, rowData, togglePanel) => togglePanel()}
      />
    )
  }

function areEqual(prevProps, nextProps) {
  return prevProps.rows === nextProps.rows
}

export default React.memo(Inventory, areEqual);

