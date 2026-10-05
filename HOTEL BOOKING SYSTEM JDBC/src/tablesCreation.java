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
    	        "room_id INT NOT NULL, " +
    	        "customer_id INT NOT NULL, " +
    	        "check_in_date DATE NULL, " +
    	        "check_out_date DATE NULL, " +
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

            String checkSql = "SELECT COUNT(*) FROM bookings WHERE room_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);

            checkPs.setInt(1, roomId);

            ResultSet rs = checkPs.executeQuery();

            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println("Cannot delete room. It is booked.");
                return;
            }

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

            System.out.println(e.getMessage());

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

            String checkSql = "SELECT COUNT(*) FROM bookings WHERE customer_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);

            checkPs.setInt(1, customerId);

            ResultSet rs = checkPs.executeQuery();

            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println("Cannot delete customer. Customer has bookings.");
                return;
            }

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

            System.out.println("Cannot delete customer.");

        }
    }

    //////////////////////////////////////////////////// Booking Operations ////////////////////////////////////////////////////////

    static void addBooking(Connection con, Scanner sc) {
        try {
        	System.out.print("Enter room ID: ");
            int roomId = sc.nextInt();
            sc.nextLine();
            Validation.validateRoomId(con, roomId);
            
            System.out.print("Enter customer ID: ");
            int customerId = sc.nextInt();
            sc.nextLine();
            Validation.validateCustomerId(con, customerId);

            String roomSql = "SELECT status FROM rooms WHERE room_id = ?";
            PreparedStatement roomPs = con.prepareStatement(roomSql);
            roomPs.setInt(1, roomId);
            ResultSet rs = roomPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Room not found.");
                return;
            }

            String status = rs.getString("status");

            if (!status.equals("Available")) {
                System.out.println("Room is not available.");
                return;
            }

            System.out.print("Do you want to check-in? (yes/no): ");
            String choice = sc.nextLine();

            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("Enter check-in date: ");
                String checkIn = sc.nextLine();
                java.sql.Date checkInDate = java.sql.Date.valueOf(checkIn);

                String sql = "INSERT INTO bookings (room_id, customer_id, check_in_date) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, roomId);
                ps.setInt(2, customerId);
                ps.setDate(3, checkInDate);
                ps.executeUpdate();

                String updateSql = "UPDATE rooms SET status = 'Occupied' WHERE room_id = ?";
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setInt(1, roomId);
                updatePs.executeUpdate();
                
                System.out.println("Booking Occupied(Checkined) successfully.");

            } else if (choice.equalsIgnoreCase("no")) {
                String sql = "INSERT INTO bookings (room_id, customer_id, check_in_date) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, roomId);
                ps.setInt(2, customerId);
                ps.setDate(3, null);
                ps.executeUpdate();

                String updateSql = "UPDATE rooms SET status = 'Booked' WHERE room_id = ?";
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setInt(1, roomId);
                updatePs.executeUpdate();
                
                System.out.println("Room Booked successfully.");
                
            } else {
                System.out.println("Please enter yes or no.");
                return;
            }


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
    
    static void calculateBill(Connection con, Scanner sc) {
   
    	    try {
    	        System.out.print("Enter room ID: ");
    	        int roomId = sc.nextInt();
    	        sc.nextLine();
    	        Validation.validateRoomId(con, roomId);

    	        String roomSql = "SELECT price_per_day, status FROM rooms WHERE room_id = ?";
    	        PreparedStatement roomPs = con.prepareStatement(roomSql);
    	        roomPs.setInt(1, roomId);
    	        ResultSet roomRs = roomPs.executeQuery();

    	        if (!roomRs.next()) {
    	            System.out.println("Room not found.");
    	            return;
    	        }

    	        String status = roomRs.getString("status");

    	        if (!status.equals("Occupied")) {
    	            System.out.println("Room is not occupied.");
    	            return;
    	        }

    	        double pricePerDay = roomRs.getDouble("price_per_day");

    	        System.out.print("Enter customer ID: ");
    	        int customerId = sc.nextInt();
    	        sc.nextLine();
    	        Validation.validateCustomerId(con, customerId);

    	        String bookingSql = "SELECT check_in_date FROM bookings WHERE room_id = ? AND customer_id = ?";
    	        PreparedStatement bookingPs = con.prepareStatement(bookingSql);
    	        bookingPs.setInt(1, roomId);
    	        bookingPs.setInt(2, customerId);
    	        ResultSet bookingRs = bookingPs.executeQuery();

    	        if (!bookingRs.next()) {
    	            System.out.println("Booking not found.");
    	            return;
    	        }

    	        java.sql.Date checkInDate = bookingRs.getDate("check_in_date");

    	        System.out.print("Enter check-out date (yyyy-MM-dd): ");
    	        String checkOut = sc.nextLine();
    	        java.sql.Date checkOutDate = java.sql.Date.valueOf(checkOut);

    	        long days = (checkOutDate.getTime() - checkInDate.getTime())
    	                / (1000 * 60 * 60 * 24);

    	        double billAmount = days * pricePerDay;

    	        System.out.println("Total Days: " + days);
    	        System.out.println("Total Bill: ₹" + billAmount);

    	        String updateSql = "UPDATE bookings SET check_out_date = ?, bill_amount = ? " +
    	                           "WHERE room_id = ? AND customer_id = ?";
    	        PreparedStatement ps = con.prepareStatement(updateSql);
    	        ps.setDate(1, checkOutDate);
    	        ps.setDouble(2, billAmount);
    	        ps.setInt(3, roomId);
    	        ps.setInt(4, customerId);

    	        ps.executeUpdate();

    	        String roomUpdateSql = "UPDATE rooms SET status = 'Available' WHERE room_id = ?";
    	        PreparedStatement roomUpdatePs = con.prepareStatement(roomUpdateSql);
    	        roomUpdatePs.setInt(1, roomId);
    	        roomUpdatePs.executeUpdate();

    	        System.out.println("Bill added successfully.");
    	        System.out.println("Room is now available.");

    	    } catch (Exception e) {
    	        System.out.println(e.getMessage());
    	    }
    	}
 }