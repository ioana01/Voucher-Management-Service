import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

enum NotificationType {
    EDIT, CANCEL
}

public class Notification {
    private NotificationType status;
    private LocalDateTime date;
    private int campaignID;
    private List<Integer> voucherCodes;

    public Notification() {
        this.status = NotificationType.CANCEL;
        this.date = null;
        this.campaignID = -1;
        this.voucherCodes = new ArrayList();
    }

    public Notification(int campaignID, NotificationType status,LocalDateTime date) {
        this.campaignID = campaignID;
        this.status = status;
        this.date = date;
    }
}

