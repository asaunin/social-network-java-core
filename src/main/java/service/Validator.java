package service;

import lombok.Getter;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Validator {

    public static final String RB_NAME = "locale";

    public enum ErrorMessage {
        EMAIL_NOT_VALID("local.error.emailnotvalid"),
        NAME_NOT_VALID("local.error.namenotvalid"),
        USER_NOT_FOUND("local.error.usernotfound"),
        PASS_NOT_VALID("local.error.passwordnotvalid"),
        PASS_INCORRECT("local.error.passwordincorrect"),
        PASS_DIFFERS("local.error.passwordsdiffers"),
        REGISTRATION("local.error.registration"),
        DUPLICATED_REGISTRATION("local.error.duplicatedregistration");

        @Getter
        private String propertyName;

        ErrorMessage(String propertyName) {
            this.propertyName = propertyName;
        }

        private static String getMessage(ErrorMessage enumMessage, Locale locale) {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(RB_NAME, locale);
            final String key = enumMessage.getPropertyName();
            if (resourceBundle.containsKey(key))
                return resourceBundle.getString(key);
            else
                return enumMessage.name();
        }

    }
/*
    public static final String ERROR_EMAIL_NOT_VALID = "local.error.emailnotvalid";
    public static final String ERROR_NAME_NOT_VALID = "local.error.namenotvalid";
    public static final String ERROR_USER_NOT_FOUND = "local.error.usernotfound";
    public static final String ERROR_PASS_NOT_VALID = "local.error.passwordnotvalid";
    public static final String ERROR_PASS_INCORRECT = "local.error.passwordincorrect";
    public static final String ERROR_PASS_DIFFERS = "local.error.passwordsdiffers";
    public static final String ERROR_REGISTRATION = "local.error.registration";
    public static final String ERROR_DUPLICATED_REGISTRATION = "local.error.duplicatedregistration";

    private static String getMessage(String key, Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RB_NAME, locale);
        return resourceBundle.getString(key);
    }
*/

    public static String getMessage(ErrorMessage enumMessage, Locale locale) {
        return ErrorMessage.getMessage(enumMessage, locale);
    }

    public static String validateLogin(String email, String password, Locale locale) {
        if (!isValidEmail(email))
            return ErrorMessage.getMessage(ErrorMessage.EMAIL_NOT_VALID, locale);
        else if (!isValidPassword(password))
            return ErrorMessage.getMessage(ErrorMessage.PASS_NOT_VALID, locale);
        else
            return "";
    }

    public static String validateRegistration(String email, String password, String password_confirmation, String first_name, String last_name, Locale locale) {
        if (!isValidEmail(email))
            return ErrorMessage.getMessage(ErrorMessage.EMAIL_NOT_VALID, locale);
        else if (!isValidFirstName(first_name))
            return ErrorMessage.getMessage(ErrorMessage.NAME_NOT_VALID, locale);
        else if (!isValidLastName(last_name))
            return ErrorMessage.getMessage(ErrorMessage.NAME_NOT_VALID, locale);
        else if (!isValidPassword(password))
            return ErrorMessage.getMessage(ErrorMessage.PASS_NOT_VALID, locale);
        else if (!isValidPassword(password, password_confirmation))
            return ErrorMessage.getMessage(ErrorMessage.PASS_DIFFERS, locale);
        else
            return "";
    }

    public static boolean isValidEmail(String email) {
        return (email != null && !email.isEmpty() && EmailValidator.getInstance().isValid(email));
    }

    public static boolean isValidPassword(String password) {
        return (password != null && !password.isEmpty());
    }

    public static boolean isValidPassword(String password, String password_confirmation) {
        return (password != null && !password.isEmpty() && password.equals(password_confirmation));
    }

    public static boolean isValidFirstName(String firstName) {
        return (firstName != null && firstName.matches("[A-ZА-ЯЁ][a-zа-яё]+"));
    }

    public static boolean isValidLastName(String lastName) {
        return (lastName != null && lastName.matches( "[A-ZА-ЯЁ][a-zа-яё]+"));
    }

}

