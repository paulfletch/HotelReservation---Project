package service;

import model.Customer;

import java.util.Collection;
import java.util.*;


public class CustomerService {

    private final static CustomerService reference = new CustomerService();

    private final Map<String, Customer> customerCollection;

    private CustomerService() {
        this.customerCollection = new HashMap<String,Customer>();
    }

    public static CustomerService getInstance() {
        return reference;
    }

    public final void addCustomer(String email, String firstName, String lastname) {
        Customer customer = new Customer(email, firstName, lastname);
        if (customerCollection.containsValue(customer) )throw new RuntimeException("customer already exists");
        customerCollection.put(email, customer);
        ReservationService.getInstance().initialiseCustomer(customer);
    }

    public final Customer getCustomer(String customerEmail) {
        return customerCollection.get(customerEmail);
    }

    public final Collection<Customer> getAllCustomers() {
        return customerCollection.values();
    }

}
