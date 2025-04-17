# Two Player Communication System

## Overview

This project implements a simple message exchange system between two players using a threaded architecture. Each player
runs in a separate thread and exchanges messages through a shared message queue. The system demonstrates basic
inter-thread communication patterns and concurrent programming concepts in Java.

## **Table of Contents**

- [Features](#features)
- [Project Structure](#project-structure)
- [Class Descriptions](#class-descriptions)
    - [Player.java](#playerjava)
    - [MessageQueue.java](#messagequeuejava)
    - [Message.java](#messagejava)
    - [GameEngine.java](#gameenginejava)
    - [Main.java](#mainjava)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Running the Application](#running-the-application)
    - [Running the Tests](#running-the-tests)
- [Test Coverage](#test-coverage)
- [Dependencies](#dependencies)
- [Message Format](#message-format)
- [Concurrency Aspects](#concurrency-aspects)

## Features

- **Threaded Architecture**: Each player runs in a separate thread, operating concurrently
- **Message Queue System**: Thread-safe communication using BlockingQueue
- **Asynchronous Communication**: Players can send and receive messages independently
- **Message Tracking**: Each message includes a counter and references previous messages
- **Structured Message Format**: Messages are tagged with counters and reference replies
- **Configurable Message Limit**: Players exchange messages up to a predefined limit
- **Centralized Game Coordination**: The GameEngine handles initialization and shutdown

## Project Structure

```
TwoPlayerCommunication/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bdkamaci/
│   │   │           ├── GameEngine.java     - Coordinates players and game flow
│   │   │           ├── Main.java           - Entry point for the application
│   │   │           ├── Message.java        - Message data structure
│   │   │           ├── MessageQueue.java   - Thread-safe message routing system
│   │   │           └── Player.java         - Player implementation (runnable)
│   │   └── resources/
│   │       └── logback.xml                 - Logging configuration
│   └── test/
│       └── java/
│           └── com/
│           │   └── bdkamaci/
│           │       ├── GameEngineTest.java   - Tests for GameEngine
│           │       ├── MessageQueueTest.java - Tests for MessageQueue
│           │       ├── MessageTest.java      - Tests for Message
│           │       └── PlayerTest.java       - Tests for Player
│           │
│           └── TestRunner.java               - Execute all test classes from a single entry point  
│
├── pom.xml                                 - Maven configuration
├── README.md                               - This file
└── start_players.sh                        - Shell script to run the application
```

## Class Descriptions

### Player.java

Represents a player in the game that can send and receive messages. Each player runs in a separate thread and processes
messages up to a configured maximum count.

Key methods:

- `start()`: Starts the player thread
- `run()`: Main player loop that receives and sends messages
- `sendReply()`: Creates and sends a reply to a received message
- `stop()`: Interrupts and stops the player thread

### MessageQueue.java

Manages message queues for each player in the system. Provides thread-safe communication between players.

Key methods:

- `registerPlayer()`: Creates a dedicated queue for a player
- `sendMessage()`: Sends a message to a specific player's queue
- `receiveMessage()`: Retrieves the next message for a player (blocking operation)

### Message.java

A record that represents a message sent between players. Contains the sender, receiver, message content, and a counter.

### GameEngine.java

Coordinates the interaction between two players using the MessageQueue. Responsible for initializing players, starting
the game, and sending the initial message.

### Main.java

Entry point for the application. Creates and initializes the GameEngine to start the message exchange.

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

1. Make the script executable:
   ```bash
   chmod +x start_players.sh

2. Run the script:
   ```bash
   ./start_players.sh

### Running the Tests

Run the complete test suite:

```bash
mvn test
```

Run a specific test class:

```bash
mvn test -Dtest=tech.PlayerTest
```

## Test Coverage

The project includes comprehensive unit tests for all classes:

- **PlayerTest**: Tests the Player class behavior, including message processing and threading
- **MessageQueueTest**: Verifies thread-safe message routing and queue operations
- **MessageTest**: Ensures Message objects are properly constructed and compared
- **GameEngineTest**: Tests game initialization and coordination between players

## Dependencies

- **SLF4J & Logback**: For logging functionality
- **Lombok**: Reduces boilerplate code with annotations
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework for unit tests

## Message Format

Messages follow this format:

```
[Msg#n] From: sender → receiver | Reply to: [Msg#m]
```

Where:

- `n` is the current message counter
- `sender` is the player sending the message
- `receiver` is the player receiving the message
- `m` is the message ID being replied to

## Concurrency Aspects

This project demonstrates several concurrency patterns and techniques:

- **Thread Safety**: Using ConcurrentHashMap and BlockingQueue for thread safety
- **Inter-Thread Communication**: Using blocking queues for communication between threads
- **Thread Lifecycle Management**: Starting and stopping threads properly
- **Timeout Handling**: Using timed operations to prevent thread blocking indefinitely
- **Interrupt Handling**: Properly handling thread interruption signals
