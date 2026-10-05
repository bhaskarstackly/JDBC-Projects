import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "jdbc:mysql://127.0.0.1:3306/JDBC_DB";
        String user = "root";
        String password = "MySql@123";

//        -- ROOMS
//         'Single', 1500, 'Available'
//         'Double', 2500, 'Available'
//         'Deluxe', 3500, 'Available'

//        -- CUSTOMERS
//         'Bhaskar', 'bhaskar@gmail.com', '9876543210'
//         'Rahul', 'rahul@gmail.com', '9876543211'
//         'Anil', 'anil@gmail.com', '9876543212'

//        -- BOOKINGS
//         1001, 101, '2026-10-05', '2026-10-08'
//         1002, 102, '2026-10-06', '2026-10-10'
//         1003, 103, '2026-10-07', '2026-10-09'

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, password);

            tablesCreation.createRoomsTable(con);
            tablesCreation.createCustomersTable(con);
            tablesCreation.createBookingsTable(con);

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
                            System.out.println("4. Calculate Bill");
                            System.out.println("5. Back");
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
                                	tablesCreation.calculateBill(con, sc);
                                    break;

                                case 5:
                                    System.out.println("Returning to main menu");
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }

                        } while (bookingChoice != 5);

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