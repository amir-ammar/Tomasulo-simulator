import InputForm from './components/InputForm';
import DataTable from './components/DataTable';
import { makeStyles } from '@material-ui/core/styles';
import { useState } from 'react';
import { Button } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    margin: '1rem',
  },
  title: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
}));

function App() {
  const classes = useStyles();
  const [start, setStart] = useState(false);
  const [data, setData] = useState({});

  const fetchData = () => {
    fetch('http://localhost:8080/', {
      method: 'GET',
    }).then((res) => {
      res.body
        .getReader()
        .read()
        .then(({ value }) => {
          console.log(JSON.parse(new TextDecoder('utf-8').decode(value)));
          setData(JSON.parse(new TextDecoder('utf-8').decode(value)));
        });
    });
  };

  const handleStart = (value) => {
    setStart(value);
    fetchData();
  };

  const generateAddSubStation = () => {
    let addSubStation = [];
    if (data?.reservationStation?.AddSubStation) {
      addSubStation = data.reservationStation.AddSubStation;
    } else {
      for (let i = 0; i < 3; i++) {
        addSubStation.push({
          label: `A${i}`,
          busy: 'false',
          op: '',
          vj: '',
          vk: '',
          qj: '',
          qk: '',
          a: '',
        });
      }
    }
    return addSubStation;
  };

  const generateMulDivStation = () => {
    let mulDivStation = [];
    if (data?.reservationStation?.MulDivStation) {
      mulDivStation = data.reservationStation.MulDivStation;
    } else {
      for (let i = 0; i < 2; i++) {
        mulDivStation.push({
          label: `M${i}`,
          busy: 'false',
          op: '',
          vj: '',
          vk: '',
          qj: '',
          qk: '',
          a: '',
        });
      }
    }

    return mulDivStation;
  };

  const generateRegisterFile = () => {
    let registerFile = [];
    if (data?.registerFile) {
      registerFile = data.registerFile;
    } else {
      for (let i = 0; i < 32; i++) {
        registerFile.push({
          label: `R${i}`,
          Qi: '',
          content: '',
        });
      }
    }

    return registerFile;
  };

  return (
    <div className={classes.root}>
      <header>
        <h1>Tomasulo simulator</h1>
        {start && (
          <Button
            variant='contained'
            color='primary'
            style={{
              marginTop: '1rem',
            }}
            onClick={() => fetchData()}
          >
            Next
          </Button>
        )}
      </header>
      {!start && <InputForm onStart={(value) => handleStart(value)} />}
      {start && (
        <>
          <h3> Clock: {data?.cycle}</h3>
          <div
            style={{
              display: 'flex',
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <div
              style={{
                marginRight: '1rem',
              }}
            >
              <h2 className={classes.title}>Add/Sub station</h2>
              <DataTable
                headers={['Name', 'Busy', 'Op', 'Vj', 'Vk', 'Qj', 'Qk', 'A']}
                rows={generateAddSubStation()}
              />
            </div>
            <div>
              <h2 className={classes.title}>Mul/Div station</h2>
              <DataTable
                headers={['Name', 'Busy', 'Op', 'Vj', 'Vk', 'Qj', 'Qk', 'A']}
                rows={generateMulDivStation()}
                className={classes.mulStation}
              />
            </div>
          </div>
          <div
            style={{
              marginTop: '1rem',
            }}
          >
            <h2 className={classes.title}>Register File</h2>
            <DataTable
              headers={['Name', 'Qi', 'Content']}
              rows={generateRegisterFile()}
            />
          </div>
        </>
      )}
    </div>
  );
}

export default App;
