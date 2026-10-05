import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "jdbc:mysql://127.0.0.1:3306/JDBC_DB";
        String user = "root";
        String password = "MySql@123";
        
//        vehicle_id | customer_id | vehicle_number | vehicle_model
//        ----------------------------------------------------------
//        1          | 1           | KA01AB1234     | Honda City
//        2          | 2           | KA05CD5678     | Hyundai Creta
//        3          | 3           | KA03EF9012     | Maruti Swift
//        4          | 1           | KA01GH3456     | Tata Nexon
        
//        | service_id | vehicle_id | service_date | service_type    | service_cost |
//        | ---------: | ---------: | ------------ | --------------- | -----------: |
//        |          1 |          1 | 2026-10-01   | General Service |      2500.00 |
//        |          2 |          2 | 2026-10-02   | Oil Change      |      1200.00 |
//        |          3 |          3 | 2026-10-03   | Brake Service   |      3500.00 |


        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, user, password);

            tablesCreation.createCustomersTable(con);
            tablesCreation.createVehiclesTable(con);
            tablesCreation.createServiceRecordsTable(con);

            int mainChoice;

            do {

                System.out.println("\n**********************************");
                System.out.println(" VEHICLE SERVICE CENTER MANAGEMENT");
                System.out.println("**********************************");
                System.out.println("1. Customer Management");
                System.out.println("2. Vehicle Management");
                System.out.println("3. Service Record Management");
                System.out.println("4. Search Vehicle");
                System.out.println("5. Exit");
                System.out.println("**********************************");

                System.out.print("Enter your choice: ");
                mainChoice = sc.nextInt();
                sc.nextLine();

                switch (mainChoice) {

                case 1:

                    int customerChoice;

                    do {

                        System.out.println("\n--------- CUSTOMER MANAGEMENT ---------");
                        System.out.println("1. Add Customer");
                        System.out.println("2. Fetch Customers");
                        System.out.println("3. Update Customer");
                        System.out.println("4. Delete Customer");
                        System.out.println("5. Back");
                        System.out.println("---------------------------------------");

                        System.out.print("Enter your choice: ");
                        customerChoice = sc.nextInt();
                        sc.nextLine();

                        switch (customerChoice) {

                        case 1:
                        	tablesCreation.addCustomer(sc,con);
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

                case 2:

                    int vehicleChoice;

                    do {

                        System.out.println("\n---------- VEHICLE MANAGEMENT ----------");
                        System.out.println("1. Add Vehicle");
                        System.out.println("2. Fetch Vehicles");
                        System.out.println("3. Update Vehicle");
                        System.out.println("4. Delete Vehicle");
                        System.out.println("5. Back");
                        System.out.println("----------------------------------------");

                        System.out.print("Enter your choice: ");
                        vehicleChoice = sc.nextInt();
                        sc.nextLine();

                        switch (vehicleChoice) {

                        case 1:
                        	tablesCreation.addVehicle(con, sc);
                            break;

                        case 2:
                        	tablesCreation.fetchVehicles(con);
                            break;

                        case 3:
                        	tablesCreation.updateVehicle(con, sc);
                            break;

                        case 4:
                        	tablesCreation.deleteVehicle(con, sc);
                            break;

                        case 5:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                        }

                    } while (vehicleChoice != 5);

                    break;

                case 3:

                    int serviceChoice;

                    do {

                        System.out.println("\n------ SERVICE RECORD MANAGEMENT ------");
                        System.out.println("1. Add Service Record");
                        System.out.println("2. Fetch Service Records");
                        System.out.println("3. Update Service Record");
                        System.out.println("4. Delete Service Record");
                        System.out.println("5. Back");
                        System.out.println("--------------------------------------");

                        System.out.print("Enter your choice: ");
                        serviceChoice = sc.nextInt();
                        sc.nextLine();

                        switch (serviceChoice) {

                        case 1:
                        	tablesCreation.addServiceRecord(con, sc);
                            break;

                        case 2:
                        	tablesCreation.fetchServiceRecords(con);
                            break;

                        case 3:
                        	tablesCreation.updateServiceRecord(con, sc);
                            break;

                        case 4:
                        	tablesCreation.deleteServiceRecord(con, sc);
                            break;

                        case 5:
                            System.out.println("Returning to main menu...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                        }

                    } while (serviceChoice != 5);

                    break;

                case 4:

                	tablesCreation.searchVehicle(con, sc);
                    break;

                case 5:
                    System.out.println("EXIT.");
                    break;

                default:
                    System.out.println("Invalid choice.");
                }

            } while (mainChoice != 5);

            con.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}