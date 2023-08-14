package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Reservation {

    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    private final SimpleDateFormat sdf;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.sdf = new SimpleDateFormat("dd/MM/yy");
    }

    @Override
    public final String toString() {
        return customer +
                "\n" + room +
                "\nCheck-In: " + sdf.format(checkInDate) + " || Check Out: " + sdf.format(checkOutDate);
    }

    public final Date getCheckInDate() {
        return checkInDate;
    }

    public final Date getCheckOutDate() {
        return checkOutDate;
    }

    public final IRoom getRoom() {
        return room;
    }

    public final Customer getCustomer() {
        return customer;
    }




    @Override  //the room equality is  based on the room number string ignoring case.
    // This stops double booking or duplication
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation reservation)) return false;
        return (getRoom().equals(reservation.getRoom()) &&
                getCheckOutDate().equals(reservation.getCheckOutDate()) &&
                getCheckInDate().equals(reservation.getCheckInDate())
        );

    }


    @Override
    public final int hashCode() {
        return Objects.hash(getRoom(),getCheckInDate(),getCheckOutDate());
    }


}
