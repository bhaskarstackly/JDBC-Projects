import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class tablesCreation {

    static void createBooksTable(Connection con) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "title VARCHAR(100) NOT NULL, " +
                "author VARCHAR(100) NOT NULL, " +
                "price DECIMAL(10,2), " +
                "status VARCHAR(20) NOT NULL CHECK (status IN ('Available', 'Issued'))" +
                ")";

        Statement stmt = con.createStatement();
        stmt.execute(sql);
        System.out.println("Books table created!");
    }

    static void createMembersTable(Connection con) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS members (" +
                "member_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "member_name VARCHAR(50) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "phone VARCHAR(10) UNIQUE NOT NULL" +
                ")";

        Statement stmt = con.createStatement();
        stmt.execute(sql);
        System.out.println("Members table created!");
    }

    static void createBookIssuesTable(Connection con) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS book_issues (" +
                "issue_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "book_id INT NOT NULL, " +
                "member_id INT NOT NULL, " +
                "issue_date DATE NOT NULL, " +
                "return_date DATE, " +
                "fine DECIMAL(10,2), " +
                "FOREIGN KEY (book_id) REFERENCES books(book_id), " +
                "FOREIGN KEY (member_id) REFERENCES members(member_id)" +
                ")";

        Statement stmt = con.createStatement();
        stmt.execute(sql);
        System.out.println("Book issues table created!");
    }

    static void addBook(Connection con, Scanner sc) {
        try {
            System.out.print("Enter book title: ");
            String title = sc.nextLine();

            System.out.print("Enter author name: ");
            String author = sc.nextLine();

            System.out.print("Enter book price: ");
            double price = sc.nextDouble();
            sc.nextLine();
            
            System.out.print("Enter book status (Available/Issued): ");
            String status = sc.nextLine();

            String sql = "INSERT INTO books (title, author, price, status) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDouble(3, price);
            ps.setString(4, status);
            ps.executeUpdate();

            System.out.println("Book added successfully.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void fetchBooks(Connection con) {
        try {
            String sql = "SELECT * FROM books";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("----------------------------------------------------------------");
            System.out.printf("%-10s %-25s %-20s %-12s %-12s%n",
                    "Book ID", "Title", "Author", "Price", "Status");
            System.out.println("----------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-25s %-20s %-12.2f %-12s%n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getString("status"));
            }

            System.out.println("----------------------------------------------------------------");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void updateBook(Connection con, Scanner sc) {
        try {
            System.out.print("Enter book ID to update: ");
            int bookId = sc.nextInt();
            sc.nextLine();

            Validation.validateBookId(con, bookId);

            System.out.print("Enter new title: ");
            String title = sc.nextLine();

            System.out.print("Enter new author: ");
            String author = sc.nextLine();

            System.out.print("Enter new price: ");
            double price = sc.nextDouble();
            sc.nextLine();
            
            System.out.print("Enter book status (Available/Issued): ");
            String status = sc.nextLine();

            String sql = "UPDATE books SET title = ?, author = ?, price = ? ,status= ? WHERE book_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDouble(3, price);
            ps.setString(4, status);
            ps.setInt(5, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book updated successfully.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteBook(Connection con, Scanner sc) {

        try {

            System.out.print("Enter book ID to delete: ");
            int bookId = sc.nextInt();
            sc.nextLine();

            String checkSql = "SELECT status FROM books WHERE book_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, bookId);

            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Book not found.");
                return;
            }

            String status = rs.getString("status");

            if (status.equals("Issued")) {
                System.out.println("Cannot delete book. Book is currently issued.");
                return;
            }

            String sql = "DELETE FROM books WHERE book_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book deleted successfully.");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    static void addMember(Connection con, Scanner sc) {
        try {
            System.out.print("Enter member name: ");
            String name = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();
            Validation.validateMember(email, "email");

            System.out.print("Enter phone: ");
            String phone = sc.nextLine();
            Validation.validateMember(phone, "phone");

            String sql = "INSERT INTO members (member_name, email, phone) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.executeUpdate();

            System.out.println("Member added successfully.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void fetchMembers(Connection con) {
        try {
            String sql = "SELECT * FROM members";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("----------------------------------------------------------------");
            System.out.printf("%-12s %-15s %-25s %-12s%n",
                    "Member ID", "Name", "Email", "Phone");
            System.out.println("----------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-12d %-15s %-25s %-12s%n",
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getString("email"),
                        rs.getString("phone"));
            }

            System.out.println("----------------------------------------------------------------");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void updateMember(Connection con, Scanner sc) {
        try {
            System.out.print("Enter member ID to update: ");
            int memberId = sc.nextInt();
            sc.nextLine();

            Validation.validateMemberId(con, memberId);

            System.out.print("Enter new member name: ");
            String name = sc.nextLine();

            System.out.print("Enter new email: ");
            String email = sc.nextLine();
            Validation.validateMember(email, "email");

            System.out.print("Enter new phone: ");
            String phone = sc.nextLine();
            Validation.validateMember(phone, "phone");

            String sql = "UPDATE members SET member_name = ?, email = ?, phone = ? WHERE member_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, memberId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Member updated successfully.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteMember(Connection con, Scanner sc) {

        try {

            System.out.print("Enter member ID to delete: ");
            int memberId = sc.nextInt();
            sc.nextLine();

            String checkMemberSql = "SELECT member_id FROM members WHERE member_id = ?";

            PreparedStatement checkMemberPs = con.prepareStatement(checkMemberSql);
            checkMemberPs.setInt(1, memberId);

            ResultSet memberRs = checkMemberPs.executeQuery();

            if (!memberRs.next()) {
                System.out.println("Member not found.");
                return;
            }

            String checkIssueSql =
                    "SELECT bi.issue_id FROM book_issues bi " +
                    "JOIN books b ON bi.book_id = b.book_id " +
                    "WHERE bi.member_id = ? AND b.status = 'Issued'";

            PreparedStatement checkIssuePs = con.prepareStatement(checkIssueSql);
            checkIssuePs.setInt(1, memberId);

            ResultSet issueRs = checkIssuePs.executeQuery();

            if (issueRs.next()) {
                System.out.println("Cannot delete member. Member has an issued book.");
                return;
            }

            String sql = "DELETE FROM members WHERE member_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, memberId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Member deleted successfully.");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }
///////////////////////////////////////////IssueBook/////////////////////////////////////////////////////////////////////////////
    
    static void issueBook(Connection con, Scanner sc) {
    	
        try {

            System.out.print("Enter book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter member ID: ");
            int memberId = sc.nextInt();
            sc.nextLine();

            Validation.validateBookId(con, bookId);
            Validation.validateMemberId(con, memberId);

            String checkSql = "SELECT status FROM books WHERE book_id = ?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, bookId);

            ResultSet rs = checkPs.executeQuery();

            rs.next();

            String status = rs.getString("status");

            if (!status.equals("Issued")) {
                System.out.println("Book is not currently issued.");
                return;
            }
            
            System.out.print("Enter issue date (yyyy-MM-dd): ");
            String issueDate = sc.nextLine();

            System.out.print("Enter return date (yyyy-MM-dd): ");
            String returnDate = sc.nextLine();

            java.sql.Date issueSqlDate = java.sql.Date.valueOf(issueDate);
            java.sql.Date returnSqlDate = java.sql.Date.valueOf(returnDate);

            long days = (returnSqlDate.getTime() - issueSqlDate.getTime())
                    / (1000 * 60 * 60 * 24);

            if (days < 0) {
                System.out.println("Return date cannot be before issue date.");
                return;
            }

            double fine = 0;

            if (days > 3) {
                fine = (days - 3) * 10;
            }

            System.out.println("Days kept: " + days);
            System.out.println("Fine: ₹" + fine);
            
            String sql = "INSERT INTO book_issues (book_id, member_id, issue_date, return_date, fine) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.setDate(3, issueSqlDate);
            ps.setDate(4, returnSqlDate);
            ps.setDouble(5, fine);

            ps.executeUpdate();

        }catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
    
    static void fetchBookIssues(Connection con) {
        try {
            String sql = "SELECT bi.issue_id, b.title, m.member_name, " +
                    "bi.issue_date, bi.return_date, bi.fine " +
                    "FROM book_issues bi " +
                    "JOIN books b ON bi.book_id = b.book_id " +
                    "JOIN members m ON bi.member_id = m.member_id";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-10s %-25s %-15s %-15s %-15s %-10s%n",
                    "Issue ID", "Book", "Member", "Issue Date", "Return Date", "Fine");
            System.out.println("--------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-25s %-15s %-15s %-15s %-10.2f%n",
                        rs.getInt("issue_id"),
                        rs.getString("title"),
                        rs.getString("member_name"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("fine"));
            }

            System.out.println("--------------------------------------------------------------------------------");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteBookIssue(Connection con, Scanner sc) {

        try {

            System.out.print("Enter issue ID to delete: ");
            int issueId = sc.nextInt();
            sc.nextLine();

            Validation.validateIssueId(con, issueId);

            String sql = "DELETE FROM book_issues WHERE issue_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, issueId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println("Book issue deleted successfully.");

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
    }
}