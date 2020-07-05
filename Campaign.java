import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

enum CampaignStatusType {
    NEW, STARTED, EXPIRED, CANCELLED
}

public class Campaign<K, V> extends UserVoucherMap<Integer, List<Voucher>> implements Subject{
    Integer campaignID;
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime finishDate;
    int possibleVoucherNumber;
    int currentVoucherNumber;
    CampaignStatusType status;
    UserVoucherMap dictionary;
    List<User> observers;
    String strategy;
    int aux = 0; //folosim variabila aux pentru a seta id-ul si codul voucherelor, incrementand-o cu 1
    List<Voucher> vouchersList = new ArrayList<>(); //lista cu voucherele campaniei

    public Campaign() {
        this.observers = new ArrayList<User>();
    }

    public Campaign(Integer id, String description, String name, LocalDateTime d1, LocalDateTime d2, int possibleVoucherNumber, String strategy, CampaignStatusType status) {
        this.campaignID = id;
        this.strategy = strategy;
        this.name = name;
        this.description = description;
        this.startDate = d1;
        this.finishDate = d2;
        this.possibleVoucherNumber = possibleVoucherNumber;
        this.currentVoucherNumber = possibleVoucherNumber;
        this.status = status;
        this.observers = new ArrayList<User>();
    }

    public Campaign(String description, LocalDateTime startDate, LocalDateTime finishDate, int possibleVoucherNumber) {
        this.possibleVoucherNumber = possibleVoucherNumber;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.observers = new ArrayList<User>();
    }

    public List getVouchers() {
        for(int i = 0; i < this.list.size(); i++)
            if((int) this.list.get(i).getKey() == campaignID)
                return this.list.get(i).getValue();

        return null;
    }

    public Voucher getVoucher(String code) {
        for(int i = 0; i < vouchersList.size(); i++) {
            if (vouchersList.get(i).cod == Integer.parseInt(code))
                return vouchersList.get(i);
        }

        return null;
    }

    public Voucher generateVoucher(String email, String voucherType, float value) {
        GiftVoucher voucher1 = new GiftVoucher();
        LoyalityVoucher voucher2 = new LoyalityVoucher();
        this.aux = this.aux + 1;

        if(voucherType.equals("GiftVoucher")) { //in functie de tipul precizat in antet, returnam un GiftVoucher sau u LoyaltyVoucher
            voucher1.amount = value;
            voucher1.email = email;
            voucher1.id = this.aux;
            voucher1.cod = this.aux;
            vouchersList.add(voucher1);
            this.currentVoucherNumber = this.currentVoucherNumber - 1; //De fiecare data cand am generat un voucher, numarul de vouchere disponibile scade

            return voucher1;
        }
        else if(voucherType.equals("LoyaltyVoucher")) {
            voucher2.amount = value;
            voucher2.email = email;
            voucher2.id = this.aux;
            voucher2.cod = this.aux;
            vouchersList.add(voucher2);
            this.currentVoucherNumber = this.currentVoucherNumber - 1;

            return voucher2;
        }

        return null;
    }

    public void redeemVoucher(String code, LocalDateTime date) {
        Voucher voucher = getVoucher(code); //obtinem voucherul cu codul specificat

        if(voucher.status.equals(VoucherStatusType.UNUSED)  && date.isBefore(this.finishDate) == true && date.isAfter(this.startDate) == true) { //veificam daca voucherul e nefolosit si se incadreaza in perioada campaniei
            voucher.status = VoucherStatusType.USED;
            voucher.date = date;
//            notifyAllObservers();
        }
    }

    public List getObservers() {
        return this.observers;
    }

    @Override
    public void addObserver(User user) {
        this.observers.add(user);
    }

    @Override
    public void removeObserver(User user) {
        int index = this.observers.indexOf(user);

        this.observers.remove(index);
    }

    @Override
    public void notifyAllObservers(Notification notification) { //de fiecare data cand o campanie e inchisa/editata observatorii primesc notificare
        for(int i = 0; i < this.observers.size(); i++)
            this.observers.get(i).update(notification);
    }

}
