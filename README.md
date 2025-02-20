# 📚 Online Bookstore / Library Management System  

![GitHub repo size](https://img.shields.io/github/repo-size/barnamelisa/Bookstore-Management?color=blue)
![GitHub last commit](https://img.shields.io/github/last-commit/barnamelisa/Bookstore-Management?color=green)
![Contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg)
![License](https://img.shields.io/github/license/barnamelisa/Bookstore-Management?color=yellow)

📌 This repository contains the implementation of a **Bookstore Management System** using Java, where users can manage books, make orders, and view reports. The system includes two types of users: **Employee** and **Administrator**. This application follows **SOLID principles**, **Layered Architecture**, and **Clean Code** principles to ensure scalability, maintainability, and clarity of the code.

---

## 📖 Table of Contents  
- [📌 About](#-about)  
- [🚀 Features](#-features)  
- [💻 How to Use](#-how-to-use)  
- [📂 Folder Structure](#-folder-structure)
- [🔹 How the Application Works](#-how-the -application-works) 
- [ℹ️ More informations about implementation]

---

## 📌 About  

The objective of this project is to familiarize with **design patterns**, **layered architecture**, **SOLID principles**, and **clean code principles**. The **Bookstore Management System** allows for **two types of users**: Employees and Administrators.

- **Employees** can log in and perform actions like:
  - Create, update, delete, or view books in the store.
  - Place orders for customers.
  - Manage their own cart and make transactions.
  
- **Administrators** can log in and:
  - Manage employees (add, delete, update employee information).
  - View all customer data (name, contact, orders, etc.).
  - Generate **monthly sales reports** for employees.

---

## 🚀 Features  

### 📑 **Employee Features**  
✔️ Login with username and password  
✔️ Add, update, view, and delete books in the store  
✔️ Place orders for customers  

### 📊 **Administrator Features**  
✔️ CRUD (Create, Read, Update, Delete) operations on employee data  
✔️ View all customer data (name, contact, orders, etc.)  
✔️ Generate monthly sales reports for employees  

---

## 📂 Folder Structure  
The application follows a **Layered Architecture** pattern with the following folder structure:

- **`controller/`**: Contains classes responsible for managing user inputs and sending requests to the service layer.
- **`database/`**: This folder contains classes related to database configuration and connection. It includes logic for establishing a connection to the MySQL database, initializing schemas, and performing any necessary database migrations.
- **`launcher/`**: Contains the main entry point for the application. It initializes the necessary components, such as database connections, service layers, and other dependencies, to get the system running.
- **`mapper/`**: Is responsible for converting between different data representations in the application, such as converting a domain model (Book) to a data transfer object (DTO) (BookDTO), and vice versa.
- **`model/`**: Contains the data models (e.g., `Book`, `Role`, `User`, `Order`, `Right`).
- **`repository/`**: Contains classes for interacting with the MySQL database (e.g., saving user data, retrieving orders).
- **`service/`**: Contains the business logic, including operations like CRUD and transactions.
- **`view/`**: The view layer is responsible for rendering the user interface (UI). It interacts with users and displays the relevant data. This can include HTML views for web applications or GUI components if it's a desktop-based application.

---

## 💻 How to Use  
 
```sh
git clone https://github.com/barnamelisa/Bookstore-Management.git
cd Bookstore-Management
```

---

## 🔹 How the Application Works
-   **Employees**: After logging in, employees can view the list of books, add and delete books and place orders.
-   **Administrators**: Administrators can view all users' data, update employees' details, add and delete employee and generate reports for the sales made by employees over a month.

---

## ℹ️ More informations about implementation

This application follows solid principles and implements a clean, secure, and maintainable structure. Here's more detailed information about how the system was implemented:

- **Password Hashing**:  
  User passwords are not stored in plaintext in the database. Instead, they are securely hashed using a strong hashing algorithm to ensure user credentials are protected. This prevents any sensitive information from being exposed, even if the database is compromised.

- **MySQL Database**:  
  The system uses **MySQL** as its database to store and manage user data, books, orders, and transactions. The database schema is defined in SQL scripts, and all necessary tables (such as `users`, `employees`, `books`, `orders`, etc.) are created automatically.

- **Architectures Used**:
  The application follows two architectural patterns for structuring the code:
   -   **Layered Architecture**: This separates the application into distinct layers (e.g., controller, service, repository) to handle specific concerns like user interaction, business logic, and data access.
   -   **MVP (Model-View-Presenter)**: The MVP pattern separates the UI (View) from the business logic (Model), with the Presenter acting as an intermediary between the two, ensuring a clean separation of concerns.