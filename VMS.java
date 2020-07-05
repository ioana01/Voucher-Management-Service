import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;

public class VMS {
    private static VMS obj = null;
    List<Campaign> campaigns ;
    List<User> users;

    private VMS() {
        this.campaigns = new ArrayList<Campaign>(); //alocam memorie pentru listele vms-ului
        this.users = new ArrayList<User>();
    }

    public static VMS getInstance() { //implementam "Singleton Pattern" pentru a-l putea instantia o singura data
        if(obj == null)
            obj = new VMS();

        return obj;
    }

    public List getCampaigns() {
        return this.campaigns;
    }

    public Campaign getCampaign(Integer id) {
        for(int i = 0; i < this.campaigns.size(); i++)
            if(this.campaigns.get(i).campaignID == id)
                return this.campaigns.get(i);

        return null;
    }

    public void addCampaign(Campaign campaign) {
            this.campaigns.add(campaign);
    }

    public void updateCampaign(Integer id, Campaign campaign) {
        Campaign cam;

        for(int i = 0; i < this.campaigns.size(); i++)
            if(this.campaigns.get(i).campaignID == id) {
                cam = this.campaigns.get(i); //cautam campania cu id-ul specificat

                if(cam.status == CampaignStatusType.NEW && campaign.possibleVoucherNumber > (cam.possibleVoucherNumber - cam.currentVoucherNumber)) { //verificam conditiile si numarul total de vouchere
                    cam.description = campaign.description;
                    cam.startDate = campaign.startDate;
                    cam.finishDate = campaign.finishDate;
                    cam.possibleVoucherNumber = campaign.possibleVoucherNumber;
                }
                else if(cam.status == CampaignStatusType.STARTED && campaign.possibleVoucherNumber > (cam.possibleVoucherNumber - cam.currentVoucherNumber)) { //verificam conditiile si numarul total de vouchere
                    cam.finishDate = campaign.finishDate;
                    cam.possibleVoucherNumber = campaign.possibleVoucherNumber;
                }
            }

    }

    public void cancelCampaign(Integer id) {
        for(int i = 0; i < this.campaigns.size(); i++)
            if(this.campaigns.get(i).campaignID == id) //cautam campania cu id-ul specificat
                if(this.campaigns.get(i).status == CampaignStatusType.NEW || this.campaigns.get(i).status == CampaignStatusType.STARTED) { //verificam statusul
                    this.campaigns.get(i).status = CampaignStatusType.CANCELLED;
                }
    }

    public List getUsers() {
        return this.users;
    }

    public void addUser(User user) {
            this.users.add(user);
    }
}
