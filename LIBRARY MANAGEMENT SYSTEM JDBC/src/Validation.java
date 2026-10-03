import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Validation {

    static void validateMember(String value, String type) throws Exception {
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

    static void validateBookId(Connection con, int bookId) throws Exception {
        String sql = "SELECT book_id FROM books WHERE book_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookId);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Book not found.");
        }
    }

    static void validateMemberId(Connection con, int memberId) throws Exception {
        String sql = "SELECT member_id FROM members WHERE member_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, memberId);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Member not found.");
        }
    }
    
    static void validateIssueId(Connection con, int issueId) throws Exception {

        String sql = "SELECT issue_id FROM book_issues WHERE issue_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, issueId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {

            throw new Exception("book_issues ID not found.");

        }

    }
}