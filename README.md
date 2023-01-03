# Tomasulo-simulator

- [Tomasulo-simulator](#tomasulo-simulator)
  - [Introduction](#introduction)
  - [frontend-structure](#frontend-structure)
  - [backend-structure](#backend-structure)
  - [Communication-between-backend-and-frontend](#communication-between-backend-and-frontend)
  - [Explanation-figure](#explanation-figure)
  - [Communication-between-backend-components](#communication-between-backend-components)
  - [Run](#run)
  - [Features](#features)
  - [Contributors](#contributors)
  - [Contributing](#contributing)
  - [License](#license)

## Introduction

This is a Tomasulo simulator written in java. It simulates the execution of a given assembly code. It is a project for the course Computer Architecture.

## frontend-structure

```bash
.
├── package.json
├── package-lock.json
├── public
│   ├── favicon.ico
│   ├── index.html
│   ├── logo192.png
│   ├── logo512.png
│   ├── manifest.json
│   └── robots.txt
├── README.md
└── src
    ├── App.js
    ├── components
    │   ├── DataTable.js
    │   └── InputForm.js
    └── index.js
```

## backend-structure

```bash
.
├── ALUPackage
│   └── ALU.java
├── DataBusPackage
│   ├── DataBusItem.java
│   ├── DataBus.java
│   ├── DataBusObserver.java
│   └── DataBusSubject.java
├── InstructionQueuePackage
│   ├── Instruction.java
│   ├── InstructionQueue.java
│   ├── InstructionQueueObserver.java
│   └── InstructionQueueSubject.java
├── LoadBufferPackage
│   ├── LoadBufferItem.java
│   ├── LoadBuffer.java
│   ├── LoadBufferObserver.java
│   └── LoadBufferSubject.java
├── MainPackage
│   ├── Executor.java
│   ├── ExecutorObserver.java
│   └── ExecutorSubject.java
├── MemoryPackage
│   └── Memory.java
├── MessagesPackage
│   └── Message.java
├── RegisterFilePackage
│   ├── RegisterFileItem.java
│   ├── RegisterFile.java
│   ├── RegisterFileObserver.java
│   └── RegisterFileSubject.java
├── ReservationStationPackage
│   ├── ReservationStationItem.java
│   ├── ReservationStation.java
│   ├── ReservationStationObserver.java
│   └── ReservationStationSubject.java
├── Server.java
└── StoreBuffer
    ├── StoreBufferItem.java
    ├── StoreBuffer.java
    ├── StoreBufferObserver.java
    └── StoreBufferSubject.java
```

## Communication-between-backend-and-frontend

1. Frontend sends the assembly code to the backend using a POST request.
2. The backend parses the assembly code and creates an instruction queue, a register file, a memory, a data bus, a load buffer, a store buffer, a reservation station, and an ALU.
3. The front end sends a GET request to the backend to get the state of the simulator.
4. The backend sends the state of the simulator to the frontend using a JSON object.
5. The frontend displays the state of the simulator.

## Explanation-figure
![doc drawio](https://user-images.githubusercontent.com/75969308/210338950-7af7f1b2-9814-43f3-8d37-37ee313b5bed.svg)

## Communication-between-backend-components


1. Executor instance notifies Load Buffer instance, Reservation Station instance, and Store Buffer instance respectively to update their state at each clock cycle.

2. Data bus instance notifies the reservation station instance, store buffer instance, and register file instance with the value of the data bus at each clock cycle.

3. Load buffer instance notifies the reservation station instance with the ready to write instruction to determine who will write to the data bus in this clock cycle, and this is done by comparing the ready to write instruction in both the reservation station and the load buffer over the dependency instructions in the reservation station and the store buffer.

4. In this step depending on the instruction type :-

   - in case of **(ADD, SUB, MUL, DIV)**

     - ```java
         public void updateClock() {
             writeToDataBus();
             removeFinishedInstructions();
             updateLatency();
             notifyInstructionQueue();
             notifyRegisterFile();
             newInstruction = false;
             writeToDataBus = true;
         }
       ```

- ### Explaination :-

            - we will write to data bus any ready instruction if **writeToDataBus** is true, and this will be true if the instruction has higher priority than all instruction in reservation station, and load buffer
            - we will remove any finished instruction from reservation station
            - we will update the latency of any instruction in reservation station
            - we will notify the instruction queue that we have an empty slot in reservation station, and get a new instruction from it if there is any.
            - we will notify the register file with the new updated registers and ask it to send the required registers values.
            - we will set **newInstruction** to false
            - we will set **writeToDataBus** to true

  - in case of **(LD)**

    - ````java
            public void updateClock() {
                writeToDataBus();
                removeFinishedInstructions();
                updateLatency();
                notifyInstructionQueue();
                notifyRegFile();
                newInstruction = false;
            }
            ```
      ````

- ### Explaination :-

            - we will write to data bus any ready instruction if **writeToDataBus** is true, and this will be true if the instruction has higher priority than all instruction in reservation station, and load buffer

            - we will remove any finished instruction from load buffer.
            - we will update the latency of any instruction in load buffer.
            - we will notify the instruction queue that we have an empty slot in load buffer, and get a new instruction from it if there is any.
            - we will notify the register file with the new updated registers.
            - we will set **newInstruction** to false.

  - in case of **(SD)**

    - ````java
            public void updateClock() {
               removeFinishedInstructions();
                updateLatency();
                notifyInstructionQueue();
                notifyRegisterFile();
                newInstruction = false;
            }
            ```
      ````

- ### Explaination :-

            - we will write the result of the register to the memory.
            - we will remove any finished instruction from store buffer.
            - we will update the latency of any instruction in store buffer.
            - we will notify the instruction queue that we have an empty slot in store buffer, and get a new instruction from it if there is any.
            - we will notify the register file with the new required registers, and ask it to send the required registers values.
            - we will set **newInstruction** to false.

## Run

    -  run the server
        - cd backend
        - javac Server.java
        - java Server
    - run the frontend
        - cd frontend
        - npm install
        - node index.js
        - open http://localhost:3000/ in your browser

## Features

- [x] 1. The simulator should be able to execute the following instructions:
  - [x] ADD
  - [x] SUB
  - [x] MUL
  - [x] DIV
  - [x] LD
  - [x] SD

## Contributors

- [Abdelrahman Fekri](https://github.com/abdelrahmanfekri)
- [Ahmed Elbltagy](https://github.com/Elbltagy2)
- [FahdHassan](https://github.com/FahdHassan)
- [Miichael-Yassa](https://github.com/Miichael-Yassa)
- [Amir ammar](https://github.com/amir-ammar)

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](https://choosealicense.com/licenses/mit/)
