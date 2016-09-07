package service;

import lombok.Getter;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Validator {

    private static final String RB_NAME = "locale";

    public enum ValidationCode {
        DUPLICATED_REGISTRATION("local.error.duplicatedregistration"),
        EMAIL_NOT_VALID("local.error.emailnotvalid"),
        NAME_NOT_VALID("local.error.namenotvalid"),
        PASS_NOT_VALID("local.error.passwordnotvalid"),
        PASS_NOT_CHANGED("local.error.passwordnotchanded"),
        PASS_INCORRECT("local.error.passwordincorrect"),
        PASS_DIFFERS("local.error.passwordsdiffers"),
        REGISTRATION("local.error.registration"),
        USER_NOT_FOUND("local.error.usernotfound"),

        LOGIN_SUCCESS("local.error.locinsuccess"),
        REGISTRATION_SUCCESS("local.error.locinsuccess"),
        CONTACT_CHANGED_SUCCESS("local.error.contactchangedsuccess"),
        PASS_CHANGED_SUCCESS("local.error.passwordchangedsuccess");

        @Getter
        private final String propertyName;

        ValidationCode(String propertyName) {
            this.propertyName = propertyName;
        }

        private static String getMessage(ValidationCode enumMessage, Locale locale) {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(RB_NAME, locale);
            final String key = enumMessage.getPropertyName();
            if (resourceBundle.containsKey(key))
                return resourceBundle.getString(key);
            else
                return enumMessage.name();
        }

    }

    public static String getMessage(ValidationCode enumMessage, Locale locale) {
        return ValidationCode.getMessage(enumMessage, locale);
    }

    public static ValidationCode validateLogin(String email, String password) {
        if (!isValidEmail(email))
            return ValidationCode.EMAIL_NOT_VALID;
        else if (!isValidPassword(password))
            return ValidationCode.PASS_NOT_VALID;
        else
            return ValidationCode.LOGIN_SUCCESS;
    }

    public static ValidationCode validateContact(String email, String first_name, String last_name) {
        if (!isValidEmail(email))
            return ValidationCode.EMAIL_NOT_VALID;
        else if (!isValidFirstName(first_name))
            return ValidationCode.NAME_NOT_VALID;
        else if (!isValidLastName(last_name))
            return ValidationCode.NAME_NOT_VALID;
        else
            return ValidationCode.CONTACT_CHANGED_SUCCESS;
    }

    public static ValidationCode validateRegistration(String email, String password, String confirm_password, String first_name, String last_name) {
        if (!isValidEmail(email))
            return ValidationCode.EMAIL_NOT_VALID;
        else if (!isValidFirstName(first_name))
            return ValidationCode.NAME_NOT_VALID;
        else if (!isValidLastName(last_name))
            return ValidationCode.NAME_NOT_VALID;
        else if (!isValidPassword(password))
            return ValidationCode.PASS_NOT_VALID;
        else if (!isValidPassword(password, confirm_password))
            return ValidationCode.PASS_DIFFERS;
        else
            return ValidationCode.REGISTRATION_SUCCESS;
    }

    public static ValidationCode validatePasswordChange(String old_password, String password, String confirm_password) {
        if (old_password.equals(password))
            return (ValidationCode.PASS_NOT_CHANGED);
        else if (!isValidPassword(password))
            return ValidationCode.PASS_NOT_VALID;
        else if (!isValidPassword(password, confirm_password))
            return ValidationCode.PASS_DIFFERS;
        else
            return ValidationCode.PASS_CHANGED_SUCCESS;
    }

    private static boolean isValidEmail(String email) {
        return (email != null && !email.isEmpty() && EmailValidator.getInstance().isValid(email));
    }

    private static boolean isValidPassword(String password) {
        return (password != null && !password.isEmpty());
    }

    private static boolean isValidPassword(String password, String confirm_password) {
        return (password != null && !password.isEmpty() && password.equals(confirm_password));
    }

    private static boolean isValidFirstName(String firstName) {
        return (firstName != null && firstName.matches("[A-ZА-ЯЁ][a-zа-яё]+"));
    }

    private static boolean isValidLastName(String lastName) {
        return (lastName != null && lastName.matches("[A-ZА-ЯЁ][a-zа-яё]+"));
    }

    public static boolean isValidCode(ValidationCode code) {
        return code == ValidationCode.LOGIN_SUCCESS
                || code == ValidationCode.REGISTRATION_SUCCESS
                || code == ValidationCode.CONTACT_CHANGED_SUCCESS
                || code == ValidationCode.PASS_CHANGED_SUCCESS;
    }
}

