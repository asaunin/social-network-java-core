package jsptags;

import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Setter
public class DateOrTime extends TagSupport {

    private Timestamp date;

    @Override
    public int doStartTag() throws JspException {

        final String formatted_date;

        LocalDateTime messageDate = date.toLocalDateTime();
        LocalDateTime currentDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        if (messageDate.compareTo(currentDate) > 0)
            formatted_date = messageDate.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        else
            formatted_date = messageDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        try {
            pageContext.getOut().write(formatted_date);
        } catch(IOException e){
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;

    }

}
