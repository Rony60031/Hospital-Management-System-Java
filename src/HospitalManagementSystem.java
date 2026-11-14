import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    static final String URL = "jdbc:sqlite:hospital.db";
    static Scanner sc = new Scanner(System.in);

    static Connection connect() throws SQLException {
        Connection con = DriverManager.getConnection(URL);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return con;
    }

    static void initializeDatabase() {
        String[] tables = {
            """
            CREATE TABLE IF NOT EXISTS patients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER,
                gender TEXT,
                disease TEXT,
                phone TEXT
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS doctors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                specialization TEXT,
                phone TEXT
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER,
                doctor_id INTEGER,
                appointment_date TEXT,
                reason TEXT,
                status TEXT DEFAULT 'Scheduled',
                FOREIGN KEY(patient_id) REFERENCES patients(id),
                FOREIGN KEY(doctor_id) REFERENCES doctors(id)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS bills (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER,
                amount REAL,
                description TEXT,
                payment_status TEXT DEFAULT 'Unpaid',
                FOREIGN KEY(patient_id) REFERENCES patients(id)
            )
            """
        };

        try (Connection con = connect();
             Statement st = con.createStatement()) {
            for (String sql : tables) st.execute(sql);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    // PATIENT MANAGEMENT

    static void addPatient() {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO patients(name,age,gender,disease,phone) VALUES(?,?,?,?,?)")) {

            System.out.print("Patient Name: ");
            ps.setString(1, sc.nextLine());

            ps.setInt(2, readInt("Age: "));

            System.out.print("Gender: ");
            ps.setString(3, sc.nextLine());

            System.out.print("Disease: ");
            ps.setString(4, sc.nextLine());

            System.out.print("Phone: ");
            ps.setString(5, sc.nextLine());

            ps.executeUpdate();
            System.out.println("Patient registered successfully!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewPatients() {
        String sql = "SELECT * FROM patients ORDER BY id";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- PATIENT LIST ---");
            System.out.printf("%-5s %-20s %-5s %-10s %-18s %-15s%n",
                "ID", "Name", "Age", "Gender", "Disease", "Phone");

            while (rs.next()) {
                System.out.printf("%-5d %-20s %-5d %-10s %-18s %-15s%n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getInt("age"), rs.getString("gender"),
                    rs.getString("disease"), rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void searchPatient() {
        int id = readInt("Enter Patient ID: ");

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM patients WHERE id=?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nPatient ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Disease: " + rs.getString("disease"));
                System.out.println("Phone: " + rs.getString("phone"));
            } else {
                System.out.println("Patient not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updatePatient() {
        int id = readInt("Enter Patient ID: ");

        System.out.print("New Patient Name: ");
        String name = sc.nextLine();

        System.out.print("New Disease: ");
        String disease = sc.nextLine();

        System.out.print("New Phone: ");
        String phone = sc.nextLine();

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "UPDATE patients SET name=?, disease=?, phone=? WHERE id=?")) {

            ps.setString(1, name);
            ps.setString(2, disease);
            ps.setString(3, phone);
            ps.setInt(4, id);

            System.out.println(ps.executeUpdate() > 0
                ? "Patient updated successfully!"
                : "Patient not found.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void deletePatient() {
        int id = readInt("Enter Patient ID to delete: ");

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "DELETE FROM patients WHERE id=?")) {

            ps.setInt(1, id);

            System.out.println(ps.executeUpdate() > 0
                ? "Patient deleted successfully!"
                : "Patient not found.");

        } catch (SQLException e) {
            System.out.println("Cannot delete patient. Related records may exist.");
        }
    }

    // DOCTOR MANAGEMENT

    static void addDoctor() {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO doctors(name,specialization,phone) VALUES(?,?,?)")) {

            System.out.print("Doctor Name: ");
            ps.setString(1, sc.nextLine());

            System.out.print("Specialization: ");
            ps.setString(2, sc.nextLine());

            System.out.print("Phone: ");
            ps.setString(3, sc.nextLine());

            ps.executeUpdate();
            System.out.println("Doctor registered successfully!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewDoctors() {
        String sql = "SELECT * FROM doctors ORDER BY id";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- DOCTOR LIST ---");

            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Specialization: %s | Phone: %s%n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getString("specialization"), rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // APPOINTMENT MANAGEMENT

    static void bookAppointment() {
        int patientId = readInt("Patient ID: ");
        int doctorId = readInt("Doctor ID: ");

        System.out.print("Appointment Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.print("Reason: ");
        String reason = sc.nextLine();

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments(patient_id,doctor_id,appointment_date,reason) VALUES(?,?,?,?)")) {

            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, reason);

            ps.executeUpdate();
            System.out.println("Appointment booked successfully!");

        } catch (SQLException e) {
            System.out.println("Booking failed. Check Patient ID and Doctor ID.");
        }
    }

    static void viewAppointments() {
        String sql = """
            SELECT a.id, p.name AS patient, d.name AS doctor,
                   a.appointment_date, a.reason, a.status
            FROM appointments a
            JOIN patients p ON a.patient_id = p.id
            JOIN doctors d ON a.doctor_id = d.id
            ORDER BY a.id
            """;

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- APPOINTMENT LIST ---");

            while (rs.next()) {
                System.out.printf(
                    "ID: %d | Patient: %s | Doctor: %s | Date: %s | Reason: %s | Status: %s%n",
                    rs.getInt("id"), rs.getString("patient"),
                    rs.getString("doctor"), rs.getString("appointment_date"),
                    rs.getString("reason"), rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // BILLING MANAGEMENT

    static void createBill() {
        int patientId = readInt("Patient ID: ");
        double amount = readDouble("Bill Amount: ");

        System.out.print("Description: ");
        String description = sc.nextLine();

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO bills(patient_id,amount,description) VALUES(?,?,?)")) {

            ps.setInt(1, patientId);
            ps.setDouble(2, amount);
            ps.setString(3, description);

            ps.executeUpdate();
            System.out.println("Bill created successfully!");

        } catch (SQLException e) {
            System.out.println("Billing failed. Check Patient ID.");
        }
    }

    static void viewBills() {
        String sql = """
            SELECT b.id, p.name, b.amount, b.description, b.payment_status
            FROM bills b
            JOIN patients p ON b.patient_id = p.id
            ORDER BY b.id
            """;

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- BILLING RECORDS ---");

            while (rs.next()) {
                System.out.printf(
                    "Bill ID: %d | Patient: %s | Amount: %.2f | Description: %s | Status: %s%n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getDouble("amount"), rs.getString("description"),
                    rs.getString("payment_status"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initializeDatabase();

        while (true) {
            System.out.println("\n================================");
            System.out.println("     HOSPITAL MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1.  Register Patient");
            System.out.println("2.  View All Patients");
            System.out.println("3.  Search Patient");
            System.out.println("4.  Update Patient");
            System.out.println("5.  Delete Patient");
            System.out.println("6.  Register Doctor");
            System.out.println("7.  View Doctors");
            System.out.println("8.  Book Appointment");
            System.out.println("9.  View Appointments");
            System.out.println("10. Create Bill");
            System.out.println("11. View Bills");
            System.out.println("12. Exit");
            System.out.println("================================");

            String choice;
            System.out.print("Enter your choice: ");
            choice = sc.nextLine();

            switch (choice) {
                case "1" -> addPatient();
                case "2" -> viewPatients();
                case "3" -> searchPatient();
                case "4" -> updatePatient();
                case "5" -> deletePatient();
                case "6" -> addDoctor();
                case "7" -> viewDoctors();
                case "8" -> bookAppointment();
                case "9" -> viewAppointments();
                case "10" -> createBill();
                case "11" -> viewBills();
                case "12" -> {
                    System.out.println("Thank you for using Hospital Management System!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
