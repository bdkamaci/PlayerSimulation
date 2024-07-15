# Message Queue Player Simulation

## Description
This project simulates players that send and receive messages through a `MessageQueue`. Each player has a different PID but runs in the same Java process.

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven 3.6 or higher
- SLF4J Logging Library
- JUnit 5 for testing

## Classes

### Player Class

The `Player` class implements the `Runnable` interface and simulates a player that sends and receives messages through a `MessageQueue`.

#### Constructor
- `messageQueue`: An instance of `MessageQueue` used for message passing.
- `name`: The name of the player.
- `maxMessageCount`: The maximum number of messages a player can send.

#### Methods

- `void setResponder(Player responder)`: Sets the responder player.
- `void run()`: Runs the player's message sending and receiving process.
- `int getMessageCount()`: Returns the number of messages sent by the player.

### MessageQueue Class

The `MessageQueue` class provides a thread-safe way to pass messages between players using a `BlockingQueue`.

#### Constructor

- Creates a new instance of `MessageQueue`.

#### Methods

- `void put(String message) throws InterruptedException`: Puts a message into the queue. Blocks if the queue is full.
- `String take() throws InterruptedException`: Takes a message from the queue. Blocks if the queue is empty.

### GameEngine Class

The GameEngine class provides functionality for initializing and managing the game.

#### Methods

- `void init()`: Initializes the game. This method can perform tasks such as starting the game and loading necessary resources.

### Main Class

The `Main` class contains the main method which sets up the message queue and player threads, and starts the message exchange.

- Creates an instance of `MessageQueue`.
- Instantiates two `Player` objects with the `MessageQueue` and sets them as responders to each other.
- Starts the player threads and initiates the message exchange by putting an initial message into the queue.

## Test Classes

### PlayerTest Class

The `PlayerTest` class contains unit tests for the `Player` class using JUnit 5.

#### Methods

- `void setUp()`: Sets up the test environment by creating instances of `MessageQueue` and `Player`, and starting the player threads.
- `void testPlayersCommunication() throws InterruptedException`: Tests the communication between two players by initiating a message exchange and asserting that both players have exchanged messages.

### MessageQueueTest Class

The `MessageQueueTest` class contains unit tests for the `MessageQueue` class using JUnit 5.

#### Methods

- `void setUp()`: Sets up the test environment by creating an instance of `MessageQueue`.
- `void testPutAndTake() throws InterruptedException`: Tests putting a message into the queue and taking it out.
- `void testBlockingTake() throws InterruptedException`: Tests the blocking behavior of the `take` method by putting a message into the queue after a delay.

### TestRunner Class

The `TestRunner` class is used to execute all tests in the `tech` package and print a summary of the test results.

- Creates a `LauncherDiscoveryRequest` to discover all tests in the `tech` package.
- Uses a `SummaryGeneratingListener` to collect and print the test execution summary.

## Usage

1. Make the script executable:
   ```bash
   chmod +x start_players.sh

2. Run the script:
   ```bash
   ./start_players.sh
