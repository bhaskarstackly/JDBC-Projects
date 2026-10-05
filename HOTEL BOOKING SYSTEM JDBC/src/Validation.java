import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Validation {

    static void validateRoom(String roomType, double price) throws Exception {

        if (!roomType.equals("Single") &&
            !roomType.equals("Double") &&
            !roomType.equals("Deluxe")) {

            throw new Exception("Invalid room type.");
        }

        if (roomType.equals("Single") && price != 1500) {
            throw new Exception("Single room price must be 1500.");
        }

        if (roomType.equals("Double") && price != 2500) {
            throw new Exception("Double room price must be 2500.");
        }

        if (roomType.equals("Deluxe") && price != 3500) {
            throw new Exception("Deluxe room price must be 3500.");
        }
    }


    static void validateCustomer(String value, String type) throws Exception {

        if (type.equals("email")) {

            if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

                throw new Exception("Invalid email format.");
            }

        } else if (type.equals("phone")) {

            if (!value.matches("\\d{10}")) {

                throw new Exception("Phone number must contain exactly 10 digits.");
            }
        }
    }



    static void validateCustomerId(Connection con, int customerId) throws Exception {

        String sql = "SELECT customer_id FROM customers WHERE customer_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, customerId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {

            throw new Exception("Customer not found.");
        }
    }


    static void validateRoomId(Connection con, int roomId) throws Exception {

        String sql = "SELECT room_id FROM rooms WHERE room_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, roomId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {

            throw new Exception("Room not found.");
        }
    }
    static void validateBookingId(Connection con, int bookingId) throws Exception {

        String sql = "SELECT booking_id FROM bookings WHERE booking_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, bookingId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Booking not found.");
        }
    }
}
