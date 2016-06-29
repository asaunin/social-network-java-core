package service;

import org.apache.commons.validator.routines.EmailValidator;

public abstract class Validator {

    public static boolean isValidEmail(String email) { return EmailValidator.getInstance().isValid(email); }

    public static boolean isValidFirstName(String firstName) { return firstName.matches("[A-Z][a-zA-Z]*"); }

    public static boolean isValidLastName(String lastName) { return lastName.matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" );  }

}
