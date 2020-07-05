import java.time.LocalDateTime;

public class LoyalityVoucher extends Voucher {
    float amount;

    public LoyalityVoucher() {
        super();
        this.amount = 0;
    }

    public LoyalityVoucher(VoucherStatusType status, int id, int cod, LocalDateTime date, String email, int campaignID, int discount) {
        super(status, id, cod, date, email, campaignID);
        this.amount = discount;
    }
}
