package model;

import java.util.Objects;
import java.util.regex.Pattern;


public class Customer {

    // In this  class, the class  variables are not final but are private.
    // They have accessor and mutator methods to fit
    // the Udacity requirement to demonstrating "encapsulation".
    private String firstName;
    private String lastName;
    private String email;


    // Project Specification: required format name@domain.com .  Simple validation is only required.
    //According to Udacity project requirements, "the email RegEx is simple for the purpose of this exercise"
    // "and may not cover all real-world valid emails."

    // The regex patterns are a constant class variable.  It is not public for security.
    private static final Pattern emailRegex = Pattern.compile("^([^@]+)@([^@]+)\\.com$");

    private static  final Pattern nameRegex = Pattern.compile("^\\w+$", Pattern.UNICODE_CHARACTER_CLASS);

    public Customer( String email, String firstName, String lastName) {

        if (!validateEmail(email.toLowerCase())) {
            throw new IllegalArgumentException(
                    "The provided email address: " + email +  "is in an unrecognised format " +
                            "\nEmails should follow the username@domain.com format" +
                            "\nThe  allowed email address range may not cover all real world valid emails but is" +
                            "\na simple regex format checker in line with the project requirements"
                    );
        }
        if (!validateName(firstName) || !validateName(lastName)  ) {
            throw new IllegalArgumentException(
                    "The provided name : " + firstName + " " + lastName +  "is in an unrecognised format " +
                            "\nNames should adhere to alphabetical characters"

            );
        }

        this.email = email.toLowerCase();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private boolean validateEmail(String email){
        return  emailRegex.matcher(email).matches();
    }
    private boolean validateName(String name) {
        return  nameRegex.matcher(name).matches();
    }

    @Override
    public final String toString() {
        return "Name: " + this.firstName + " " + this.lastName + " | Email : " + this.email+" ";
    }

    // accessor methods
    public final String getEmail() {
        return email;
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    // mutator methods
    public final void  setEmail(String email) {
         validateEmail(email);
         this.email = email;
    }
    public final void  setFirstName(String firstName) {
        validateName(firstName);
        this.firstName = firstName;
    }

    public final void  setLastName(String lastName) {
        validateName(lastName);
        this.lastName = lastName;
    }

    @Override  //the room equality is  based on the room number string ignoring case.
    // This stops double booking or duplication
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return(getEmail().equalsIgnoreCase(customer.getEmail()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

}
