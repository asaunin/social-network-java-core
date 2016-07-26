package service;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static service.Validator.ErrorMessage;

public class ValidatorTest {

    private static final Locale locale = Locale.ENGLISH;
    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_NOT_FOUND_RU = "Пользователь не найден";
    private static final String VALID_EMAIL = "test@mail.ru";
    private static final String VALID_PASSWORD = "qwerty";
    private static final String ANOTHER_VALID_PASSWORD = "qwerty123";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String INVALID_EMAIL = "test";
    private static final String INVALID_PASSWORD = "";
    private static final String INVALID_NAME = "John Doe";

    @Before
    public void ValidatorTest() {

        //To test abstract class itself
        class ValidatorImpl extends Validator {
            public boolean abstractMethod() {
                return true;
            }
        }

        ValidatorImpl validatorImpl = new ValidatorImpl();
        assertThat(validatorImpl.abstractMethod(), is(true));

    }

    @Test
    public void getMessageTest() throws Exception {

        assertThat(Validator.getMessage(ErrorMessage.USER_NOT_FOUND, new Locale("ru")),
                is(USER_NOT_FOUND_RU));
        assertThat(Validator.getMessage(ErrorMessage.USER_NOT_FOUND, Locale.ENGLISH),
                is(USER_NOT_FOUND));
        assertThat(Validator.getMessage(ErrorMessage.USER_NOT_FOUND, Locale.GERMAN),
                is(Validator.getMessage(ErrorMessage.USER_NOT_FOUND, Locale.getDefault())));
        assertThat(Validator.getMessage(ErrorMessage.REGISTRATION, Locale.GERMAN),
                is(Validator.getMessage(ErrorMessage.REGISTRATION, Locale.getDefault())));

    }

    @Test
    public void validateLoginTest() throws Exception {

        assertThat(Validator.validateLogin(INVALID_EMAIL, VALID_PASSWORD, locale),
                is(Validator.getMessage(ErrorMessage.EMAIL_NOT_VALID, locale)));
        assertThat(Validator.validateLogin(VALID_EMAIL, INVALID_PASSWORD, locale),
                is(Validator.getMessage(ErrorMessage.PASS_NOT_VALID, locale)));
        assertThat(Validator.validateLogin(VALID_EMAIL, VALID_PASSWORD, locale),
                is(""));

    }

    @Test
    public void validateRegistrationTest() throws Exception {

        assertThat(Validator.validateRegistration(INVALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME, locale),
                is(Validator.getMessage(ErrorMessage.EMAIL_NOT_VALID, locale)));
        assertThat(Validator.validateRegistration(VALID_EMAIL, INVALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME, locale),
                is(Validator.getMessage(ErrorMessage.PASS_NOT_VALID, locale)));
        assertThat(Validator.validateRegistration(VALID_EMAIL, VALID_PASSWORD, ANOTHER_VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME, locale),
                is(Validator.getMessage(ErrorMessage.PASS_DIFFERS, locale)));
        assertThat(Validator.validateRegistration(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, INVALID_NAME, VALID_LAST_NAME, locale),
                is(Validator.getMessage(ErrorMessage.NAME_NOT_VALID, locale)));
        assertThat(Validator.validateRegistration(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, INVALID_NAME, locale),
                is(Validator.getMessage(ErrorMessage.NAME_NOT_VALID, locale)));
        assertThat(Validator.validateRegistration(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD, VALID_FIRST_NAME, VALID_LAST_NAME, locale),
                is(""));

    }

}