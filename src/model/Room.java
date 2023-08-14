package model;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class Room implements IRoom{

    private final String roomNumber;
    private final Double price;
    private final RoomType enumeration;

    private final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);

    public Room(String roomNumber, Double price, RoomType enumeration){
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration =enumeration;
    }

    @Override
    public String toString() {
        return "Room Number: " + this.getRoomNumber() + " | Type: " + this.getRoomType() + " | Price: " + formatter.format(this.getRoomPrice());
    }

    @Override
    public final String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public final Double getRoomPrice() {
        return price;
    }

    @Override
    public final RoomType getRoomType() {
        return enumeration;
    }

    @Override
    public final boolean isFree() {
        return price == 0.0;
    }


    @Override  //the room equality is  based on the room number string ignoring case.
    // This stops double booking or duplication
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IRoom room)) return false;
        return(getRoomNumber().equalsIgnoreCase(room.getRoomNumber()));
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getRoomNumber());
    }

}
