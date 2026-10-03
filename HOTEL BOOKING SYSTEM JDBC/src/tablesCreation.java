import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class tablesCreation {

    static void createRoomsTable(Connection con) throws SQLException {

        String sql =
            "CREATE TABLE IF NOT EXISTS rooms (" +
            "room_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "room_type VARCHAR(30) NOT NULL CHECK (room_type IN ('Single', 'Double', 'Deluxe')), " +
            "price_per_day DECIMAL(10,2), " +
            "status VARCHAR(20) NOT NULL CHECK (status IN ('Available', 'Booked', 'Occupied'))" +
            ")";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
            System.out.println("Rooms table created with constraints!");
        }
    }

    static void createCustomersTable(Connection con) throws SQLException {

        String sql =
            "CREATE TABLE IF NOT EXISTS customers (" +
            "customer_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "customer_name VARCHAR(50) NOT NULL, " +
            "email VARCHAR(100) UNIQUE NOT NULL, " +
            "phone VARCHAR(10) UNIQUE NOT NULL" +
            ")";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
            System.out.println("Customers table created with constraints!");
        }
    }

    static void createBookingsTable(Connection con) throws SQLException {

        String sql =
            "CREATE TABLE IF NOT EXISTS bookings (" +
            "booking_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "customer_id INT NOT NULL, " +
            "room_id INT NOT NULL, " +
            "check_in_date DATE NOT NULL, " +
            "check_out_date DATE NOT NULL, " +
            "bill_amount DECIMAL(10,2), " +
            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id), " +
            "FOREIGN KEY (room_id) REFERENCES rooms(room_id)" +
            ")";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
            System.out.println("Bookings table created with constraints!");
        }

        System.out.println("**********************************************************************");
    }


    /////////////////////////////////////////////// Room Operations ///////////////////////////////////////////////////////////////

    static void addRoom(Connection con, Scanner sc) {

        try {

            System.out.print("Enter room type (Single/Double/Deluxe): ");

            String roomType = sc.nextLine();

            System.out.print("Enter price per day (Single: 1500, Double: 2500, Deluxe: 3500): ");

            double price = sc.nextDouble();
            sc.nextLine();

            Validation.validateRoom(roomType, price);

            System.out.print("Enter status (Available/Booked/Occupied): ");

            String status = sc.nextLine();

            String sql =
                "INSERT INTO rooms " +
                "(room_type, price_per_day, status) " +
                "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, roomType);
            ps.setDouble(2, price);
            ps.setString(3, status);

            ps.executeUpdate();

            System.out.println("Room added successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void fetchRooms(Connection con) {

        try {

            String sql = "SELECT * FROM rooms";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("-------------------------------------------------------------");

            System.out.printf("%-12s %-12s %-12s %-12s%n",
                    "Room ID", "Room Type", "Price/Day", "Status");

            System.out.println("-------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-12d %-12s %-12.2f %-12s%n",
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_day"),
                        rs.getString("status"));
            }

            System.out.println("-------------------------------------------------------------");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void updateRoom(Connection con, Scanner sc) {

        try {

            System.out.print("Enter room ID to update: ");

            int roomId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter room type (Single/Double/Deluxe): ");

            String roomType = sc.nextLine();

            System.out.print("Enter price per day (Single: 1500, Double: 2500, Deluxe: 3500): ");

            double price = sc.nextDouble();
            sc.nextLine();

            Validation.validateRoom(roomType, price);

            System.out.print("Enter status (Available/Booked/Occupied): ");

            String status = sc.nextLine();

            String sql =
                "UPDATE rooms SET room_type = ?, price_per_day = ?, status = ? " +
                "WHERE room_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, roomType);
            ps.setDouble(2, price);
            ps.setString(3, status);
            ps.setInt(4, roomId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Room updated successfully.");

            } else {

                System.out.println("Room not found.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void deleteRoom(Connection con, Scanner sc) {

        try {

            System.out.print("Enter room ID to delete: ");

            int roomId = sc.nextInt();
            sc.nextLine();

            Validation.validateRoomId(con, roomId);

            String sql = "DELETE FROM rooms WHERE room_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, roomId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Room deleted successfully.");

            } else {

                System.out.println("Room not found.");

            }

        } catch (Exception e) {

            System.out.println("Cannot delete room. It may be booked.");

        }
    }


    ////////////////////////////////////////////////// Customer Operations //////////////////////////////////////////////////////////

    static void addCustomer(Connection con, Scanner sc) {

        try {

            System.out.print("Enter customer name: ");

            String name = sc.nextLine();

            System.out.print("Enter email: ");

            String email = sc.nextLine();

            Validation.validateCustomer(email, "email");

            System.out.print("Enter phone: ");

            String phone = sc.nextLine();

            Validation.validateCustomer(phone, "phone");

            String sql =
                "INSERT INTO customers " +
                "(customer_name, email, phone) " +
                "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            ps.executeUpdate();

            System.out.println("Customer added successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void fetchCustomers(Connection con) {

        try {

            String sql = "SELECT * FROM customers";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--------------------------------------------------------------");

            System.out.printf("%-12s %-15s %-25s %-15s%n",
                    "Customer ID", "Name", "Email", "Phone");

            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-12d %-15s %-25s %-15s%n",
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("email"),
                        rs.getString("phone"));
            }

            System.out.println("--------------------------------------------------------------");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void updateCustomer(Connection con, Scanner sc) {

        try {

            System.out.print("Enter customer ID to update: ");

            int customerId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new name: ");

            String name = sc.nextLine();

            System.out.print("Enter new email: ");

            String email = sc.nextLine();

            Validation.validateCustomer(email, "email");

            System.out.print("Enter new phone: ");

            String phone = sc.nextLine();

            Validation.validateCustomer(phone, "phone");

            String sql =
                "UPDATE customers SET customer_name = ?, email = ?, phone = ? " +
                "WHERE customer_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Customer updated successfully.");

            } else {

                System.out.println("Customer not found.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void deleteCustomer(Connection con, Scanner sc) {

        try {

            System.out.print("Enter customer ID to delete: ");

            int customerId = sc.nextInt();
            sc.nextLine();

            Validation.validateCustomerId(con, customerId);

            String sql = "DELETE FROM customers WHERE customer_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Customer deleted successfully.");

            } else {

                System.out.println("Customer not found.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    //////////////////////////////////////////////////// Booking Operations ////////////////////////////////////////////////////////

    static void addBooking(Connection con, Scanner sc) {

        try {

            System.out.print("Enter customer ID: ");

            int customerId = sc.nextInt();

            Validation.validateCustomerId(con, customerId);

            System.out.print("Enter room ID: ");

            int roomId = sc.nextInt();

            sc.nextLine();

            Validation.validateRoomId(con, roomId);

            System.out.print("Enter check-in date (yyyy-MM-dd): ");

            String checkIn = sc.nextLine();

            System.out.print("Enter check-out date (yyyy-MM-dd): ");

            String checkOut = sc.nextLine();

            String priceSql =
                    "SELECT price_per_day, status FROM rooms WHERE room_id = ?";

            PreparedStatement pricePs = con.prepareStatement(priceSql);

            pricePs.setInt(1, roomId);

            ResultSet rs = pricePs.executeQuery();

            if (!rs.next()) {

                System.out.println("Room not found.");

                return;
            }

            double pricePerDay = rs.getDouble("price_per_day");

            String status = rs.getString("status");

            if (!status.equals("Available")) {

                System.out.println("Room is not available.");

                return;
            }

            double billAmount =
                    Validation.validateBookingDates(checkIn, checkOut, pricePerDay);

            String sql =
                    "INSERT INTO bookings " +
                    "(customer_id, room_id, check_in_date, check_out_date, bill_amount) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ps.setInt(2, roomId);

            ps.setDate(3, java.sql.Date.valueOf(checkIn));

            ps.setDate(4, java.sql.Date.valueOf(checkOut));

            ps.setDouble(5, billAmount);

            ps.executeUpdate();

            System.out.println("Booking added successfully.");

            System.out.println("Bill amount: " + billAmount);

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }


    static void fetchBookings(Connection con) {

        try {

            String sql = "SELECT * FROM bookings";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--------------------------------------------------------------------------");

            System.out.printf("%-12s %-14s %-14s %-15s %-15s %-10s%n",
                    "Booking ID", "Customer ID", "Room ID",
                    "Check-In", "Check-Out", "Bill");

            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-12d %-14d %-14d %-15s %-15s %-10.2f%n",
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("room_id"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"),
                        rs.getDouble("bill_amount"));
            }

            System.out.println("--------------------------------------------------------------------------");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }

    static void deleteBooking(Connection con, Scanner sc) {

        try {

            System.out.print("Enter booking ID to delete: ");

            int bookingId = sc.nextInt();

            sc.nextLine();

            Validation.validateBookingId(con, bookingId);

            String sql = "DELETE FROM bookings WHERE booking_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookingId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Booking deleted successfully.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }
}