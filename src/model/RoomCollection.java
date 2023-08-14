package model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Date;

public class RoomCollection extends HashSet<IRoom> {

    final String metaDescription;
    final Date checkIn;
    final Date checkOut;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    public RoomCollection(String metaDescription, Date checkIn, Date checkOut, Collection<model.IRoom> foundRooms) {
        super();
        this.metaDescription = metaDescription;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.addAll(foundRooms);
    }

    public final String getMetaDescription() {
        return metaDescription;
    }

    public final Date getCheckInDate() {
        return checkIn;
    }

    public final Date getCheckOutDate() {
        return checkOut;
    }


    public final String toString() {
        String output = "";
        // Capture user's room choice
        output = output +
                "\n************************************************\n" +
                this.metaDescription + "\n" +
                "Check in: " + sdf.format(getCheckInDate()) + " | " +
                "Check Out: " + sdf.format(getCheckOutDate()) + "\n" +
                "************************************************\n";

        for (IRoom iRoom : this) output = output + iRoom + "\n";

        return output;
    }


}
