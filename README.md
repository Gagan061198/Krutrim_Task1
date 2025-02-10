
# Data Stream App

This is a real-time Android application that streams and displays data ( here data is cryptocurrency order book data) using WebSocket. Built with Kotlin, Jetpack Compose, MVVM architecture, and Room Database.

---

## Features

- **Real-Time Data Streaming**: Fetches live order book data (bids and asks) from the Coincheck WebSocket API.
- **MVVM Architecture**: Follows the Model-View-ViewModel pattern for clean and maintainable code.
- **Jetpack Compose UI**: Modern and responsive UI built with Jetpack Compose.
- **Room Database**: Caches data locally for offline access.

---

## Demo Video - included in the project structure

## Technologies Used

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: Modern UI toolkit for building native Android apps.
- **MVVM Architecture**: Separates UI logic from business logic.
- **Room Database**: Local data caching and persistence.
- **WebSocket**: Real-time data streaming using OkHttp WebSocket.
- **StateFlow**: Handles real-time data updates in a reactive way.
- **SavedStateHandle**: Preserves UI state across configuration changes.

---

## Setup Instructions

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK (API level 24 or higher)
- Kotlin plugin (installed by default in Android Studio)

### Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Gagan061198/Krutrim_Task1.git
   ```

2. **Open the Project**:
   - Open Android Studio.
   - Select `Open an Existing Project` and navigate to the cloned repository.

3. **Build the Project**:
   - Sync the project with Gradle files.
   - Build the project using `Build > Make Project`.

4. **Run the App**:
   - Connect an Android device or emulator.
   - Click `Run > Run 'app'` to install and launch the app.

---


## API Details

The app uses the **Coincheck WebSocket API** to fetch real-time order book data.

- **WebSocket URL**: `wss://ws-api.coincheck.com/`
- **Subscription Message**:
  ```json
  {
    "type": "subscribe",
    "channel": "btc_jpy-orderbook"
  }
  ```
- **Response Format**:
  ```json
  ["btc_jpy", {
    "bids": [["price", "amount"], ...],
    "asks": [["price", "amount"], ...],
    "last_update_at": "timestamp"
  }]
  ```

---

## Key Components

### 1. **WebSocketClient**
- Handles WebSocket connection, message parsing, and error handling.
- Parses raw JSON messages into `StreamEntity` objects.

### 2. **StreamRepository**
- Manages data flow between the WebSocket and Room Database.
- Exposes data to the ViewModel using `StateFlow`.

### 3. **StreamViewModel**
- Acts as the bridge between the UI and data layer.


### 4. **OrderBookScreen**
- Displays real-time data in a `LazyColumn`.
- Includes buttons to start/stop streaming.


- [Coincheck API](https://coincheck.com/documents/exchange/api#websocket) for providing the WebSocket API.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI toolkit.
- [OkHttp](https://square.github.io/okhttp/) for WebSocket support.

---

Feel free to customize this `README.md` further to suit your project's needs. Let me know if you need additional sections or details!
