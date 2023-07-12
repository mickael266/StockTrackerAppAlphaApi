# Stock Tracking App

## Description
The Stock Tracking App is a software application designed to track stock market data using the AlphaVantage API. It allows users to view real-time stock prices, historical data, and create personalized watchlists. The app follows various design patterns to ensure modularity, extensibility, and maintainability.

## Design Patterns Used

### 1. Singleton
The Singleton design pattern is used to ensure that only one instance of certain classes exists throughout the application.

- **ChartActivity**: The ChartActivity class, responsible for displaying stock charts, is implemented as a singleton. It ensures that only one instance of ChartActivity is created, allowing consistent handling of chart-related operations.

The Singleton pattern ensures that all interactions with the ChartActivity are performed through the same instance, maintaining a unified and controlled behavior.

### 2. Factory
The Factory design pattern is used to create instances of different stock-related objects based on user preferences.

- **APIResponseFactory**: The APIResponseFactory class creates instances of APIResponse objects based on user-defined criteria. It encapsulates the logic of creating different types of APIResponse objects, such as StockResponse, WatchlistResponse, or HistoricalDataResponse, depending on the user's request.

The Factory pattern centralizes the object creation logic, simplifying the process and promoting code reusability.

### 3. Prototype
The Prototype design pattern is used to create new objects by cloning existing objects, providing a mechanism for creating object instances without specifying their exact class.

- **Stock**: The Stock class serves as a prototype for creating new instances of stock objects. It allows the app to create copies of existing stocks and customize them as needed.

The Prototype pattern helps reduce object creation overhead and allows for the creation of new objects with varying configurations based on existing instances.

![image](https://github.com/mickael266/StockTrackerAppAlphaApi/assets/40838452/837279f3-ee1b-486a-8224-0df8e0f27ddb)
![image](https://github.com/mickael266/StockTrackerAppAlphaApi/assets/40838452/34d3edac-905e-4a00-acab-5c50784b0d10)

![FullClassDiagram](https://github.com/mickael266/StockTrackerAppAlphaApi/assets/40838452/d4e450f2-1e25-4fc9-95a9-ffe5861a9d0a)


