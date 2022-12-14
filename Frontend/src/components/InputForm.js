import { Button, TextField } from '@material-ui/core';
import React, { useRef, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
  input: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    margin: '1rem',
  },
  text: {
    margin: '1rem',
    width: '30rem',
  },
  number: {
    width: '5rem',
  },
}));

function InputForm({ onStart }) {
  const [instructions, setInstructions] = useState(1);
  const classes = useStyles();
  const itemRef = useRef();

  const collectData = () => {
    const data = [];

    for (let i = 0; i < itemRef.current.children.length; i++) {
      data.push(
        itemRef.current.children[
          i
        ].children[0].children[1].children[0].value.toUpperCase() +
          ' ' +
          itemRef.current.children[i].children[1].children[1].children[0].value
      );
    }
    console.log(data);
    return data;
  };

  const sendData = async () => {
    try {
      const data = collectData();
      console.log(data);
      const response = await fetch('http://localhost:8080/', {
        method: 'POST',
        body: JSON.stringify(data),
      });

      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const handleStart = () => {
    onStart(true);
    sendData();
  };

  return (
    <form>
      <div ref={itemRef}>
        {[...Array(instructions)].map((e, i) => {
          return (
            <div className={classes.input} key={i}>
              <TextField
                id='standard-basic'
                label='Enter your instruction'
                className={classes.text}
              />
              <TextField
                id='standard-number'
                label='Latency'
                type='number'
                className={classes.number}
              />
            </div>
          );
        })}
      </div>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <div
          style={{
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
            margin: '1rem',
          }}
        >
          <Button
            variant='contained'
            style={{ marginRight: '1rem' }}
            onClick={() => setInstructions(instructions + 1)}
          >
            Add instruction
          </Button>
          <Button
            variant='contained'
            onClick={() => setInstructions(instructions - 1)}
          >
            Remove instruction
          </Button>
        </div>
        <Button variant='contained' color='primary' onClick={handleStart}>
          Start simulation
        </Button>
      </div>
    </form>
  );
}

export default InputForm;
