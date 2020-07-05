import javax.naming.ldap.SortKey;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Test extends JFrame  implements ActionListener {

    private JTextField name;
    JTextField id_field;
    String data[][] = new String[10][8];
    JTable table;
    int lineNumber;
    int users_number;
    JTextField newId;
    JTextField newName;
    JTextField newDescription;
    JTextField newDate;
    JTextField newFinish;
    JTextField newBudget;
    JTextField newStrategy;
    JTextField newStatus;
    JTextField message;
    JTextField editedCampaign;
    JTextField seeDetails;
    UserVoucherMap<Integer, List<Voucher>> map; //variabila ce va contine dictionarul de tip UserVoucherMap
    CampaignVoucherMap<String, List<Voucher>> cmap; //variabila ce va contine dictionarul de tip CampaignVoucherMap
    JTextField campaignNr;
    Integer number = -1;
    String data2[][] = new String[40][6];
    String column2[] = {"ID", "COD", "STATUS", "EMAIL", "AMOUNT"};
    JFrame v_frame;
    JFrame c_frame;
    JFrame v_frame_guest;
    JFrame c_frame_guest;
    JTable vouchersTable;
    List<User> usersDisplay; //lista ce va contine toti userii
    JTextField campaignPath;
    Label password_check;
    JPasswordField password;


    public Test(List<Campaign> campaigns, UserVoucherMap<Integer, List<Voucher>> vMap, List<User> u, CampaignVoucherMap<String, List<Voucher>> cMap) {
        map = vMap;
        usersDisplay = u;
        cmap = cMap;

        JFrame frame = new JFrame("upload"); //setam parametrii ferestrei pentru logare
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300, 300));
        frame.getContentPane().setBackground(Color.pink);
        frame.setLayout(new FlowLayout());

        name = new JTextField(15); //adaugam 2 casete pentru nume si parola si un buton de "login"
        frame.add(name);

        Label name_label = new Label("name");
        frame.add(name_label);

        password = new JPasswordField(15);
        frame.add(password);

        Label password_label = new Label("password");
        frame.add(password_label);

        password_check = new Label();
        frame.add(password_check);

        JButton buttonLogin = new JButton ("Login");
        buttonLogin.addActionListener(this);
        frame.add(buttonLogin);

        frame.show();
        frame.pack();


        c_frame = new JFrame("campaigns"); //setam parametrii ferestrei cu informatiile despre campanie in cazul in care userul e admin
        c_frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        c_frame.setMinimumSize(new Dimension(300, 300));
        c_frame.getContentPane().setBackground(Color.cyan);
        c_frame.setLayout(new FlowLayout());

        c_frame_guest = new JFrame("campaigns"); //setam parametrii ferestrei cu informatiile despre campanie in cazul in care userul e guest
        c_frame_guest.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        c_frame_guest.setMinimumSize(new Dimension(300, 300));
        c_frame_guest.getContentPane().setBackground(Color.cyan);
        c_frame_guest.setLayout(new FlowLayout());

        //numai una dintre cele 2 ferestre se va deschide in functie de tipul userului

        id_field = new JTextField(15);
        c_frame.add(id_field);

        Label id_label = new Label("campaign id");
        c_frame.add(id_label);

        JButton cancelCampaign = new JButton("Cancel campaign" );
        cancelCampaign.addActionListener(this);
        c_frame.add(cancelCampaign);

        newId = new JTextField(15);
        c_frame.add(newId);

        Label new_Id = new Label("new id");
        c_frame.add(new_Id);

        newName = new JTextField(15);
        c_frame.add(newName);

        Label new_Name = new Label("new name");
        c_frame.add(new_Name);

        newDescription = new JTextField(15);
        c_frame.add(newDescription);

        Label new_description = new Label("new description");
        c_frame.add(new_description);

        newDate = new JTextField(15);
        c_frame.add(newDate);

        Label new_date = new Label("new start date");
        c_frame.add(new_date);

        newFinish = new JTextField(15);
        c_frame.add(newFinish);

        Label new_finish = new Label("new finish date");
        c_frame.add(new_finish);

        newBudget = new JTextField(15);
        c_frame.add(newBudget);

        Label new_budget = new Label("new budget");
        c_frame.add(new_budget);

        newStrategy = new JTextField(15);
        c_frame.add(newStrategy);

        Label new_strategy = new Label("new strategy");
        c_frame.add(new_strategy);

        newStatus = new JTextField(15);
        c_frame.add(newStatus);

        Label new_status = new Label("new status");
        c_frame.add(new_status);

        JButton addCampaign = new JButton("Add campaign");
        addCampaign.addActionListener(this);
        c_frame.add(addCampaign);

        editedCampaign = new JTextField(15);
        c_frame.add(editedCampaign);

        Label edit = new Label("edited campaign");
        c_frame.add(edit);

        JButton editCampaign = new JButton("Edit campaign");
        editCampaign.addActionListener(this);
        c_frame.add(editCampaign);

        seeDetails = new JTextField(15);
        c_frame.add(seeDetails);

        Label campaignNumber = new Label("Campaign Number");
        c_frame.add(campaignNumber);

        JButton details = new JButton("See details");
        details.addActionListener(this);
        c_frame.add(details);

        message = new JTextField(50);
        c_frame.add(message);

        String column[] = {"ID", "Name", "Description", "Start date",  "Finish date", "Budget", "Strategy", "Status"}; //vector ce contine categoriile din tabel
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); //format folosit pentru a face tranzitia de la String la LocalDateTime

        for(int i = 0; i < campaigns.size(); i++) //adaugam pe fiecare linie a matricii informatii despre o campanie
            for(int j = 0; j < 8; j++) {
                if(j == 0 )
                    data[i][j] = Integer.toString(campaigns.get(i).campaignID);
                if(j == 1)
                    data[i][j] = campaigns.get(i).name;
                if(j == 2)
                    data[i][j] = campaigns.get(i).description;
                if(j == 3)
                    data[i][j] = campaigns.get(i).startDate.format(formatter);
                if(j == 4)
                    data[i][j] = campaigns.get(i).finishDate.format(formatter);
                if(j == 5)
                    data[i][j] = String.valueOf(campaigns.get(i).possibleVoucherNumber);
                if(j == 6)
                    data[i][j] = campaigns.get(i).strategy;
                if(j == 7) {
                    if(campaigns.get(i).status.equals(CampaignStatusType.NEW))
                        data[i][j] = "NEW";
                    else if(campaigns.get(i).status.equals(CampaignStatusType.STARTED))
                        data[i][j] = "STARTED";
                    else if(campaigns.get(i).status.equals(CampaignStatusType.EXPIRED))
                        data[i][j] = "EXPIRED";
                    else if(campaigns.get(i).status.equals(CampaignStatusType.CANCELLED))
                        data[i][j] = "CANCELLED";
                }

            }

        lineNumber = campaigns.size(); //retinem numarul de linii din primul tabel
            users_number = usersDisplay.size(); //retinem numarul de linii pe care il va avea tabelul cu userii

        table = new JTable(data ,column); //cream tabelul despre campanii in cazul in care userul e admin
        table.setBounds(30,40,300,100);
        JScrollPane sp = new JScrollPane(table);
        c_frame.add(sp);

        JTable table_guest = new JTable(data ,column); //cream tabelul despre campanii in cazul in care userul e admin
        table_guest.setBounds(30,40,300,100);
        JScrollPane rs = new JScrollPane(table_guest);
        c_frame_guest.add(rs);


//        TableRowSorter<TableModel> sorting = new TableRowSorter<>(table.getModel());
//        table.setRowSorter(sorting);
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//
//        int columnIndexToSort = 3;
//        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
//
//        columnIndexToSort = 1;
//        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
//
//        sorting.setSortKeys(sortKeys);
//        sorting.sort();


        v_frame = new JFrame("vouchers"); //setam parametrii ferestrei cu informatiile despre vouchere in cazul in care userul e admin
        v_frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        v_frame.setMinimumSize(new Dimension(300, 600));
        v_frame.getContentPane().setBackground(Color.yellow);
        v_frame.setLayout(new FlowLayout());

        v_frame_guest = new JFrame("vouchers"); //setam parametrii ferestrei cu informatiile despre vouchere in cazul in care userul e admin
        v_frame_guest.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        v_frame_guest.setMinimumSize(new Dimension(300, 600));
        v_frame_guest.getContentPane().setBackground(Color.yellow);
        v_frame_guest.setLayout(new FlowLayout());

        //numai una dintre cele 2 ferestre se va deschide in functie de tipul userului

        campaignNr = new JTextField(15);
        v_frame.add(campaignNr);

        Label nr = new Label("Campaign Number");
        v_frame.add(nr);

        JButton seeVouchers = new JButton("See vouchers");
        seeVouchers.addActionListener(this);
        v_frame.add(seeVouchers);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        if (button.getText().equals("Login")){
            for(int i = 0; i < usersDisplay.size(); i++) {
                if(usersDisplay.get(i).name.equals(name.getText()) && usersDisplay.get(i).password.equals(password.getText())) { //cautam in lista userul care s-a logat
                    if (usersDisplay.get(i).status.equals(UserType.ADMIN)) { //in functie de tipul de user hotaram ce fereastre afisam
                        v_frame.show();
                        v_frame.pack();

                        c_frame.show();
                        c_frame.pack();
                    } else { //daca este de tip guest mai cream un tabel cu informatiile despre voucherele lui
                        User auxUser = null;
                        List<Voucher> userVouchers = null;
                        String data3[][] = new String[40][6];

                        for(int j = 0; j < usersDisplay.size(); j++) {
                            if(usersDisplay.get(j).name.equals(name.getText())) //cautam userul dupa numele cu care s-a logat
                                auxUser = usersDisplay.get(j);
                        }


                        for(int p = 0; p < cmap.list.size(); p++) {
                            if (cmap.list.get(p).getKey().equals(auxUser.email)) //cautam lista sa de vouchere
                            {
                                userVouchers = cmap.list.get(p).getValue();
                            }
                        }
                        if(userVouchers == null)
                            System.out.println("e null");

                        for(int p = 0; p < userVouchers.size(); p++) {
                            data3[p][0] = Integer.toString(userVouchers.get(p).id);
                            data3[p][1] = Integer.toString(userVouchers.get(p).cod);
                            data3[p][3] = userVouchers.get(p).email;
                            data3[p][4] = Float.toString(userVouchers.get(p).amount);
                            if(userVouchers.get(p).status.equals(VoucherStatusType.USED))
                                data3[p][2] = "USED";
                            else
                                data3[p][2] = "UNUSED";
                        }

                        JTable usersTable = new JTable(data3 ,column2);
                        usersTable.setBounds(30,40,300,100);
                        JScrollPane mp = new JScrollPane(usersTable);

                        v_frame_guest.add(mp); //afisam ferestrele pentru userii de tip guest (read only)

                        v_frame_guest.show();
                        v_frame_guest.pack();

                        c_frame_guest.show();
                        c_frame_guest.pack();
                    }
                }
                else
                    password_check.setText("Parola incorecta");
            }
        }
        else if(button.getText().equals("Cancel campaign")) {
            table.setValueAt("CANCELLED", Integer.parseInt(id_field.getText()) - 1, 7); //id-ul campaniei corespunde cu randul pe care se afla in tabel
        }
        else if(button.getText().equals("Add campaign")) {
            int ok = 1;

            for(int i  = 0; i < lineNumber; i++) {
                if(newId.getText().equals(data[i][0]) && newName.getText().equals(data[i][1])) { //verificam daca noua campanie se afla deja in tabel
                    ok = 0;
                }
            }
            if(ok == 1) { //daca ok = 1 adaugam in tabel noua campanie si incrementa numarul de linii
                table.setValueAt(newId.getText(),  lineNumber, 0);
                table.setValueAt(newName.getText(),  lineNumber, 1);
                table.setValueAt(newDescription.getText(),  lineNumber, 2);
                table.setValueAt(newDate.getText(),  lineNumber, 3);
                table.setValueAt(newFinish.getText(),  lineNumber, 4);
                table.setValueAt(newBudget.getText(),  lineNumber, 5);
                table.setValueAt(newStrategy.getText(),  lineNumber, 6);
                table.setValueAt(newStatus.getText(),  lineNumber, 7);

                lineNumber = lineNumber + 1;
            }
            else {
                message.setText("Campania exista deja!"); //daca exista deja in tabel afisam  mesajul
            }



        }
        else if(button.getText().equals("Edit campaign")) {
            for(int i = 0; i < table.getRowCount(); i++) {
                if (editedCampaign.getText().equals(data[i][0])) { //cautam campania care trebuie modificata dupa id pe fiecare rand
                    table.setValueAt(newId.getText(),  i, 0);
                    table.setValueAt(newName.getText(),  i, 1);
                    table.setValueAt(newDescription.getText(),  i, 2);
                    table.setValueAt(newDate.getText(),  i, 3);
                    table.setValueAt(newFinish.getText(),  i, 4);
                    table.setValueAt(newBudget.getText(),  i, 5);
                    table.setValueAt(newStrategy.getText(),  i, 6);
                    table.setValueAt(newStatus.getText(),  i, 7);
                }
            }
        }
        else if(button.getText().equals("See details")) {
            for(int i  = 0; i < lineNumber; i++) {
                if (seeDetails.getText().equals(data[i][0])) { //cautam pe fiecare linie campania
                    for(int j = 0; j < map.list.size(); j++) {
                        if(map.list.get(j).getKey() == Integer.parseInt(seeDetails.getText()) - 1) { //cautam in dictionar lista de vouchere a campaniei
                            message.setText(map.list.get(j).getValue().toString());
                        }
                    }
                }
            }
        }
        else if(button.getText().equals("See vouchers")) {
            number = Integer.parseInt(campaignNr.getText());

            List<Voucher> showVouchers = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            if(number != -1)  { //vom afisa pagina cu detaliile despre vouchere doar dupa ce a introdus idul campaniei dorite si am apasat "see vouchers"
                for(int i = 0; i < map.list.size(); i++) {
                    if (map.list.get(i).getKey() == number)
                        showVouchers = map.list.get(i).getValue(); //cautam in dictionar lista cu vouchere a campaniei
                }

                for(int i = 0; i < showVouchers.size(); i++) { //cream tabelul
                    data2[i][0] = Integer.toString(showVouchers.get(i).id);
                    data2[i][1] = Integer.toString(showVouchers.get(i).cod);
                    data2[i][3] = showVouchers.get(i).email;
                    data2[i][4] = Float.toString(showVouchers.get(i).amount);
                    if(showVouchers.get(i).status.equals(VoucherStatusType.USED))
                        data2[i][2] = "USED";
                    else
                        data2[i][2] = "UNUSED";
                }

                vouchersTable = new JTable(data2 ,column2);
                vouchersTable.setBounds(30,40,300,100);
                JScrollPane lp=new JScrollPane(vouchersTable);
                v_frame.add(lp);

                JTextField newID = new JTextField(15);
                v_frame.add(newID);

                Label new_ID = new Label("New id");
                v_frame.add(new_ID);

                JTextField newCod = new JTextField(15);
                v_frame.add(newCod);

                Label new_Cod = new Label("New code");
                v_frame.add(new_Cod);

                JTextField newEmail = new JTextField(15);
                v_frame.add(newEmail);

                Label new_Email = new Label("New email");
                v_frame.add(new_Email);

                JTextField newAmount = new JTextField(15);
                v_frame.add(newAmount);

                Label new_Amount = new Label("New amount");
                v_frame.add(new_Amount);

                JTextField newStatus = new JTextField(15);
                v_frame.add(newStatus);

                Label new_Status = new Label("New status");
                v_frame.add(new_Status);

                JButton newVoucher = new JButton("New Voucher");
                newVoucher.addActionListener(new ActionListener() { //cream o clasa interna
                    public void actionPerformed(ActionEvent e) {
                        JButton btn = (JButton)e.getSource();

                        if(btn.getText().equals("New Voucher")) {
                            vouchersTable.setValueAt(newID.getText(), users_number ,0);
                            vouchersTable.setValueAt(newCod.getText(), users_number, 1);
                            vouchersTable.setValueAt(newEmail.getText(), users_number, 3);
                            vouchersTable.setValueAt(newAmount.getText(), users_number, 4);
                            vouchersTable.setValueAt(newStatus.getText(), users_number, 2);

                            users_number = users_number + 1; //incrementam numarul de linii din tabel
                        }
                    }
                });
                v_frame.add(newVoucher);

                JTextField newMark = new JTextField(15);
                v_frame.add(newMark);

                Label new_Mark = new Label("Voucher code");
                v_frame.add(new_Mark);

                JButton mark = new JButton("Mark voucher"); //marcam voucherul ca fiind utilizat
                mark.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton btn = (JButton)e.getSource();

                        if(btn.getText().equals("Mark voucher")) {

                            for(int i = 0; i < vouchersTable.getRowCount(); i++)
                                    if(vouchersTable.getValueAt(i, 1).toString().equals(newMark.getText())) { //cautam voucheul dupa cod
                                        vouchersTable.setValueAt("USED", i, 2);
                                    }
                        }
                    }
                });
                v_frame.add(mark);

                v_frame.show();
                v_frame.pack();
            }
        }
    }



    public static void main(String args[]) throws Exception {

        JFrame f = new JFrame("file chooser"); //setam parametrii ferestrei in care o sa selectam fisierele
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);

        JButton button2 = new JButton("upload");
        JButton btn_campaign = new JButton("campaign"); //cream o caseta text si un buton de cautare pentru fiecare fisier din input
        JTextField campaignPath = new JTextField(15);

        JButton btn_users = new JButton("users");
        JTextField usersPath = new JTextField(15);

        JButton btn_events = new JButton("events");
        JTextField eventsPath = new JTextField(15);


        btn_campaign.addActionListener(new ActionListener() { //cream o clasa interna pentru fiecare buton de cautare in parte
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                {
                    JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int r = j.showOpenDialog(null);

                    if (r == JFileChooser.APPROVE_OPTION) { //verificam daca userul a ales un fisier
                        campaignPath.setText(j.getSelectedFile().getAbsolutePath()); //obtinem calea catre fisier (campaigns.txt) si o scriem in caseta text
                    }

                }
            }
        });

        btn_events.addActionListener(new ActionListener() { //se repeta procedeul anterior pentru fiecare buton
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                {
                    JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int r = j.showOpenDialog(null);

                    if (r == JFileChooser.APPROVE_OPTION) { //verificam daca userul a ales un fisier
                        eventsPath.setText(j.getSelectedFile().getAbsolutePath()); //obtinem calea catre fisier (events.txt) si o scriem in caseta text
                    }
                }
            }
        });

        btn_users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                {
                    JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int r = j.showOpenDialog(null);

                    if (r == JFileChooser.APPROVE_OPTION) { //verificam daca userul a ales un fisier
                        usersPath.setText(j.getSelectedFile().getAbsolutePath()); //obtinem calea catre fisier (users.txt) si o scriem in caseta text
                    }
                }
            }
        });


        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JButton button = (JButton)evt.getSource();

                if (button.getText().equals("upload")) { //numai dupa ce a fost apasat butonul "upload" putem continua

                    File campaign = new File(campaignPath.getText()); //folosim calea obtinuta in fereastra text pentru a deschie fisierul
                    Scanner sc = null;
                    try {
                        sc = new Scanner(campaign);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //citim informatiile din fisierul campaign.txt
                    int campaignsNumber = Integer.parseInt(sc.nextLine()); //citim numarul de campanii de pe primul rand

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); //variabila folosita pentru formatarea datei
                    LocalDateTime date = LocalDateTime.parse(sc.nextLine(), formatter); //citim data curenta de pe al doilea rand

                    String campaignInfo;
                    UserVoucherMap<Integer, List<Voucher>> vMap = new UserVoucherMap(); //cream dictionarul care are ca si chei id-urile ampaniilor
                    CampaignVoucherMap<String, List<Voucher>> cMap = new CampaignVoucherMap<>(); //cream dictionarul care are ca si chei email-urile user-ilor
                    VMS vms = VMS.getInstance(); //instantiem o singura data clasa VMS
                    List<User> observers = new ArrayList<User>(); //lista de observatori

                    PrintWriter writer = null;
                    try {
                        writer = new PrintWriter("output.txt", "UTF-8"); //variabila folosita pentru scrierea in fisierul output.txt
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    for (int i = 0; i < campaignsNumber; i++) { //citim pe rand toate randurile
                        if (sc.hasNext()) {
                            campaignInfo = sc.nextLine();
                            StringTokenizer st = new StringTokenizer(campaignInfo, ";"); //delimitam fiecare rand in subsiruri delimitate de ";"


                            Integer id = Integer.parseInt(st.nextToken()); //id-ul campaniei
                            String name = st.nextToken();  //numele campaniei
                            String description = st.nextToken(); //descrierea campaniei
                            LocalDateTime start = LocalDateTime.parse(st.nextToken(), formatter); //data de inceput a campaniei
                            LocalDateTime finish = LocalDateTime.parse(st.nextToken(), formatter); //data de final a campaniei
                            int budget = Integer.parseInt(st.nextToken()); //bugetul campaniei
                            String strategy = st.nextToken(); //strategia campaniei
                            CampaignStatusType status = CampaignStatusType.NEW; //statusul campaniei


                            if (date.isBefore(finish) == true && date.isAfter(start) == true) //setam statusul campaniei in functie e data curenta si perioada ei de desfasurare
                                status = CampaignStatusType.STARTED;
                            else if (date.isEqual(start))
                                status = CampaignStatusType.NEW;
                            else if (date.isEqual(finish))
                                status = CampaignStatusType.CANCELLED;
                            else if (date.isAfter(finish))
                                status = CampaignStatusType.EXPIRED;

                            Campaign c = new Campaign(id, description, name, start, finish, budget, strategy, status); //cream o noua campanie cu informatiile citite
                            vms.addCampaign(c); //si o adaugam la lista de campanii din vms
                        }
                    }


                    //citim informatiile din fisierul users.txt

                    File user = new File(usersPath.getText()); //folosim calea obtinuta in fereastra text pentru a deschie fisierul
                    Scanner u = null;
                    try {
                        u = new Scanner(user);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    int usersNumber = Integer.parseInt(u.nextLine()); //citim numarul de useri de pe prima linie
                    String userInfo;

                    for (int i = 0; i < usersNumber; i++) {
                        if (u.hasNext()) {
                            userInfo = u.nextLine(); //va contine pe rand informatiile de pe fiecare rand
                            StringTokenizer s = new StringTokenizer(userInfo, ";"); //delimitam fiecare rand in subsiruri delimitate de ";"

                            int id = Integer.parseInt(s.nextToken()); //id-ul userului
                            String name = s.nextToken(); //numele userului
                            String password = s.nextToken(); //parola userului
                            String email = s.nextToken(); //emailul userului
                            String status = s.nextToken(); //statusul userului
                            UserType userStatus; //tipul userului


                            if ("ADMIN".equals(status))  //setam statusul
                                userStatus = UserType.ADMIN;
                            else
                                userStatus = UserType.GUEST;

                            User newUser = new User(id, name, password, email, userStatus); //cream un nou user cu informatiile citite
                            vms.addUser(newUser); //si il adaugam la lista de useri din vms

                        }
                    }


                    //citim informatiile din fisierul events.txt
                    File events = new File(eventsPath.getText()); //folosim calea obtinuta in fereastra text pentru a deschie fisierul
                    Scanner e = null;

                    try {
                        e = new Scanner(events);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    LocalDateTime currentDate = LocalDateTime.parse(e.nextLine(), formatter); //citim data curenta de pe prima linie
                    int eventsNumber = Integer.parseInt(e.nextLine()); //citim numarul de eventuri de pe a doua linie
                    int userId;
                    String eventsInfo; //va contine pe rand informatiile de pe fiecare rand

                    for (int i = 0; i < eventsNumber; i++) {
                        eventsInfo = e.nextLine();
                        StringTokenizer ev = new StringTokenizer(eventsInfo, ";"); //delimitam fiecare rand in subsiruri delimitate de ";"

                        userId = Integer.parseInt(ev.nextToken()); //citim id-ul userului
                        String method = ev.nextToken();

                        //am impartit metodele in categorii dupa formatul inputului
                        if (method.equals("cancelCampaign") || method.equals("getObservers") || method.equals("getVoucher")) {
                            int campaignId = Integer.parseInt(ev.nextToken()); //id-ul campaniei
                            User auxUser = null;
                            List<Voucher> l;
                            Voucher getV;

                            if (method.equals("cancelCampaign")) {
                                User uc;

                                for (int rb = 0; rb < vms.users.size(); rb++) {
                                    if (vms.users.get(rb).userID == userId) { //cautam userul in lista dupa id
                                        uc = vms.users.get(rb);

                                        if (uc.status.equals(UserType.ADMIN)) { //verificam daca e admin
                                            vms.cancelCampaign(campaignId); //apelam metoda "cancel campaign" din clasa VMS
                                            writer.println("[" + campaignId + ";" + "[2]" + ";" + currentDate + ";" + "CANCEL]");
                                        }
                                    }
                                }

                                Notification not = new Notification(campaignId, NotificationType.CANCEL, currentDate); //cream o noua notificare

                                for (int je = 0; je < vms.campaigns.size(); je++) {
                                    if (vms.campaigns.get(je).campaignID == campaignId)
                                        vms.campaigns.get(je).notifyAllObservers(not); //notificam toti observatorii care au vouchere pentru campania modificata
                                }
                            }

                            if (method.equals("getVoucher")) {
                                for (int je = 0; je < vms.users.size(); je++) {
                                    if (vms.users.get(je).userID == userId) //cautam userul in lista dupa id
                                        auxUser = vms.users.get(je);
                                }

                                for (int je = 0; je < cMap.list.size(); je++) {
                                    if (auxUser.email.equals(cMap.list.get(je).getKey()) && auxUser.status.equals(UserType.ADMIN)) { //cautam lista devouchere a userului daca acesta e admin
                                        l = cMap.list.get(je).getValue();

                                        for (int k = 0; k < l.size(); k++) {
                                            if (l.get(k).campaignID == campaignId) { //cautam in lista voucherele din campania precizata
                                                getV = l.get(k);
                                                writer.println("[[" + getV.id + ";" + getV.status + ";" + getV.email + ";" + getV.amount + ";" + getV.campaignID + ";" + getV.date + "]]");
                                            }
                                        }
                                    }

                                }
                            }
                            if (method.equals("getObservers")) {
                                Campaign camp = null;
                                for(int t = 0; t < vms.campaigns.size(); t++) {
                                    if(vms.campaigns.get(t).campaignID == campaignId) //cautam campania cu id-ul dat
                                        camp = vms.campaigns.get(t);
                                }

                                for (int t = 0; t < vms.users.size(); t++) {
                                    if (vms.users.get(t).status.equals(UserType.ADMIN)) { //cautam doar printre userii care sunt admini

                                        for (int je = 0; je < cMap.list.size(); je++) {
                                            if (vms.users.get(t).email.equals(cMap.list.get(je).getKey())) {
                                                l = cMap.list.get(je).getValue(); //cautam lista de vouchere a userului respectiv

                                                for (int k = 0; k < l.size(); k++) {
                                                    if (l.get(k).campaignID == campaignId) { //daca voucherul face parte dn campanie am gsit un observator
                                                        observers.add(vms.users.get(t));
                                                        writer.println("[" + vms.users.get(t).userID + ";" + vms.users.get(t).name + ";" + vms.users.get(t).email + ";" + vms.users.get(t).status + ";");
                                                    }
                                                }
                                            }
                                        }

                                    }

                                }


                            }

                        }
                        if (method.equals("getNotifications") || method.equals("getVouchers")) {
                            if (method.equals("getVouchers")) {
                                User us;
                                List<Voucher> vouchers = null;

                                for (int rb = 0; rb < vms.users.size(); rb++) {
                                    if (vms.users.get(rb).userID == userId) { //cautam user-ul cu id-ul dat
                                        us = vms.users.get(rb);

                                        for (int t = 0; t < cMap.list.size(); t++) {
                                            if (cMap.list.get(t).getKey().equals(us.email) && us.status.equals(UserType.GUEST)) { //cautam userul in dictionar si verificam ca e guest
                                                vouchers = cMap.list.get(t).getValue(); //obtinem lista sa de vouchere
                                            }
                                        }
                                    }
                                }

                                if (vouchers != null)
                                    for (int je = 0; je < vouchers.size(); je++) {
                                        for (int k = 0; k < vms.users.size(); k++)
                                            if (vms.users.get(k).email.equals(vouchers.get(je).email))
                                                writer.println("[[" + vms.users.get(k).userID + ";" + vouchers.get(je).status + ";" + vouchers.get(je).email + ";" + vouchers.get(je).amount + ";" + vouchers.get(je).campaignID + ";" + vouchers.get(je).date + "]]");
                                    }


                            }
                            if (method.equals("getNotifications")) {
                                for (int k = 0; k < vms.users.size(); k++)
                                    if (vms.users.get(k).userID == userId) //cautam in lista de user pe cel cu id-ul dat
                                        if (vms.users.get(k).status.equals(UserType.GUEST) && vms.users.get(k).notifications != null) //verificam ca e guest
                                            for (int t = 0; t < vms.users.get(k).notifications.size(); t++)
                                                writer.println("[" + vms.users.get(k).userID + ";" + vms.users.get(k).name + ";" + vms.users.get(k).email + ";" + vms.users.get(k).status + "]");
                            }
                        }
                        if (method.equals("addCampaign") || method.equals("editCampaign")) {
                            Integer id = Integer.parseInt(ev.nextToken()); //id-ul campaniei
                            String name = ev.nextToken(); //numele campaniei
                            String description = ev.nextToken(); //descrierea campaniei
                            LocalDateTime start = LocalDateTime.parse(ev.nextToken(), formatter); //data de inceput a campaniei
                            LocalDateTime finish = LocalDateTime.parse(ev.nextToken(), formatter); //data de final a campaniei
                            int budget = Integer.parseInt(ev.nextToken()); //bugetul campaniei
                            String strategy; //strategia campaniei

                            if (method.equals("editCampaign")) {
                                Campaign edit = new Campaign(description, start, finish, budget); //cream o noua campanie cu informatiile date

                                vms.updateCampaign(id, edit);
                                Notification not = new Notification(id, NotificationType.EDIT, currentDate); //cream si o notificare deoarece o campanie a fost modificata

                                for (int je = 0; je < vms.campaigns.size(); je++) {
                                    if (vms.campaigns.get(je).campaignID == id)
                                        vms.campaigns.get(je).notifyAllObservers(not);
                                }

                                writer.println("[" + id + ";[" + userId + "];" + currentDate + ";EDIT]");
                            }

                            if (method.equals("addCampaign")) {
                                strategy = ev.nextToken(); //strategia campaniei

                                Campaign newCampaign = new Campaign(id, description, name, start, finish, budget, strategy, CampaignStatusType.NEW); //cream o noua campanie
                                vms.addCampaign(newCampaign);

                            }
                        }
                        if (method.equals("generateVoucher")) {

                            int campaignId = Integer.parseInt(ev.nextToken()); //id-ul campaniei
                            String email = ev.nextToken(); //email-ul user-ului
                            String voucherType = ev.nextToken(); //tipul voucherului
                            int value = Integer.parseInt(ev.nextToken()); //valoarea voucherului

                            Campaign c = null;
                            for (int je = 0; je < vms.campaigns.size(); je++)
                                if (vms.campaigns.get(je).campaignID == campaignId) //cautam campania dupa id-ul dat
                                    c = vms.campaigns.get(je);

                            Voucher voucher = c.generateVoucher(email, voucherType, value); //generam un nou voucher

                            if (voucher != null) { //setam si id-ul campaniei si valoarea voucherului
                                voucher.campaignID = campaignId;
                                voucher.amount = value;
                            }


                            for (int je = 0; je < vms.users.size(); je++) {
                                if (vms.users.get(je).userID == userId)
                                    if (vms.users.get(je).status.equals(UserType.ADMIN)) { //adaugam voucherul la cele 2 dictionare daca userul e admin
                                        vMap.addVoucher(voucher);
                                        cMap.addVoucher(voucher);
                                    }
                            }

                        }
                        if (method.equals("redeemVoucher")) {

                            int campaignId = Integer.parseInt(ev.nextToken()); //id-ul campaniei
                            int voucherId = Integer.parseInt(ev.nextToken()); // id-ul voucherului
                            LocalDateTime localDate = LocalDateTime.parse(ev.nextToken(), formatter); // data curenta

                            Campaign d = null;
                            for (int je = 0; je < vms.campaigns.size(); je++)
                                if (vms.campaigns.get(je).campaignID == campaignId) //cautam campania dupa id
                                    d = vms.campaigns.get(je);


                            Voucher auxVoucher = null;
                            List<Voucher> voucherList = vMap.get(d.campaignID); //obtinem lista de vouchere din campanie

                            for (int p = 0; p < vms.users.size(); p++) {
                                if (vms.users.get(p).userID == userId) //cautam userul dupa id
                                    if (vms.users.get(p).status.equals(UserType.ADMIN)) { //verificam ca e admin
                                        for (int z = 0; z < voucherList.size(); z++)
                                            if (voucherList.get(z).id == voucherId) { //cautam voucherul dupa id
                                                auxVoucher = voucherList.get(z);
                                                d.redeemVoucher(Integer.toString(auxVoucher.cod), localDate);
                                                writer.println("[[" + auxVoucher.id + ";" + auxVoucher.status + ";" + auxVoucher.email + ";" + auxVoucher.amount + ";" + auxVoucher.campaignID + ";" + localDate + "]]");
                                            }
                                    }
                            }
                        }
                    }
                    for (int je = 0; je < observers.size(); je++) {
                        writer.println("[[" + observers.get(je).userID + ";" + observers.get(je).name + ";" + observers.get(je).email + ";" + observers.get(je).status + "]]");
                    }

                    Test test = new Test(vms.campaigns, vMap, vms.users, cMap);
                    writer.close(); //inchidem fisierul de output
                }
            }

        });


        JPanel pa = new JPanel();
        pa.setBackground(Color.green);

        //adaugam casetele ext si butoanele pentru pagina de selectare a fisierelor
        pa.add(btn_campaign);
        pa.add(campaignPath);

        pa.add(btn_users);
        pa.add(usersPath);

        pa.add(btn_events);
        pa.add(eventsPath);

        pa.add(button2);

        f.add(pa);

        f.show();
    }
}
