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

            tablesCreation.createRoomsTable(con);
            tablesCreation.createCustomersTable(con);
            tablesCreation.createBookingsTable(con);


            // ================= ROOM SAMPLE DATA =================

            // tablesCreation.addRoom(con, sc);

            // | Room id | Room Type | Price/Day | Status    |
            // |-------------|-----------|-----------|-----------|
            // | 1         | Single    | 1500      | Available |
            // | 2         | Double    | 2500      | Booked    |
            // | 3         | Deluxe    | 3500      | Occupied  |
            // | 4         | Single    | 1500      | Available |
            // | 5         | Double    | 2500      | Booked    |
            // | 6         | Deluxe    | 3500      | Available |


            // ================= CUSTOMER SAMPLE DATA =================

            // tablesCreation.addCustomer(con, sc);

            // | Customer ID | Customer Name | Email              | Phone      |
            // |-------------|---------------|--------------------|------------|
            // | 1           | Bhaskar       | bhaskar@gmail.com   | 9876543210 |
            // | 2           | Rahul         | rahul@gmail.com     | 9876543211 |
            // | 3           | Anil          | anil@gmail.com      | 9876543212 |
            // | 4           | Kiran         | kiran@gmail.com     | 9876543213 |
            // | 5           | Arjun         | arjun@gmail.com     | 9876543214 |


            // ================= BOOKING SAMPLE DATA =================

            // tablesCreation.addBooking(con, sc);

            // (customer_id, room_number, check_in_date, check_out_date, bill_amount)

            // (1, 1, '2026-10-01', '2026-10-03', 3000),
            // (2, 4, '2026-10-02', '2026-10-05', 4500),
            // (3, 6, '2026-10-05', '2026-10-08', 10500);
            
            // (4, 1, '2026-10-10', '2026-10-13', 4500);-- test -add
            //(5, 3, '2026-10-15', '2026-10-18', 10500);--test- add
            // 2, 2, 2026-10-01, 2026-10-03---------------- test-fail


            // ================= MAIN MENU =================

            int mainChoice;

            do {

                System.out.println("\n**********************************");
                System.out.println("      HOTEL BOOKING SYSTEM");
                System.out.println("================================");
                System.out.println("1. Room Management");
                System.out.println("2. Customer Management");
                System.out.println("3. Booking Management");
                System.out.println("4. Exit");
                System.out.println("***********************************");

                System.out.print("Enter your choice: ");
                mainChoice = sc.nextInt();
                sc.nextLine();

                switch (mainChoice) {

                    case 1:

                        int roomChoice;

                        do {

                            System.out.println("\n---------- ROOM MANAGEMENT ----------");
                            System.out.println("1. Create Room");
                            System.out.println("2. Fetch Rooms");
                            System.out.println("3. Update Room");
                            System.out.println("4. Delete Room");
                            System.out.println("5. Back");
                            System.out.println("-------------------------------------");

                            System.out.print("Enter your choice: ");
                            roomChoice = sc.nextInt();
                            sc.nextLine();


                            switch (roomChoice) {

                                case 1:
                                    tablesCreation.addRoom(con, sc);
                                    break;

                                case 2:
                                    tablesCreation.fetchRooms(con);
                                    break;

                                case 3:
                                    tablesCreation.updateRoom(con, sc);
                                    break;

                                case 4:
                                    tablesCreation.deleteRoom(con, sc);
                                    break;

                                case 5:
                                    System.out.println("Return to main menu");
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }

                        } while (roomChoice != 5);

                        break;

                    case 2:

                        int customerChoice;

                        do {

                            System.out.println("\n-------- CUSTOMER MANAGEMENT --------");
                            System.out.println("1. Create Customer");
                            System.out.println("2. Fetch Customers");
                            System.out.println("3. Update Customer");
                            System.out.println("4. Delete Customer");
                            System.out.println("5. Back");
                            System.out.println("-------------------------------------");

                            System.out.print("Enter your choice: ");
                            customerChoice = sc.nextInt();
                            sc.nextLine();


                            switch (customerChoice) {

                                case 1:
                                    tablesCreation.addCustomer(con, sc);
                                    break;

                                case 2:
                                    tablesCreation.fetchCustomers(con);
                                    break;

                                case 3:
                                    tablesCreation.updateCustomer(con, sc);
                                    break;

                                case 4:
                                    tablesCreation.deleteCustomer(con, sc);
                                    break;

                                case 5:
                                    System.out.println("Returning to main menu...");
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }

                        } while (customerChoice != 5);

                        break;


                    case 3:

                        int bookingChoice;

                        do {

                            System.out.println("\n--------- BOOKING MANAGEMENT ---------");
                            System.out.println("1. Add Booking");
                            System.out.println("2. Fetch Bookings");
                            System.out.println("3. Delete Booking");
                            System.out.println("4. Back");
                            System.out.println("--------------------------------------");

                            System.out.print("Enter your choice: ");
                            bookingChoice = sc.nextInt();
                            sc.nextLine();


                            switch (bookingChoice) {

                                case 1:
                                    tablesCreation.addBooking(con, sc);
                                    break;

                                case 2:
                                    tablesCreation.fetchBookings(con);
                                    break;

                                case 3:
                                    tablesCreation.deleteBooking(con, sc);
                                    break;

                                case 4:
                                    System.out.println("Returning to main menu");
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }

                        } while (bookingChoice != 4);

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