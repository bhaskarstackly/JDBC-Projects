import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "jdbc:mysql://127.0.0.1:3306/JDBC_DB";
        String user = "root";
        String password = "MySql@123";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, password);

            tablesCreation.createBooksTable(con);
            tablesCreation.createMembersTable(con);
            tablesCreation.createBookIssuesTable(con);

            int mainChoice;
            
//             BOOKS(title, author, price, status)
//            ('Effective Java', 'Joshua Bloch', 850, 'Available'),
//            ('Clean Code', 'Robert Martin', 700, 'Issued'),
//            ('The Alchemist', 'Paulo Coelho', 450, 'Available'),
//            ('Atomic Habits', 'James Clear', 550, 'Issued'),
//            ('Head First Java', 'Kathy Sierra', 900, 'Available');
//            (' Java', 'Bloch', 850, 'Issued'),
            
//            MEMBERS (member_name, email, phone)
//            ('Bhaskar', 'bhaskar@gmail.com', '9876543210'),
//            ('Rahul', 'rahul@gmail.com', '9876543211'),
//            ('Anil', 'anil@gmail.com', '9876543212'),
//            ('Kiran', 'kiran@gmail.com', '9876543213'),
//            ('Arjun', 'arjun@gmail.com', '9876543214');
            
//          Book_issues (book_id, member_id, issue_date, return_date, fine)
//            (2, 1, '2026-09-30', '2026-10-04', 10),
//            (4, 1, '2026-09-28', '2026-10-04', 30);
//            (6, 2, '2026-10-01', 2026-10-03, 00),

            

            do {

                System.out.println("\n**********************************");
                System.out.println("     LIBRARY MANAGEMENT SYSTEM");
                System.out.println("*************************************");
                System.out.println("1. Book Management");
                System.out.println("2. Member Management");
                System.out.println("3. Book Issue Management");
                System.out.println("4. Exit");
                System.out.println("**********************************");

                System.out.print("Enter your choice: ");
                mainChoice = sc.nextInt();
                sc.nextLine();

                switch (mainChoice) {

                case 1:

                    int bookChoice;

                    do {

                        System.out.println("\n------------ BOOK MANAGEMENT ------------");
                        System.out.println("1. Add Book");
                        System.out.println("2. Fetch Books");
                        System.out.println("3. Update Book");
                        System.out.println("4. Delete Book");
                        System.out.println("5. Back");
                        System.out.println("-----------------------------------------");

                        System.out.print("Enter your choice: ");
                        bookChoice = sc.nextInt();
                        sc.nextLine();

                        switch (bookChoice) {

                        case 1:
                            tablesCreation.addBook(con, sc);
                            break;

                        case 2:
                            tablesCreation.fetchBooks(con);
                            break;

                        case 3:
                            tablesCreation.updateBook(con, sc);
                            break;

                        case 4:
                            tablesCreation.deleteBook(con, sc);
                            break;

                        case 5:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                        }

                    } while (bookChoice != 5);

                    break;

                case 2:

                    int memberChoice;

                    do {

                        System.out.println("\n----------- MEMBER MANAGEMENT -----------");
                        System.out.println("1. Add Member");
                        System.out.println("2. Fetch Members");
                        System.out.println("3. Update Member");
                        System.out.println("4. Delete Member");
                        System.out.println("5. Back");
                        System.out.println("-----------------------------------------");

                        System.out.print("Enter your choice: ");
                        memberChoice = sc.nextInt();
                        sc.nextLine();

                        switch (memberChoice) {

                        case 1:
                            tablesCreation.addMember(con, sc);
                            break;

                        case 2:
                            tablesCreation.fetchMembers(con);
                            break;

                        case 3:
                            tablesCreation.updateMember(con, sc);
                            break;

                        case 4:
                            tablesCreation.deleteMember(con, sc);
                            break;

                        case 5:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                        }

                    } while (memberChoice != 5);

                    break;

                case 3:

                    int issueChoice;

                    do {

                        System.out.println("\n--------- BOOK ISSUE MANAGEMENT ---------");
                        System.out.println("1. Issue & Return Book");
                        System.out.println("2. Fetch Book Issues");
                        System.out.println("3. Delete Book Issue");
                        System.out.println("4. Back");
                        System.out.println("-----------------------------------------");

                        System.out.print("Enter your choice: ");
                        issueChoice = sc.nextInt();
                        sc.nextLine();

                        switch (issueChoice) {

                        case 1:
                            tablesCreation.issueBook(con, sc);
                            break;
                        case 2:
                            tablesCreation.fetchBookIssues(con);
                            break;

                        case 3:
                            tablesCreation.deleteBookIssue(con, sc);
                            break;

                        case 4:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                        }

                    } while (issueChoice != 4);

                    break;

                case 4:
                    System.out.println("EXIT.");
                    break;

                default:
                    System.out.println("Invalid choice.");
                }

            } while (mainChoice != 4);

            con.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}