import java.time.LocalDateTime;

enum  VoucherStatusType{
    USED, UNUSED;
}

public abstract class Voucher  {
    int id;
    int cod;
    VoucherStatusType status;
    LocalDateTime date;
    String email;
    int campaignID;
    float amount;

    public Voucher() {
        this.status = VoucherStatusType.UNUSED;
        this.email = null;
        this.date = null;
        this.campaignID = 0;
        this.id = -1;
        this.cod = -1;
    }

    public Voucher(VoucherStatusType status, int id, int cod, LocalDateTime date, String email, int campaignID) {
        this.status = status;
        this.cod = cod;
        this.id = id;
        this.campaignID = campaignID;
        this.date = date;
        this.email = email;
    }
}
