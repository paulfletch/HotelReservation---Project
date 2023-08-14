package api;
import model.IRoom;
import model.Customer;
import service.CustomerService;
import service.ReservationService;
import java.util.List;
import java.util.Collection;


public class AdminResource {
    private final static  AdminResource reference = new AdminResource();

    public static AdminResource getInstance() {
        return reference;
    }

    private AdminResource(){}

    public Customer getCustomer(String email){
       return CustomerService.getInstance().getCustomer(email);
    }

    public final void addRoom(List<IRoom> rooms){
        rooms.forEach(aRoom -> ReservationService.getInstance().addRoom(aRoom));
    }

    public final Collection<IRoom>getAllRooms(){
        return ReservationService.getInstance().findRooms(null,null);
    }

    public final Collection<Customer> getAllCustomers(){
        return CustomerService.getInstance().getAllCustomers();
    }


    public final void displayAllReservations(){
        ReservationService.getInstance().printAllReservation();
    }


}
