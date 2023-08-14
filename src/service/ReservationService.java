package service;

import model.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ReservationService {

    public static final int RECOMMENDATION_TIME_SHIFT = 7;    // Declare Constant

    // Rooms are added to a Set (implemented as hashset).  This ensures identical rooms are not stored.
    private final Set<IRoom> roomLookup;

    // Each Date is mapped to a Set<IRoom> for tracking reservations and used when looking up available rooms .
    // This data structure (implemented as hashset) ensures that rooms cannot be booked for the same time for any (date).
    // Treemap implements navigableMap.   subMap(...)  returns reservations associated with a block of dates.
    private final TreeMap<Date, Set<IRoom>> reservedRoomByDate;

    private final Map<Customer, Set<Reservation>> reservationsByCustomer;

    private final static ReservationService reference = new ReservationService();

    public static ReservationService getInstance() {
        return reference;
    }

    // constructor is a private method as this is Singleton pattern.
    private ReservationService() {
        this.roomLookup = new HashSet<IRoom>();
        this.reservationsByCustomer = new HashMap<Customer, Set<Reservation>>();
        this.reservedRoomByDate = new TreeMap<Date, Set<IRoom>>();
    }

    public final void addRoom(IRoom room) {
        if (roomLookup.contains(room))
            throw new IllegalArgumentException("Cannot add room because room number already exists on reservation system");
        else roomLookup.add(room);
    }


    public final IRoom getARoom(String roomID) {
        // returns null if room does not exist.
        return roomLookup.stream()
                .filter(room -> room.getRoomNumber().equals(roomID))
                .findAny()
                .orElse(null);
    }

    /*
    When a customer is created in  CustomerService.
    This  default (package private) method is called so that the reservation system has a data structure
    set up to store reservations for that customer.
    */
    void initialiseCustomer(Customer customer) {
        reservationsByCustomer.putIfAbsent(customer, new HashSet<Reservation>());
    }


    public final Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        // Create reservation and store in map
        Reservation createdReservation = new Reservation(customer, room, checkInDate, checkOutDate);


        // Store reservation for fast look up of IRoom availability.


        // For reservationsByDate, add IRoom for each inDate from checkInDate to checkOutDate
        // If date does not exist in reservationsByDate, then new map entry and collection is created.
        // This also checks availability by throwing exceptions before storing the reservation mapped to customer.

        Calendar ca = Calendar.getInstance();
        ca.setTime(checkInDate);

        Stream
                .iterate  // get the dates in the booking period
                (checkInDate, date -> !date.after(checkOutDate), date ->
                {
                    ca.add(Calendar.DATE, 1);
                    return ca.getTime();
                })

                .peek    // create a room set  for  a date if it does not exist
                (date -> reservedRoomByDate.putIfAbsent(date, new HashSet<IRoom>()))

                .peek  // check that the room is available for these dates (likely a redundant check)
                (date -> {
                    if (reservedRoomByDate.get(date).contains(room))
                        throw new RuntimeException("The reservation is not allowed as the room is not available for the whole date range");
                })

                .forEach // add the room to the room set for each date
                (date -> reservedRoomByDate.get(date).add(room))
        ;

        //  store reservation.
        if (reservationsByCustomer.get(customer).contains(createdReservation))
            throw new RuntimeException("The reservation already exists");
        else reservationsByCustomer.get(customer).add(createdReservation);


        return createdReservation;
    }


    public final Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        // Start with set of potentialRooms which is  all rooms
        // For each Date in the reservation period eliminate rooms with a booking
        Collection<IRoom> foundRooms = new HashSet<IRoom>(roomLookup);

        // Return all rooms if null arguments for dates
        // (this is a bit hacky but follows the class specification)
        if (checkInDate == null && checkOutDate == null) return foundRooms;

        Date inDate = checkInDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        while (!inDate.after(checkOutDate)) {
            if (reservedRoomByDate.get(inDate) != null) {
                foundRooms.removeAll(reservedRoomByDate.get(inDate));
            }
            cal.add(Calendar.DATE, 1);
            inDate = cal.getTime();
        }
        return foundRooms;
    }


    public final RoomCollection findRoomCollection(Date checkInDate, Date checkOutDate) {

        RoomCollection roomCollection = null;

        Collection<IRoom> foundRooms = findRooms(checkInDate, checkOutDate);

        if (!foundRooms.isEmpty()) {
            roomCollection = new RoomCollection("These are Rooms for the requested dates", checkInDate, checkOutDate, foundRooms);
        } else  {
            Date recCheckInDate;
            Date recCheckOutDate;
            Calendar cal = Calendar.getInstance();
            cal.setTime(checkInDate);
            cal.add(Calendar.DATE, RECOMMENDATION_TIME_SHIFT);
            recCheckInDate = cal.getTime();
            cal.setTime(checkOutDate);
            cal.add(Calendar.DATE, RECOMMENDATION_TIME_SHIFT);
            recCheckOutDate = cal.getTime();

            String description = "Rooms are not available for the requested dates.\nThese are recommended rooms found " + RECOMMENDATION_TIME_SHIFT + " days later";
            foundRooms = findRooms(recCheckInDate, recCheckOutDate);

            if (!foundRooms.isEmpty()) {
                roomCollection = new RoomCollection(description, recCheckInDate, recCheckOutDate, foundRooms);
            } else {
                roomCollection = new RoomCollection("No rooms can be found or recommended", checkInDate, checkOutDate, foundRooms);
            }
        }
        return roomCollection;
    }


    public final Collection<Reservation> getCustomerReservation(Customer customer) {
        return reservationsByCustomer.getOrDefault(customer, new HashSet<Reservation>());
    }

    // The specifications had no return type when one might expect to return an object to the api layer
    // Perhaps, this method could be used to create a report on a device such as a printer.
    // For now, the reservations are simply printed to screen.
    public final void printAllReservation() {
        for (Customer cus : reservationsByCustomer.keySet()) {
            for (Reservation res : reservationsByCustomer.get(cus)) {
                System.out.println(res);
                System.out.println("************************************************");
            }
        }

    }


}