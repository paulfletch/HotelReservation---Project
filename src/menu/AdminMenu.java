package menu;


import api.AdminResource;
import api.HotelResource;
import model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class AdminMenu {

    private static final AdminMenu instance =  new AdminMenu();

    private  final Scanner scanner;
    private AdminMenu(){
        scanner = MainMenu.getInstance().getScanner();
    }
    public static AdminMenu getInstance(){
        return instance;
    }

    void showAdminMenu(){

        System.out.print("\033[H\033[2J"); // ANSI Escape Code to clear the console screen
        System.out.flush();


        boolean keepRunning = true;
        do {
            System.out.println("------------------\n1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Back to Main Menu");
            System.out.println("6. Populate the system with test data (Customers, Rooms and Reservations).");
            int selection = 0;

            try {
                if (scanner.hasNextInt()) selection = scanner.nextInt();
                scanner.nextLine();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input: \n\n" + ex.getLocalizedMessage());
            }

            switch (selection) {
                case 1 -> {
                    Collection<Customer> allCustomers = AdminResource.getInstance().getAllCustomers();
                    if ( allCustomers.isEmpty()) {
                        System.out.println("************************************************");
                        System.out.println("*             No Customers found               *");
                        System.out.println("************************************************");
                    }
                    else {
                        System.out.println("************************************************");
                        System.out.println("*             All Customers                    *");
                        System.out.println("************************************************");

                        for (Customer customer : allCustomers)
                            System.out.println(customer);
                    }
                    System.out.println("************************************************");
                }
                case 2 -> {
                    Collection<IRoom> allRooms = AdminResource.getInstance().getAllRooms();
                    if (allRooms.isEmpty()){
                        System.out.println("************************************************");
                        System.out.println("*             No Rooms found                  *");
                        System.out.println("************************************************");
                    }
                    else {
                        System.out.println("************************************************");
                        System.out.println("*                All Rooms                     *");
                        System.out.println("************************************************");
                        for (IRoom room : allRooms)
                            System.out.println(room);
                    }
                    System.out.println("************************************************");
                }
                case 3 -> {
                    System.out.println("************************************************");
                    System.out.println("*             All Reservations                 *");
                    System.out.println("************************************************");
                    AdminResource.getInstance().displayAllReservations();
                    System.out.println("************************************************");
                }
                case 4 -> {
                    System.out.println("************************************************");
                    System.out.println("*                Add a Room                    *");
                    System.out.println("************************************************");
                    this.addARoom();
                }
                case 5 -> {
                    System.out.println("************************************************");
                    System.out.println("*                Main Menu                     *");
                    System.out.println("************************************************");
                    keepRunning = false;
                }
                case 6 -> {
                    System.out.println("************************************************");
                    System.out.println("*    Generating Toy Data for Testing           *");
                    System.out.println("************************************************");

                    List<IRoom> toyRooms = new ArrayList<IRoom>();
                    try {
                    toyRooms.add (new Room("1",50.0 ,RoomType.SINGLE) );
                    toyRooms.add (new Room("2",49.50 ,RoomType.SINGLE) );
                    toyRooms.add (new Room("10",60.50 ,RoomType.DOUBLE) );
                    toyRooms.add (new FreeRoom("3", RoomType.DOUBLE) );
                    toyRooms.add (new FreeRoom("4", RoomType.SINGLE) );
                    toyRooms.add (new FreeRoom("5", RoomType.SINGLE) );
                    toyRooms.add (new Room("6", 60.50, RoomType.DOUBLE) );
                    AdminResource.getInstance().addRoom(toyRooms);
                        System.out.println("Toy data for testing:  room objects added..." );
                    }catch(Exception ee){
                        System.out.println("Problem creating \"Room\" objects for testing. **" + ee.getLocalizedMessage() + " **");
                    }

                    try{
                    HotelResource.getInstance().createACustomer("felix@domain.com", "Felix", "Benson");
                    HotelResource.getInstance().createACustomer("Ariya@domain.com", "Ariya", "Barajas");
                    HotelResource.getInstance().createACustomer("ruth@domain.com", "Ruth", "Bonilla");
                    HotelResource.getInstance().createACustomer("milan@domain.com", "Milan", "Bryant");
                    HotelResource.getInstance().createACustomer("lucy@domain.com", "Lucy", "Patel");
                    HotelResource.getInstance().createACustomer("roy@domain.com", "Roy", "Watson");
                        System.out.println("Toy data for testing:  Customer objects added..." );
                    }catch(Exception ee){
                        System.out.println("Problem creating \"Customer\" objects for testing. **" + ee.getLocalizedMessage() + " **");
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        HotelResource.getInstance().bookARoom("felix@domain.com", toyRooms.get(0), sdf.parse("30/04/23"), sdf.parse("5/05/23"));
                        HotelResource.getInstance().bookARoom("Ariya@domain.com", toyRooms.get(0), sdf.parse("27/04/23"), sdf.parse("29/04/23"));
                        HotelResource.getInstance().bookARoom("ruth@domain.com", toyRooms.get(5), sdf.parse("26/04/23"), sdf.parse("29/04/23"));
                        System.out.println("Toy data for testing:  Reservations created ." );
                    }catch(Exception ee){
                        System.out.println("Problem creating \"Reservation\" objects for testing. **" + ee.getLocalizedMessage() + " **");

                    }


                }

                default -> System.out.println("Please enter a number between 1 and 6");
            }

        }while(keepRunning);

        //return to main menu
        MainMenu.getInstance().showInitialMenu();
    }

    //  Add one room at a time using interactive prompt
    private void  addARoom(){
        List<IRoom> roomsToAdd = new ArrayList<IRoom>();
        boolean free = false ;
        String roomNumber = null;
        double price = -1;
        RoomType enumeration = null;

        do{
            while(roomNumber==null){
                try {
                    System.out.println("Please enter the room number.  [0-9]+");
                    roomNumber = scanner.nextLine();
                    if(!roomNumber.matches("[0-9]+")){
                        System.out.println("A room number must be a number made up of one or more digits [0-9] (this validation restriction is included to meet the requirements of the Udacity review )");
                        roomNumber=null;
                    }
                }catch (Exception e){
                    System.out.println( "Invalid input for room number" +e.getLocalizedMessage());
                }
            }
            while(enumeration==null) {
                try {
                    System.out.println("Please enter the room type.  Either Double or Single");
                    System.out.println( "[Single|Double]");
                    String roomTypeChoice = scanner.nextLine();
                    if(roomTypeChoice.equalsIgnoreCase("SINGLE")) enumeration = RoomType.SINGLE;
                    else if (roomTypeChoice.equalsIgnoreCase("DOUBLE")) enumeration = RoomType.DOUBLE;
                    else System.out.println( "The room type was not recognised.  Please enter either \"Single\" or \"Double\" ");
                    }
                catch (Exception e){
                    System.out.println( "Invalid input for room type" +e.getLocalizedMessage());
                    }
            }
            while(price <0) {
                try {
                    System.out.println("Is this a free room?  Please answer Yes or No");
                    String freeRoomChoice = scanner.nextLine();
                    if(freeRoomChoice.equalsIgnoreCase("YES") || freeRoomChoice.equalsIgnoreCase("Y")  ){
                        free = true;
                        price =0.0;
                    } else if (freeRoomChoice.equalsIgnoreCase("NO") || freeRoomChoice.equalsIgnoreCase("N") )   {
                        do {
                            System.out.println("Please enter a price for the room ");
                            if (scanner.hasNextDouble()){
                                price = scanner.nextDouble();
                            }
                            scanner.nextLine();
                        }while(price<0);
                    }
                    }catch (Exception e){
                        System.out.println( "Invalid input for pricing" +e.getLocalizedMessage());
                    }
            }

            if(free && roomNumber !=null && enumeration!=null)
                roomsToAdd.add(new FreeRoom(roomNumber,enumeration));
            else if (!free && roomNumber !=null  && enumeration!=null && price>0)
                roomsToAdd.add(new Room(roomNumber,price,enumeration));

        }while(roomsToAdd.isEmpty());

        try {
            AdminResource.getInstance().addRoom(roomsToAdd);
            System.out.println("************************************************");
            System.out.println("*       Room added to reservation system       *");
            System.out.println("************************************************");
            System.out.println(roomsToAdd.get(0));
            System.out.println("************************************************");

        }catch (IllegalArgumentException e){
            System.out.println("************************************************");
            System.out.println("*       Room not added to reservation system   *");
            System.out.println("************************************************");
            System.out.println("The specified room was not added to the reservation system : \n" +roomsToAdd.get(0));
            System.out.println("This following problem was found: ");
            System.out.println(e.getLocalizedMessage());
        }
    }
}

