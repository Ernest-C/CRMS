# CRMS
#DOCUMENTATION
Final project for IT SIA - A Car Rental Management System that uses Report Generation API


Car Rental Management System

Overview

This project is a Java-based Car Rental Management System featuring a Graphical User Interface (GUI) and integration with a MySQL database. It supports advanced database transactions, making it a reliable solution for managing car rentals.

Features

View All Cars: Retrieve and display a list of all available cars from the database.

Add New Car: Add new car entries to the database.

Register Customers: Register new customers by collecting and storing their details in the database.

Rent a Car: Rent a car to a customer while updating the car's availability and recording the rental transaction using database transactions.

Return a Car: Handle car returns and update the database accordingly.

GUI Interface: Easy-to-use Java Swing-based interface.

Advanced Database Feature

Transactions:

Implemented during the car rental process to ensure atomicity and consistency.

Example: Updating the car's availability and recording the rental transaction are part of a single transaction. If any step fails, the database rolls back to its previous state.

Requirements

Software:

Java JDK (version 8 or later)

MySQL Server

JDBC Driver for MySQL

Database Setup:

Create a database named car_rental.

Execute the provided SQL script to set up the tables (cars, customers, rentals).

Installation

Clone the repository:

git clone https://github.com/your-username/car-rental-management-system.git

Open the project in your favorite IDE (e.g., IntelliJ IDEA, Eclipse).

Configure the database connection in CarRentalDatabase:

private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "password";

Compile and run the project.

How to Use

Main Menu:

Launch the application to access the main menu.

Select actions such as viewing cars, adding cars, registering customers, renting or returning cars.

Car Rental Process:

Enter the required customer ID, car ID, and rental date.

The system will check car availability and update the database.

Return Process:

Enter the rental ID and return date to mark the car as returned.

Project Structure

CarRentalDatabase: Handles database connection and operations.

CarRentalGUI: Manages the graphical user interface and user interaction.


Future Enhancements

Report Generation: Add a system to generate rental and availability reports.

Middleware Integration: Enable communication with other applications using APIs or middleware solutions.

Improved GUI: Upgrade the interface with modern JavaFX components.
