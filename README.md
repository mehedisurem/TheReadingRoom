# The Reading Room Bookstore Application

This repository contains the source code for The Reading Room Bookstore application built to satisfy the requirements of COSC2391 Further Programming / COSC1295 Advanced Programming course at RMIT University.

## Project Overview


The Reading Room is a JavaFX based GUI application aimed at the management of a bookstore. It enables users to:

Establish account profiles with specified usernames and passwords

- Successfully log in to the application and access their profiles.

- Browse a collection of books, place selected books in shopping carts, modify the number of copies, or finalize purchases.

- Review previous purchases and print the orders in CSV files.

- Admin users can manage book inventory


The application demonstrates the use of:
- JavaFX for building the GUI frontend
- JDBC for storing persistent data
- Object-oriented design principles like SOLID
- Design patterns such as MVC and Singleton
- Unit testing of key classes

## Getting Started

To run the application locally:

1. Clone the repository
2. Open the project in your Java IDE
3. Set up the required dependencies (JavaFX, JDBC driver)
4. Build and run the `Main` class

## Project Structure

- `src/main/java/`: Contains the Java source code
    - `controllers/`: JavaFX controllers
    - `dao/`: Database configurations
    - `models/`: Data models and database access
    - `views/`: FXML view files
    - `Image/`: Contains Image of all book covers
    - `Icons/`: Contains Icon Images
- `src/test/java/`: Contains the unit tests
- `pom.xml`: Maven configuration file
- `README.md`: This readme file

## Contributors

- Mehedi Hasan Surem
- ID: s4117080


