import java.time.LocalDateTime;

public class GiftVoucher extends Voucher {
    float amount;

    public GiftVoucher() {
        super();
        this.amount = 0;
    }

    public GiftVoucher(VoucherStatusType status, int id, int cod, LocalDateTime date, String email, int campaignID, int amount) {
        super(status, id, cod, date, email, campaignID);
        this.amount = amount;
    }
}
