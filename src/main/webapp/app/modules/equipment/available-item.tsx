import MaterialTable from "material-table";
import React from "react";
import {Column} from "@material-table/core";
import {IInventory} from "app/shared/model/inventory";
import {IItem} from "app/shared/model/item.model";
import {Grid, ListItem, TextField} from "@mui/material";
import { List } from "@material-ui/core";

interface AvailableItemProps {
  items: IItem[];
}

export const AvailableItem = (props: AvailableItemProps) => {

  const columns =
    [
      { id: 'Id', field: 'id' },
      { title: 'Is rented?', field: 'rented' },
      { title: 'Rented by',
        render: (rowData: IItem) => {
        const name = rowData && rowData.rental && rowData.rental.rentedBy ? rowData.rental.rentedBy.firstName + ' ' + rowData.rental.rentedBy.lastName : '';
        return (
          <TextField
            multiline={false}
            value={name}
            hiddenLabel={true}
          />
        )
        }},
    ] as Column<IItem>[];

  return (
      {
        props.items.map((rowData) => {
          const name = rowData && rowData.rental && rowData.rental.rentedBy ? rowData.rental.rentedBy.firstName + ' ' + rowData.rental.rentedBy.lastName : '';
          return
            <div>
              <div>Raz</div>
              <div>Raz</div>
            </div>
        })
      }
  )
}
