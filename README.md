# Hospital Management System

A Java-based Hospital Management System developed using Java 21 and SQLite. The application manages patient records, doctors, appointments and billing information.

## Technologies Used
- Java 21
- SQLite
- JDBC
- SQL
- PowerShell

## Features

### Patient Management
- Register new patients
- View all patients
- Search patients by ID
- Update patient information
- Delete patient records

### Doctor Management
- Register doctors
- View doctor information
- Store medical specializations

### Appointment Management
- Book appointments
- View appointment records
- Link patients with doctors
- Track appointment status

### Billing Management
- Create patient bills
- View billing records
- Track payment status

## Database Design

The application uses four SQLite tables:

| Table | Purpose |
|---|---|
| patients | Patient information |
| doctors | Doctor information |
| appointments | Appointment records |
| bills | Billing information |

## Project Structure

- src/ - Java source code
- database/ - SQL schema
- lib/ - SQLite JDBC driver
- screenshots/ - Application screenshots
- report/ - Project report

## Application Screenshots

### 1. Patient Registration
![Patient Registration](screenshots/01_patient_registration.png)

### 2. Patient Registration - Second Example
![Patient Registration](screenshots/02_patient_registration.png)

### 3. Doctor Registration
![Doctor Registration](screenshots/03_doctor_registration.png)

### 4. Doctor List
![Doctor List](screenshots/04_doctor_list.png)

### 5. Appointment Booking
![Appointment Booking](screenshots/05_appointment_booking.png)

### 6. Appointment List
![Appointment List](screenshots/06_appointment_list.png)

### 7. Bill Creation
![Bill Creation](screenshots/07_bill_creation.png)

### 8. Billing Records
![Billing Records](screenshots/08_billing_records.png)

## How to Run

Compile:

javac -cp "lib/*" -d out src/HospitalManagementSystem.java

Run:

java -cp "out;lib/*" HospitalManagementSystem

## Author

Rony60031

GitHub: https://github.com/Rony60031
