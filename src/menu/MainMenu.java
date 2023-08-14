package menu;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.RoomCollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {

    private String theUser = null;
    private static final MainMenu instance = new MainMenu();
    private final SimpleDateFormat sdf;
    private Scanner scanner;

    private MainMenu() {
        sdf = new SimpleDateFormat("dd/MM/yy");

    }

    public static MainMenu getInstance() {
        return instance;
    }

    Scanner getScanner() {
        return scanner;
    }

    public void initialise() {
        try {
            scanner = new Scanner(System.in);
            getInstance().showInitialMenu();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            scanner.close();
        }
    }


    public void showInitialMenu() {   // protected maybe

        System.out.print("\033[H\033[2J"); // ANSI Escape Code to clear the console screen
        System.out.flush();

        System.out.println("************************************************");
        System.out.println("*       Hotel Reservations: Main Menu          *");
        System.out.println("************************************************");


        boolean keepRunning = true;
        do {

            System.out.println("------------------\n1. Find and reserve a room");
            System.out.println("2. See my reservations");
            if (theUser == null) System.out.println("3. Create an account");
            else System.out.println("3. Change or create account  (Using account: " + theUser + ")");
            System.out.println("4. Admin");
            System.out.println("5. Exit");

            int selection = 0;
            try {
                if (scanner.hasNextInt()) selection = scanner.nextInt();
                scanner.nextLine();
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input: \n\n" + ex.getLocalizedMessage());
            }

            switch (selection) {
                case 1 -> {

                    System.out.println("************************************************");
                    System.out.println("*                Search for a Room             *");
                    System.out.println("************************************************");

                    Reservation theReservation = this.findAndReserve();
                    if (theReservation != null) {
                        System.out.println("************************************************");
                        System.out.println("*         New Reservation Created              *");
                        System.out.println("************************************************");
                        System.out.println(theReservation);
                        System.out.println("************************************************");
                    } else {
                        System.out.println("************************************************");
                        System.out.println("*          Reservation not created             *");
                        System.out.println("************************************************");
                    }
                }
                case 2 -> {
                    String customerEmail;
                    if (theUser != null) customerEmail = theUser;
                    else customerEmail = this.captureEmail();

                    Collection<Reservation> customerReservations = HotelResource.getInstance().getCustomerReservations(customerEmail);

                    if (customerReservations.isEmpty()) {
                        System.out.println("************************************************");
                        System.out.println("* No reservations associated with your account *");
                        System.out.println("************************************************");
                    } else {
                        System.out.println("************************************************");
                        System.out.println("*               Your reservations              *");
                        System.out.println("************************************************");
                        for (Reservation res : customerReservations) {
                            System.out.println(res);
                            System.out.println("------------------------------------------------");
                        }
                        System.out.println("************************************************");
                    }
                }
                case 3 -> {
                    System.out.println("************************************************");
                    System.out.println("* Create account \\ Switch Account             *");
                    System.out.println("************************************************");
                    this.captureCustomer();
                }
                case 4 -> {
                    System.out.println("************************************************");
                    System.out.println("*               Admin menu                     *");
                    System.out.println("************************************************");
                    AdminMenu.getInstance().showAdminMenu();
                }
                case 5 -> {
                    System.out.println("************************************************");
                    System.out.println("*               Goodbye!                       *");
                    System.out.println("************************************************");
                    keepRunning = false;
                    System.exit(0);
                }
                default -> System.out.println("Please enter a number between 1 and 5");
            }


        } while (keepRunning);

    }


    private Date captureDate() {
        do {
            try {
                System.out.print("Please enter your desired date.  Use format DD/MM/YY \n");
                String input = scanner.nextLine();
                return sdf.parse(input);
            } catch (ParseException e) {
                System.out.println("There was a problem understanding the input as a date: \nParseException " + e.getLocalizedMessage());
                System.out.println("Please try again using the requested format");
            }
        } while (true);
    }

    private String captureEmail() {
        String emailInput = "";
        do {
            System.out.println("Please enter your email address");
            try {
                if (scanner.hasNextLine()) {
                    emailInput = scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Invalid input " + e.getLocalizedMessage());
            }
        } while (emailInput.isBlank());
        return emailInput;
    }

    private String captureCustomer() {
        Customer theCustomer = null;
        String emailInput = "";
        String firstNameInput = null;
        String lastNameInput = null;

        do {
            emailInput = captureEmail();
            // Lookup Customer account
            theCustomer = HotelResource.getInstance().getCustomer(emailInput);
            // Create a new account if none associated with email address

            if (theCustomer != null) {
                System.out.println("************************************************");
                System.out.println("* Account found : " + emailInput);
                System.out.println("************************************************");
                System.out.println("An account with that email.");
                if (emailInput.equalsIgnoreCase(this.theUser)) {
                    System.out.println("You are currently using this account on the hotel reservation system");
                } else
                    System.out.println("You are now using this account on the hotel reservation system");
            } else {
                while (firstNameInput == null) {
                    try {
                        System.out.println("Please enter your first name");
                        firstNameInput = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input for first name" + e.getLocalizedMessage());
                    }
                }

                while (lastNameInput == null)
                    try {
                        System.out.println("Please enter your last name");
                        lastNameInput = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input for last name" + e.getLocalizedMessage());
                        continue;
                    }

                try {
                    HotelResource.getInstance().createACustomer(emailInput, firstNameInput, lastNameInput);
                    theCustomer = HotelResource.getInstance().getCustomer(emailInput);
                    System.out.println("************************************************");
                    System.out.println("* Account created on hotel reservation system  *");
                    System.out.println("************************************************");
                    System.out.println(theCustomer);
                    System.out.println("************************************************");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getLocalizedMessage());  //catching email validations thrown exception
                } catch (Exception e) {
                    System.out.println("Invalid input" + e.getLocalizedMessage());
                }

            }
        } while (theCustomer == null);

        this.theUser = emailInput;

        return emailInput;
    }


    private Reservation findAndReserve() {
        Date checkInDate = null;
        Date checkOutDate = null;
        IRoom targetRoom = null;
        String customerEmail = null;
        // Capture desired dates
        System.out.print("When do you want to check-in? ");
        checkInDate = this.captureDate();
        System.out.print("When do you want to check-out? ");
        checkOutDate = this.captureDate();

        if (checkInDate.compareTo(checkOutDate) >= 0) {
            System.out.println("Check-out date must be after Check-in date");
            return null;   // no reservation can be made as rooms can be or recommended .
        }

        // Discover available or recommended rooms associated with date range
        RoomCollection foundRooms = HotelResource.getInstance().findRoomsCollection(checkInDate, checkOutDate);


        // Capture user's room choice
        System.out.println(foundRooms.toString());

        if (foundRooms.isEmpty()) {
            return null;   // no reservation can be made as rooms can be or recommended .
        }

        do {
            String userInput;
            System.out.println("Enter your choice of room number that you wish to reserve or Q if you no longer wish to proceed with a reservation");
            try {
                userInput = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("There was a problem understanding the room choice: \n " + e.getLocalizedMessage());
                System.out.println("Please try again");
                continue;
            }
            if (userInput.equals("Q")) {
                System.out.println("You chose not to proceed with reserving a room");
                return null;  // No reservation created as used did not choose a room
            } else {
                targetRoom = HotelResource.getInstance().getRoom(userInput);
                if (targetRoom == null) {
                    System.out.println("The room you entered does not exist");
                    continue;
                }
                if (!foundRooms.contains(targetRoom)) {
                    System.out.println("Your choice of : " + targetRoom + " is not available for the booking period ");
                    targetRoom = null;
                    continue;
                }
            }
        } while (targetRoom == null);

        // Retrieve or create user account.
        if (theUser != null) {
            System.out.println("You are currently using customer account: " + theUser);
            String accChoice = null;
            do {
                try {
                    System.out.println("Would you like to make a reservation using this account ?  Please answer Yes or No");
                    accChoice = scanner.nextLine();
                    if (accChoice.equalsIgnoreCase("YES") || accChoice.equalsIgnoreCase("Y")) {
                        customerEmail = theUser;
                    } else if (accChoice.equalsIgnoreCase("NO") || accChoice.equalsIgnoreCase("N")) {
                        System.out.println("Please provide  details of the account you wish to use");
                        customerEmail = this.captureCustomer();
                    } else accChoice = null;
                } catch (Exception e) {
                    System.out.println("There was a problem here :" + e.getLocalizedMessage());
                }
            } while (accChoice == null);
        } else {
            System.out.println("To complete your reservation, we need your details");
            customerEmail = this.captureCustomer();
        }
        return HotelResource.getInstance().bookARoom(customerEmail, targetRoom, foundRooms.getCheckInDate(), foundRooms.getCheckOutDate());
    }
}





