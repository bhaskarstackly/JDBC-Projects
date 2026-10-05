import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class tablesCreation {

    static void createCustomersTable(Connection con) throws Exception {

        String sql = "CREATE TABLE IF NOT EXISTS vehiclecustomers (" +
                "customer_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "customer_name VARCHAR(50) NOT NULL, " +
                "phone VARCHAR(15) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL" +
                ")";

        Statement stmt = con.createStatement();
        stmt.execute(sql);

        System.out.println("VehicleCustomers table created!");
    }

    static void createVehiclesTable(Connection con) throws Exception {

        String sql = "CREATE TABLE IF NOT EXISTS vehicles (" +
                "vehicle_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "customer_id INT NOT NULL, " +
                "vehicle_number VARCHAR(20) NOT NULL, " +
                "vehicle_model VARCHAR(50) NOT NULL" +
                ")";

        Statement stmt = con.createStatement();
        stmt.execute(sql);

        System.out.println("Vehicles table created!");
    }

    static void createServiceRecordsTable(Connection con) throws Exception {

        String sql = "CREATE TABLE IF NOT EXISTS service_records (" +
                "service_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "vehicle_id INT NOT NULL, " +
                "service_date DATE NOT NULL, " +
                "service_type VARCHAR(50) NOT NULL, " +
                "service_cost DECIMAL(10,2) DEFAULT 0" +
                ")";

        Statement stmt = con.createStatement();
        stmt.execute(sql);

        System.out.println("Service Records table created!");
    }
    
    static void addCustomer(Scanner sc, Connection con) {


        try {

            System.out.print("Enter customer name: ");
            String name = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            Validation.validateCustomer(email, "email");

            System.out.print("Enter phone: ");
            String phone = sc.nextLine();

            Validation.validateCustomer(phone, "phone");

            String sql = "INSERT INTO vehiclecustomers (customer_name, phone, email) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);

            ps.executeUpdate();

            System.out.println("Customer added successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void fetchCustomers(Connection con) {

        try {

            String sql = "SELECT * FROM vehiclecustomers";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--------------------------------------------------------------------------");

            System.out.printf("%-12s %-20s %-15s %-25s%n",
                    "Customer ID", "Name", "Phone", "Email");

            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-12d %-20s %-15s %-25s%n",
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("email"));

            }

            System.out.println("--------------------------------------------------------------------------");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void updateCustomer(Connection con, Scanner sc) {

        try {

            System.out.print("Enter customer ID to update: ");

            int customerId = sc.nextInt();

            sc.nextLine();

            Validation.validateCustomerId(con, customerId);

            System.out.print("Enter new customer name: ");

            String name = sc.nextLine();

            System.out.print("Enter new email: ");

            String email = sc.nextLine();

            Validation.validateCustomer(email, "email");

            System.out.print("Enter new phone: ");

            String phone = sc.nextLine();

            Validation.validateCustomer(phone, "phone");

            String sql = "UPDATE vehiclecustomers SET customer_name = ?, email = ?, phone = ? WHERE customer_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Customer updated successfully.");

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

            String checkSql = "SELECT vehicle_id FROM vehicles WHERE customer_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, customerId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                System.out.println("Cannot delete customer. Customer has registered vehicle.");
                return;
            }

            String sql = "DELETE FROM vehiclecustomers WHERE customer_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer deleted successfully.");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    // Vehicle Operations

    static void addVehicle(Connection con, Scanner sc) {

        try {

            System.out.print("Enter customer ID: ");

            int customerId = sc.nextInt();

            sc.nextLine();

            Validation.validateCustomerId(con, customerId);

            System.out.print("Enter vehicle number: ");

            String vehicleNumber = sc.nextLine();

            System.out.print("Enter vehicle model: ");

            String vehicleModel = sc.nextLine();

            String sql = "INSERT INTO vehicles (customer_id, vehicle_number, vehicle_model) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);
            ps.setString(2, vehicleNumber);
            ps.setString(3, vehicleModel);

            ps.executeUpdate();

            System.out.println("Vehicle added successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void fetchVehicles(Connection con) {

        try {

            String sql = "SELECT v.vehicle_id, c.customer_name, v.vehicle_number, v.vehicle_model " +
                    "FROM vehicles v " +
                    "JOIN customers c ON v.customer_id = c.customer_id";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--------------------------------------------------------------------------");

            System.out.printf("%-12s %-20s %-20s %-20s%n",
                    "Vehicle ID", "Customer", "Vehicle Number", "Model");

            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-12d %-20s %-20s %-20s%n",
                        rs.getInt("vehicle_id"),
                        rs.getString("customer_name"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_model"));

            }

            System.out.println("--------------------------------------------------------------------------");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void updateVehicle(Connection con, Scanner sc) {

        try {

            System.out.print("Enter vehicle ID to update: ");

            int vehicleId = sc.nextInt();

            sc.nextLine();

            Validation.validateVehicleId(con, vehicleId);

            System.out.print("Enter new vehicle number: ");

            String vehicleNumber = sc.nextLine();

            System.out.print("Enter new vehicle model: ");

            String vehicleModel = sc.nextLine();

            String sql = "UPDATE vehicles SET vehicle_number = ?, vehicle_model = ? WHERE vehicle_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);
            ps.setString(2, vehicleModel);
            ps.setInt(3, vehicleId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Vehicle updated successfully.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void deleteVehicle(Connection con, Scanner sc) {

        try {

            System.out.print("Enter vehicle ID to delete: ");

            int vehicleId = sc.nextInt();

            sc.nextLine();

            Validation.validateVehicleId(con, vehicleId);

            String checkSql = "SELECT service_id FROM service_records WHERE vehicle_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);

            checkPs.setInt(1, vehicleId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                System.out.println("Cannot delete vehicle. Service records exist.");

                return;

            }

            String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, vehicleId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Vehicle deleted successfully.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    // Service Record Operations

    static void addServiceRecord(Connection con, Scanner sc) {

        try {

            System.out.print("Enter vehicle ID: ");

            int vehicleId = sc.nextInt();

            sc.nextLine();

            Validation.validateVehicleId(con, vehicleId);

            System.out.print("Enter service date (yyyy-MM-dd): ");

            String serviceDate = sc.nextLine();

            java.sql.Date sqlDate = java.sql.Date.valueOf(serviceDate);

            System.out.print("Enter service type: ");

            String serviceType = sc.nextLine();

            System.out.print("Enter service cost: ");

            double serviceCost = sc.nextDouble();

            sc.nextLine();

            String sql = "INSERT INTO service_records (vehicle_id, service_date, service_type, service_cost) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, vehicleId);
            ps.setDate(2, sqlDate);
            ps.setString(3, serviceType);
            ps.setDouble(4, serviceCost);

            ps.executeUpdate();

            System.out.println("Service record added successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void fetchServiceRecords(Connection con) {

        try {

            String sql = "SELECT sr.service_id, c.customer_name, v.vehicle_number, " +
                    "v.vehicle_model, sr.service_date, sr.service_type, sr.service_cost " +
                    "FROM service_records sr " +
                    "JOIN vehicles v ON sr.vehicle_id = v.vehicle_id " +
                    "JOIN vehiclecustomers c ON v.customer_id = c.customer_id";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("------------------------------------------------------------------------------------------");

            System.out.printf("%-10s %-18s %-18s %-15s %-20s %-12s%n",
                    "Service ID", "Customer", "Vehicle", "Model", "Service", "Cost");

            System.out.println("------------------------------------------------------------------------------------------");

            while (rs.next()) {

                System.out.printf("%-10d %-18s %-18s %-15s %-20s %-12.2f%n",
                        rs.getInt("service_id"),
                        rs.getString("customer_name"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_model"),
                        rs.getString("service_type"),
                        rs.getDouble("service_cost"));

            }

            System.out.println("------------------------------------------------------------------------------------------");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void updateServiceRecord(Connection con, Scanner sc) {

        try {

            System.out.print("Enter service ID to update: ");

            int serviceId = sc.nextInt();

            sc.nextLine();

            Validation.validateServiceId(con, serviceId);

            System.out.print("Enter new service date (yyyy-MM-dd): ");

            String serviceDate = sc.nextLine();

            java.sql.Date sqlDate = java.sql.Date.valueOf(serviceDate);

            System.out.print("Enter new service type: ");

            String serviceType = sc.nextLine();

            System.out.print("Enter new service cost: ");

            double serviceCost = sc.nextDouble();

            sc.nextLine();

            String sql = "UPDATE service_records SET service_date = ?, service_type = ?, " +
                    "service_cost = ? WHERE service_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, sqlDate);
            ps.setString(2, serviceType);
            ps.setDouble(3, serviceCost);
            ps.setInt(4, serviceId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Service record updated successfully.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    static void deleteServiceRecord(Connection con, Scanner sc) {

        try {

            System.out.print("Enter service ID to delete: ");

            int serviceId = sc.nextInt();

            sc.nextLine();

            Validation.validateServiceId(con, serviceId);

            String sql = "DELETE FROM service_records WHERE service_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, serviceId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Service record deleted successfully.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    // Search Operation

    static void searchVehicle(Connection con, Scanner sc) {

        try {

            System.out.print("Enter vehicle number to search: ");

            String vehicleNumber = sc.nextLine();

            String sql = "SELECT v.vehicle_id, c.customer_name, c.phone, c.email, " +
                    "v.vehicle_number, v.vehicle_model " +
                    "FROM vehicles v " +
                    "JOIN vehiclecustomers c ON v.customer_id = c.customer_id " +
                    "WHERE v.vehicle_number = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, vehicleNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("----------------------------------------");
                System.out.println("Vehicle ID     : " + rs.getInt("vehicle_id"));
                System.out.println("Customer Name  : " + rs.getString("customer_name"));
                System.out.println("Phone          : " + rs.getString("phone"));
                System.out.println("Email          : " + rs.getString("email"));
                System.out.println("Vehicle Number : " + rs.getString("vehicle_number"));
                System.out.println("Vehicle Model  : " + rs.getString("vehicle_model"));
                System.out.println("----------------------------------------");

            } else {

                System.out.println("Vehicle not found.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }
}