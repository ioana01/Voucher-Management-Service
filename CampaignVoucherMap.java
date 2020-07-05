import java.util.ArrayList;
import java.util.List;

public class CampaignVoucherMap<K,V> extends ArrayMap<String,List<Voucher>>{

    public boolean addVoucher(Voucher v) {
        List<Voucher> voucherList =  this.get(v.email);

        if(voucherList == null) {
            voucherList = new ArrayList<>(); //daca lista corespunzatoare emailului dat e nula ii alocam memorie pentru a putea adauga in ea
        }

        if(!voucherList.contains(v)) { //daca lista nu contine voucherul il adaugam si returnam true
            voucherList.add(v);
            put(v.email, voucherList);

            return true;
        }

        return false; //daca il contine deja returnam false
    }
}
