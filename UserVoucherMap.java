import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UserVoucherMap<K,V> extends ArrayMap<Integer,List<Voucher>>{

    public boolean addVoucher(Voucher v) {
        List<Voucher> voucherList =  this.get(v.campaignID);

        if(voucherList == null) {
            voucherList = new ArrayList<>(); //daca lista corespunzatoare emailului dat e nula ii alocam memorie pentru a putea adauga in ea
        }

        if(!voucherList.contains(v)) { //daca lista nu contine voucherul il adaugam si returnam true
            voucherList.add(v);
            put(v.campaignID, voucherList);

            return true;
        }

        return false; //daca il contine deja returnam false
    }
}


