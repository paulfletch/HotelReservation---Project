package api;

import service.CustomerService;
import model.*;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {


    private static final HotelResource reference = new HotelResource();
    public  static HotelResource getInstance() {
        return reference;
    }

    private HotelResource() {}

    public final Customer getCustomer(String email) {
        return CustomerService.getInstance().getCustomer(email);
    }

    public final void createACustomer(String email, String firstName, String lastName) {
        CustomerService.getInstance().addCustomer(email, firstName, lastName);

    }

    public final IRoom getRoom(String roomNumber) {
        return ReservationService.getInstance().getARoom(roomNumber);
    }

    public final Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        return ReservationService.getInstance().reserveARoom(getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public final Collection<Reservation> getCustomerReservations(String customerEmail) {
        return ReservationService.getInstance().getCustomerReservation(
                this.getCustomer(customerEmail)
        );
    }

    public final RoomCollection findRoomsCollection(Date checkIn, Date checkOut) {
        return ReservationService.getInstance().findRoomCollection(checkIn, checkOut);
    }

    public final Collection<IRoom> findRooms(Date checkIn, Date checkOut) {
        return ReservationService.getInstance().findRooms(checkIn, checkOut);
    }


}
