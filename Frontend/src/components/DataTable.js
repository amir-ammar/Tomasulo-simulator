import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

const DataTable = ({ headers, rows }) => {
  const classes = useStyles();

  return (
    <TableContainer component={Paper}>
      <Table className={classes.table} aria-label='simple table'>
        <TableHead>
          <TableRow>
            {headers.map((header) => {
              return <TableCell>{header}</TableCell>;
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row?.name}>
              {<TableCell key={row?.label + '0'}>{row?.label}</TableCell>}
              {(row.label?.charAt(0) === 'A' ||
                row.label?.charAt(0) === 'M') && (
                <>
                  {<TableCell key={row?.label + '1'}>{row?.busy}</TableCell>}
                  {<TableCell key={row?.label + '2'}>{row?.op}</TableCell>}
                  {<TableCell key={row?.label + '3'}>{row?.vj}</TableCell>}
                  {<TableCell key={row?.label + '4'}>{row?.vk}</TableCell>}
                  {<TableCell key={row?.label + '5'}>{row?.qj}</TableCell>}
                  {<TableCell key={row?.label + '6'}>{row?.qk}</TableCell>}
                  {<TableCell key={row?.label + '7'}>{row?.a}</TableCell>}
                </>
              )}
              {row.label?.charAt(0) === 'R' && (
                <>
                  {<TableCell key={row?.label + '8'}>{row?.Qi}</TableCell>}
                  {<TableCell key={row?.label + '9'}>{row?.content}</TableCell>}
                </>
              )}
              {row.label?.charAt(0) === 'L' && (
                <>
                  {<TableCell key={row?.label + '10'}>{row?.busy}</TableCell>}
                  {
                    <TableCell key={row?.label + '11'}>
                      {row?.address}
                    </TableCell>
                  }
                </>
              )}
              {row.label?.charAt(0) === 'S' && (
                <>
                  {<TableCell key={row?.label + '12'}>{row?.busy}</TableCell>}
                  {
                    <TableCell key={row?.label + '13'}>
                      {row?.address}
                    </TableCell>
                  }
                  {<TableCell key={row?.label + '14'}>{row?.V}</TableCell>}
                  {<TableCell key={row?.label + '15'}>{row?.Q}</TableCell>}
                </>
              )}
              {row.label?.includes('Mem') && (
                <>
                  {
                    <TableCell key={row?.label + '16'}>
                      {row?.content}
                    </TableCell>
                  }
                </>
              )}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default DataTable;
