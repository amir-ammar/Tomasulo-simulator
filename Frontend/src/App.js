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

  const generateLoadBuffer = () => {
    let loadBuffer = [];
    if (data?.loadBuffer) {
      loadBuffer = data.loadBuffer;
    } else {
      for (let i = 0; i < 3; i++) {
        loadBuffer.push({
          label: `L${i}`,
          busy: 'false',
          address: '',
        });
      }
    }
    return loadBuffer;
  };

  const generateStoreBuffer = () => {
    let storeBuffer = [];
    if (data?.storeBuffer) {
      storeBuffer = data.storeBuffer;
    } else {
      for (let i = 0; i < 3; i++) {
        storeBuffer.push({
          label: `S${i}`,
          busy: 'false',
          address: '',
          V: '',
          Q: '',
        });
      }
    }
    return storeBuffer;
  };

  const generateMemory = () => {
    let memory = [];
    if (data?.memory) {
      memory = data.memory;
    } else {
      for (let i = 0; i < 32; i++) {
        memory.push({
          label: `Mem${i}`,
          content: '',
        });
      }
    }
    return memory;
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
          <h3>Clock: {data?.cycle}</h3>
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
              />
            </div>
          </div>
          <div
            style={{
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'center',
              marginTop: '1rem',
            }}
          >
            <div
              style={{
                marginRight: '1rem',
              }}
            >
              <h2 className={classes.title}>Register File</h2>
              <DataTable
                headers={['Name', 'Qi', 'Content']}
                rows={generateRegisterFile()}
              />
            </div>
            <div>
              <h2 className={classes.title}>Load Buffer</h2>
              <DataTable
                headers={['Name', 'Busy', 'Address']}
                rows={generateLoadBuffer()}
              />

              <h2 className={classes.title}>Store Buffer</h2>
              <DataTable
                headers={['Name', 'Busy', 'Address', 'V', 'Q']}
                rows={generateStoreBuffer()}
              />

              <h2 className={classes.title}>Memory</h2>
              <DataTable
                headers={['Name', 'Address']}
                rows={generateMemory()}
              />
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default App;
