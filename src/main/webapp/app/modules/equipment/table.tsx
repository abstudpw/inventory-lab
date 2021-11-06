import * as React from 'react';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import {IInventory} from "app/shared/model/inventory";
import {Button} from "@mui/material";


function Row(props: { row: IInventory }) {
  const { row } = props;
  const [open, setOpen] = React.useState(false);

  return (
    <React.Fragment>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
  <TableCell>
    <IconButton
      aria-label="expand row"
  size="small"
  onClick={() => setOpen(!open)}
>
  {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
  </IconButton>
  </TableCell>
        <TableCell ><img src={row.equipment.photo} style={{maxWidth: '100px'}} alt={'new'}/></TableCell>
        <TableCell component="th" scope="row">{row.equipment.name}</TableCell>
        <TableCell >{row.equipment.producer.name}</TableCell>
        <TableCell >{row.equipment.model}</TableCell>
        <TableCell ><a href={row.equipment.url}>Link</a></TableCell>
    <TableCell align="right" style={{maxWidth: '300px'}}>{row.equipment.description}</TableCell>
  </TableRow>
  <TableRow>
  <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
  <Collapse in={open} timeout="auto" unmountOnExit>
  <Box sx={{ margin: 1 }}>
  <Typography variant="h6" gutterBottom component="div">
    Items
    </Typography>
    <Table size="small" aria-label="purchases">
    <TableHead>
      <TableRow>
        <TableCell>Id</TableCell>
        <TableCell>Is Rented</TableCell>
        <TableCell>Rented by</TableCell>
        <TableCell>Rent it</TableCell>
  </TableRow>
  </TableHead>
  <TableBody>
  {row.items.map((item) => {
    const name = item && item.rental && item.rental.rentedBy ? item.rental.rentedBy.firstName + ' ' + item.rental.rentedBy.lastName : 'FREE';
    return (<TableRow key={item.id}>
      <TableCell component="th" scope="row">
        {item.id.toString()}
      </TableCell>
      <TableCell component="th" scope="row">
        {item.rented.toString()}
      </TableCell>
      <TableCell>{name.toString()}</TableCell>
      <TableCell>
        <Button>Click</Button>
      </TableCell>

    </TableRow>)
  })}
  </TableBody>
  </Table>
  </Box>
  </Collapse>
  </TableCell>
  </TableRow>
  </React.Fragment>
);
}

interface CollapsibleTableProps {
  rows: IInventory[],
}

export default function CollapsibleTable(props: CollapsibleTableProps) {
  return (
    <TableContainer component={Paper}>
    <Table aria-label="collapsible table">
      <TableHead>
        <TableRow>
          <TableCell />
          <TableCell >Photo</TableCell>
          <TableCell>Name</TableCell>
          <TableCell >Producer</TableCell>
          <TableCell >Model</TableCell>
          <TableCell >URL</TableCell>
          <TableCell align="right">Description</TableCell>
          <TableCell align="right">Protein&nbsp;(g)</TableCell>
  </TableRow>
  </TableHead>
  <TableBody>
  {props.rows.map((row) => (
      <Row key={row.equipment.id} row={row} />
))}
  </TableBody>
  </Table>
  </TableContainer>
);
}
