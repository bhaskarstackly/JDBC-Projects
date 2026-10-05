import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Validation {

    static void validateCustomer(String value, String type) throws Exception {

        if (type.equals("email")) {

            if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new Exception("Invalid email format.");
            }

        } else if (type.equals("phone")) {

            if (!value.matches("\\d{10}")) {
                throw new Exception("Phone number must contain 10 digits.");
            }

        }

    }

    static void validateCustomerId(Connection con, int customerId) throws Exception {

        String sql = "SELECT customer_id FROM vehiclecustomers WHERE customer_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, customerId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Customer not found.");
        }

    }

    static void validateVehicleId(Connection con, int vehicleId) throws Exception {

        String sql = "SELECT vehicle_id FROM vehicles WHERE vehicle_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, vehicleId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Vehicle not found.");
        }

    }

    static void validateServiceId(Connection con, int serviceId) throws Exception {

        String sql = "SELECT service_id FROM service_records WHERE service_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, serviceId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Service record not found.");
        }

    }

}