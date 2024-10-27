# The Reading Room Bookstore Application
<h1 align="center">
The Reading Rooom
</h1>

<div style="text-align: justify">
This repository contains the source code for The Reading Room Bookstore application built to satisfy the requirements of COSC2391 Further Programming / COSC1295 Advanced Programming course at RMIT University. 
</div>


To see the full requirement of this project. Please go to this  [Link](https://drive.google.com/drive/folders/1YdbuTkRk_K3HMO11dspkXcFimgbpnj44?usp=sharing) and download the Requirement.pdf file 


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

## Project Dependencies 

To run the application you should check  if the proper dependencies are selected or not!!
1.  sqlite-jdbc-3.46.1.3
2. zulu23.28.85-ca-fx-jdk23.0.0

If those two dependencies are not into the project dependencies you can download them from [here](https://drive.google.com/drive/folders/1YdbuTkRk_K3HMO11dspkXcFimgbpnj44?usp=sharing)


## User Credential
<div style="text-align: justify">
You can simply create your own user account.
After Build and run the `Main` class the Login scene will pop up. 
From there simply click on "Don't have account, Sign up?" that will navigate you to Signup Scene there you can create new user account
</div>

- For Admin User you must use this Credential:
  - UserID: admin
  - Password: reading_admin

- For Existing User:
  - UserID: Surem
  - Password: 1234 
  
  - UserID: Mir
  - Password: @123#asd

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


## References
- [Lambda expression](https://www.baeldung.com/java-map-computeifabsent)
- [Oracle's official Stream documentation](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Tableview](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableView.html)

## Contributors

- Mehedi Hasan Surem
- ID: s4117080


