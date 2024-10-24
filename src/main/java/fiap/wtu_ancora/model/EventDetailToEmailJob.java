package fiap.wtu_ancora.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class EventDetailToEmailJob {

    private String title;

    private String startDate;

    private Set<String> usersEmails;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM 'às' HH:mm'h'");
        this.startDate = formatter.format(startDate);
    }

    public Set<String> getUsersEmails() {
        return usersEmails;
    }

    public void setUsersEmails(Set<String> usersEmails) {
        this.usersEmails = usersEmails;
    }
}
