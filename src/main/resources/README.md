# Stock market share

## Description
A REST application with a layered architecture that will collect information about stock market shares. Information about stocks (price, name, indices, etc.) is stored in a database and updated from a third-party Twelve data API once at the beginning of the day. At the request of users, they should be given up-to-date information on the company they are interested in.

## Getting Started
These instructions will help you get started with the Spring Boot RESTful application using IntelliJ IDEA. Before you begin, make sure you have the necessary prerequisites installed.

### Prerequisites

Before you can run this Spring Boot application, you need to have the following software and tools installed on your system:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) - Ensure you have a compatible JDK installed (e.g., JDK 17(preferred)).
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) - Make sure you have IntelliJ IDEA installed on your system.

### Clone the Project

1. Open IntelliJ IDEA and select "File" -> "New" -> "Project from Version Control" -> "Git."
2. In the "URL" field, enter the Git repository URL:

https://github.com/Tokhirbek7/stock_market_share


Copy code

3. Click "Clone" to clone the project to your local machine.

### Open the Project in IntelliJ IDEA

1. After cloning, open the project in IntelliJ IDEA.
2. The IDE will recognize it as a Spring Boot project and set up the necessary configurations.

### Configure Application Properties

1. Locate the `src/main/resources/application.properties` file.
2. Configure your application properties, such as database connection settings or other environment-specific configurations.

### Run the Application

1. Open the `Application` class, which contains the `main` method.
2. Right-click on the `main` method and select "Run 'Application.main()'."

The Spring Boot application should start and run on the default port (usually 8080).

### Access the REST API

You can access the REST API endpoints by making HTTP requests to `http://localhost:8080/register`.

Now you're ready to use the Spring Boot RESTful application. You can explore its features and interact with the API as needed.

## DDL Script
The DDL script for this project can be found in the  [Resources]DDL/database_script.sql.


