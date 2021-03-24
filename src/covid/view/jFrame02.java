package covid.view;

import covid.controller.CountryJpaController;
import covid.controller.CoviddataJpaController;
import covid.controller.DBController;
import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import covid.model.Country;
import covid.model.Coviddata;
import covid.service.Mappage;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class jFrame02 extends javax.swing.JFrame {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CovidDBPU1");
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    CountryJpaController countryController = new CountryJpaController(emf);
    CoviddataJpaController coviddataController = new CoviddataJpaController(emf);
    DBController dbController = new DBController();
    // για το γεμισμα του drop down menu επιλογης χωρας
    List<Country> countries;
    DefaultComboBoxModel comboboxmodel;
    DefaultComboBoxModel comboboxmodel1;
    DefaultTableModel deathsmodel;
    DefaultTableModel recoveredmodel;
    DefaultTableModel confirmedmodel;
    DefaultTableModel emptymodel = new DefaultTableModel();
    SortedMap<LocalDate, Coviddata> deathsMap;
    SortedMap<LocalDate, Coviddata> confirmedMap;
    SortedMap<LocalDate, Coviddata> recoveredMap;

    /**
     * Creates new form jFrame02
     */
    public jFrame02() {
        initComponents();
        // ρυθμιση οταν κλεινει το jFrame να μην κλεινει ολο το app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // after init code
        // γεμισ΄μα του drop down menu και των menu επιλογης ημερομηνια
        fillCountriesMenu();
    }

    // γεμιζει το dropdownmenu για την επιλογη χωρας
    private void fillCountriesMenu() {
        countries = countryController.findCountryEntities();
        comboboxmodel = new DefaultComboBoxModel();
        // πρωτη επιλογη κενή
        comboboxmodel.addElement("");
        for (Country country : countries) {
            comboboxmodel.addElement(country.getName());
        }
        jComboBox8.setModel(comboboxmodel);
    }

    // γεμιζει το dropdownmenu για την επιλογη ημερομηνιας
    private void filldates(LocalDate first, LocalDate last) {

        comboboxmodel = new DefaultComboBoxModel();
        comboboxmodel1 = new DefaultComboBoxModel();

        while (true) {
            if (first.equals(last.plusDays(1))) {
                break;
            }

            comboboxmodel.addElement(first.toString());
            comboboxmodel1.addElement(first.toString());

            first = first.plusDays(1);
        }

        // ημερομηνια απο
        jComboBox3.setModel(comboboxmodel);

        // ημερομηνια εως
        jComboBox4.setModel(comboboxmodel1);
        int i = jComboBox4.getItemCount();
        jComboBox4.setSelectedIndex(i - 1);
    }

    // γεμιζει τα τραπεζια με τα δεδομενα, αναλογα με το first και last 
    // date που δινεται στην κληση της.
    private void filltables(LocalDate first, LocalDate last) {
        // δημιουργια μοντελων του πινακα. ιδιοτητες του jtable
        deathsmodel = new DefaultTableModel();
        recoveredmodel = new DefaultTableModel();
        confirmedmodel = new DefaultTableModel();

        // επιλογη τιτλων για τις στηλες
        deathsmodel.setColumnIdentifiers(new String[]{"Ημέρα", "Αριθμος", "Συνολο"});
        recoveredmodel.setColumnIdentifiers(new String[]{"Ημέρα", "Αριθμος", "Συνολο"});
        confirmedmodel.setColumnIdentifiers(new String[]{"Ημέρα", "Αριθμος", "Συνολο"});

        // διαπεραση των SortedList για γέμισμα των πινακων
        if (deathsMap.size() > 0) {

            for (LocalDate ldate : deathsMap.keySet()) {
                if ((ldate.isAfter(first) && ldate.isBefore(last)) || ldate.isEqual(first) || ldate.isEqual(last)) {
                    // εισαγωγη μιας γραμμης στον πίνακα
                    deathsmodel.addRow(new String[]{ldate.toString(), String.valueOf(deathsMap.get(ldate).getQty()), String.valueOf(deathsMap.get(ldate).getProodqty())});
                }
            }
        }
        if (confirmedMap.size() > 0) {

            for (LocalDate ldate : confirmedMap.keySet()) {

                if ((ldate.isAfter(first) && ldate.isBefore(last)) || ldate.isEqual(first) || ldate.isEqual(last)) {
                    // εισαγωγη μιας γραμμης στον πίνακα
                    confirmedmodel.addRow(new String[]{ldate.toString(), String.valueOf(confirmedMap.get(ldate).getQty()), String.valueOf(confirmedMap.get(ldate).getProodqty())});
                }
            }
        }

        if (recoveredMap.size() > 0) {

            for (LocalDate ldate : recoveredMap.keySet()) {

                if ((ldate.isAfter(first) && ldate.isBefore(last)) || ldate.isEqual(first) || ldate.isEqual(last)) {
                    // εισαγωγη μιας γραμμης στον πίνακα
                    recoveredmodel.addRow(new String[]{ldate.toString(), String.valueOf(recoveredMap.get(ldate).getQty()), String.valueOf(recoveredMap.get(ldate).getProodqty())});
                }
            }
        }

        // στοιχιση στο κεντρο
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        jTable1.setDefaultRenderer(Object.class, centerRenderer);
        jTable2.setDefaultRenderer(Object.class, centerRenderer);
        jTable3.setDefaultRenderer(Object.class, centerRenderer);

        // update τα jTable με τα models
        jTable1.setModel(deathsmodel);
        jTable2.setModel(recoveredmodel);
        jTable3.setModel(confirmedmodel);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jButton12 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel10.setText("Επιλογή Χώρας :");

        jComboBox8.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButton12.setText("Προβολή Σε Χάρτη");
        jButton12.setEnabled(false);
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton12MousePressed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButton5.setText("Διαγραφή Δεδομένων");
        jButton5.setEnabled(false);
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel4.setText("Από ημερομηνία");

        jComboBox3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jComboBox3.setEnabled(false);
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel5.setText("Έως ημερομηνία");

        jComboBox4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jComboBox4.setEnabled(false);
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel6.setText("Θάνατοι");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "TRNDATE", "QTY", "PRODQTY"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "TRNDATE", "QTY", "PRODQTY"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel7.setText("Επιβεβαιωμένα Κρούσματα");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel8.setText("Ασθενείς που ανέκαμψαν");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "TRNDATE", "QTY", "PRODQTY"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" ");

        jButton15.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jButton15.setText("Προβολη Διαγράμματος");
        jButton15.setEnabled(false);
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton15MousePressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel11.setText("Επιλογή Είδους Διαγράμματος :");

        jComboBox1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Πλήρες Διάγραμμα", "Θάνατοι", "Επιβεβαιωμένα Κρούσματα", "Ασθενείς που ανάρρωσαν" }));
        jComboBox1.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(335, 335, 335)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addComponent(jLabel6))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jLabel7)
                                .addGap(100, 100, 100)
                                .addComponent(jLabel8))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton15))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(220, 220, 220)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ολα τα event handler για το jFrame02
    // διαγραφη δεδομενων μιας χώρας
    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MousePressed
        // TODO add your handling code here:
        System.out.println("Πατηθηκε η Διαγραφη δεδομενων μιας χωρας");
        // εμφανιση παραθυρου επιβεβαιωσης της διαγραφης
        int a = JOptionPane.showConfirmDialog(null, "Διαγραφη των δεδομενων της χώρας.\n"
                + "Είστε σίγουροι;",
                "Διαγραφή Δεδομένων", JOptionPane.YES_NO_OPTION);
        if (a == JOptionPane.YES_OPTION) {
            try {

                // επιλογη της χωρας
                Country country = dbController.getCountryByName(jComboBox8.getSelectedItem().toString());
                dbController.deleteCovviddataByCountry(country);

                // διαγραφη των δεδομενων απο τον πινακα εαν υπαρχουν σε προβολη
                // update τα jTable με τα models
                jTable1.setModel(emptymodel);
                jTable2.setModel(emptymodel);
                jTable3.setModel(emptymodel);

                // enable set false για τα κουμπια που τωρα δεν θα πρεπει να μπορουν να πατηθουν
                // εφοσον για τη χωρα πλεον δεν υπαρχουν δεδομενα
                jButton12.setEnabled(false);
                jButton15.setEnabled(false);
                jButton5.setEnabled(false);
                jComboBox1.setEnabled(false);
                jComboBox3.setEnabled(false);
                jComboBox4.setEnabled(false);

            } catch (Exception ex) {
                System.out.println("Κατι δεν πηγε καλα με τη διαγραφη της χώρας");
            }
        }
    }//GEN-LAST:event_jButton5MousePressed

    // προβολη σε χαρτη
    private void jButton12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MousePressed
        // TODO add your handling code here:
        System.out.println("Πατηθηκε η προβολη σε χαρτη για μια χώρα. Οθονη Β");

        // συνεχιζουμε μονο αν υπαρχουν ημερομηνιες στις επιλογες των drop down menu
        // διαφορετικα σημαινει οτι η χωρα δεν εχει αποθηκευμενα δεδομενα coviddata στη ΒΔ
        if (!(jComboBox3.getSelectedItem() != null)) {
            JOptionPane.showMessageDialog(null, "Δεν υπαρχουν δεδομενα για τη χώρα", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // αρχικη και τελικη ημερομηνια
        if (LocalDate.parse(jComboBox3.getSelectedItem().toString()).isAfter(LocalDate.parse(jComboBox4.getSelectedItem().toString()))) {
            JOptionPane.showMessageDialog(null, "Η ημερομηνια Εως δεν μπορει να ειναι νωριτερα\n"
                    + "απο την ημερομηνια Απο. Δοκιμασε παλι!",
                    "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // συνεχεια
        // κωδικας ομοιος της Οθονης Γ για την προβολη χαρτη με την αλλαγη οτι δεν εχουμε
        // λιστα επιπλεον χωρών για προβολη αλλα μονο μια κεντρικη χώρα
        // επιλογη χωρας απο το drop down menu
        int datakind;
        int totalDeaths, totalRecovered, totalConfirmed;
        String resultlist[][] = new String[1][4];
        String lat, long1;
        Country country;

        String countryName = jComboBox8.getSelectedItem().toString();

        try {
            country = dbController.getCountryByName(countryName);
        } catch (Exception ex1) {
            System.out.println("Δεν μπορεσε να ανακτηθει η χωρα απο τη ΒΔ");
            JOptionPane.showMessageDialog(null, "Δεν μπορεσε να ανακτηθει η χωρα απο τη ΒΔ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // για να διατηρησουμε ημερολογιακη σειρα στους πινακες δεδομενων θα εισαγουμε τα δεδομενα
        // σε data structure που μας προσφερει η Java τα οποια αυτοματα διατηρουν μια σειρα
        SortedMap<LocalDate, Coviddata> deathsMap = new TreeMap<>();
        SortedMap<LocalDate, Coviddata> confirmedMap = new TreeMap<>();
        SortedMap<LocalDate, Coviddata> recoveredMap = new TreeMap<>();

        // αρχικη και τελικη ημερομηνια που επιλεγει ο χρηστης
        LocalDate date;
        LocalDate first = LocalDate.parse(jComboBox3.getSelectedItem().toString());
        LocalDate last = LocalDate.parse(jComboBox4.getSelectedItem().toString());

        // αναζητουμε τα δεδομενα για αυτη τη χώρα
        List<Coviddata> coviddataList;

        // deaths
        try {
            // αναζητουμε τα δεδομενα στη ΒΔ
            coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 1);
            if (!coviddataList.isEmpty()) {

                for (Coviddata coviddata : coviddataList) {

                    date = LocalDate.parse(coviddata.getTrndate());
                    deathsMap.put(date, coviddata);

                }
            }
        } catch (Exception ex) {
            System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
        }

        // recovered
        try {
            // αναζητουμε τα δεδομενα στη ΒΔ
            coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 2);
            if (!coviddataList.isEmpty()) {

                for (Coviddata coviddata : coviddataList) {

                    date = LocalDate.parse(coviddata.getTrndate());
                    recoveredMap.put(date, coviddata);

                }
            }

        } catch (Exception ex) {
            System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
        }

        // confirmed
        try {
            // αναζητουμε τα δεδομενα στη ΒΔ
            coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 3);
            if (!coviddataList.isEmpty()) {

                for (Coviddata coviddata : coviddataList) {

                    date = LocalDate.parse(coviddata.getTrndate());
                    confirmedMap.put(date, coviddata);

                }
            }
        } catch (Exception ex) {
            System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
        }

        if (deathsMap.size() > 0) {
            // ποσοτητα τελικης - αρχικης ημερομηνιας ειναι το ποσο που ζηταμε να δειξουμε
            if (deathsMap.containsKey(first) && deathsMap.containsKey(last)) {

                totalDeaths = deathsMap.get(last).getProodqty() - deathsMap.get(first).getProodqty();

            } else if (deathsMap.containsKey(first) && !deathsMap.containsKey(last)) {
                // αν η τελικη ημερομηνια δεν υπαρχει αποθηκευμενη στη ΒΔ τοτε επιλεγουμε την πιο προσφατη ημερομηνια
                // που υπαρχει στο
                totalDeaths = deathsMap.get(deathsMap.lastKey()).getProodqty() - deathsMap.get(first).getProodqty();

            } else {
                // αν η αρχικη ημερομηνια επιλογης ειναι χρονικα μετα απο την τελευταια αποθηκευμενη στη ΒΔ
                // δεν υπαρχουν δεδομενα να δειξουμε αρα 0
                totalDeaths = 0;
            }

        } else {
            // για καποιο λογο δεν εχουμε δεδομενα να δειξουμε
            totalDeaths = 0;
        }

        if (confirmedMap.size() > 0) {
            // ποσοτητα τελικης - αρχικης ημερομηνιας ειναι το ποσο που ζηταμε να δειξουμε
            if (confirmedMap.containsKey(first) && confirmedMap.containsKey(last)) {

                totalConfirmed = confirmedMap.get(last).getProodqty() - confirmedMap.get(first).getProodqty();

            } else if (confirmedMap.containsKey(first) && !confirmedMap.containsKey(last)) {
                // αν η τελικη ημερομηνια δεν υπαρχει αποθηκευμενη στη ΒΔ τοτε επιλεγουμε την πιο προσφατη ημερομηνια
                // που υπαρχει στο
                totalConfirmed = confirmedMap.get(confirmedMap.lastKey()).getProodqty() - confirmedMap.get(first).getProodqty();

            } else {
                // αν η αρχικη ημερομηνια επιλογης ειναι χρονικα μετα απο την τελευταια αποθηκευμενη στη ΒΔ
                // δεν υπαρχουν δεδομενα να δειξουμε αρα 0
                totalConfirmed = 0;
            }
        } else {
            // για καποιο λογο δεν εχουμε δεδομενα να δειξουμε
            totalConfirmed = 0;
        }
        if (recoveredMap.size() > 0) {
            // ποσοτητα τελικης - αρχικης ημερομηνιας ειναι το ποσο που ζηταμε να δειξουμε
            if (recoveredMap.containsKey(first) && recoveredMap.containsKey(last)) {

                totalRecovered = recoveredMap.get(last).getProodqty() - recoveredMap.get(first).getProodqty();

            } else if (recoveredMap.containsKey(first) && !recoveredMap.containsKey(last)) {
                // αν η τελικη ημερομηνια δεν υπαρχει αποθηκευμενη στη ΒΔ τοτε επιλεγουμε την πιο προσφατη ημερομηνια
                // που υπαρχει στο
                totalRecovered = recoveredMap.get(recoveredMap.lastKey()).getProodqty() - recoveredMap.get(first).getProodqty();

            } else {
                // αν η αρχικη ημερομηνια επιλογης ειναι χρονικα μετα απο την τελευταια αποθηκευμενη στη ΒΔ
                // δεν υπαρχουν δεδομενα να δειξουμε αρα 0
                totalRecovered = 0;
            }
        } else {
            // για καποιο λογο δεν εχουμε δεδομενα να δειξουμε
            totalRecovered = 0;
        }

        resultlist[0][0] = "'" + country.getName() + ", Deaths = " + totalDeaths
                + " , Confirmed = " + totalConfirmed + ", Recovered = " + totalRecovered + "'";
        resultlist[0][1] = String.valueOf(country.getLat());
        resultlist[0][2] = String.valueOf(country.getLong1());
        resultlist[0][3] = String.valueOf(1);
        lat = String.valueOf(country.getLat());
        long1 = String.valueOf(country.getLong1());

        try {
            // κληση του Mappage για το ανοιγμα του χαρτη
            Mappage.loadGoogleMap(resultlist, lat, long1);

        } catch (IOException ex) {

            System.out.println(ex);
        }
    }//GEN-LAST:event_jButton12MousePressed

    // προβολη δεδομενων στους πινακες για μια χωρα
    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        // TODO add your handling code here:
        System.out.println("Επιλεχθηκε χωρα για προβολη");

        // βρισκουμε τη χωρα για την οποια θα αναζητησουμε τα δεδομενα
        String countryName = jComboBox8.getSelectedItem().toString();

        // εαν διαλεξει τη null επιλογη δεν κανουμε τπτ
        if (countryName.equals("")) {
            // disale τα κουμπια Προβολη Χαρτη, Προβολη Διαγραμματος, Διαγραφη Δεδομεμων
            jButton12.setEnabled(false);
            jButton15.setEnabled(false);
            jButton5.setEnabled(false);
            jComboBox1.setEnabled(false);
            jComboBox3.setEnabled(false);
            jComboBox4.setEnabled(false);

            // update τα jTable με τα models
            jTable1.setModel(emptymodel);
            jTable2.setModel(emptymodel);
            jTable3.setModel(emptymodel);

            return;
        }

        Country country = dbController.getCountryByName(countryName);

        // εαν για τη χωρα δεν υπαρχουν δεδομενα δεν θελουμε να επιτρεπουμε τα υπολοιπα
        // πληκτρα να ειναι ενεργα. Δηλαδη αν επιλεξουμε μια χωρα με δεδομενα, στην αλλαγη
        // σε μια χωρα χωρις δεδομενα απενεργοποιουμε τα πληκτρα για να αποφυγουμε
        // καταστασεις που μπορει να "κρασαρουν" την εφαρμογη
        try {
            List<Coviddata> list = dbController.getCoviddataByCountry(country);
            if (list.size() == 0) {

                // update τα jTable με τα models
                jTable1.setModel(emptymodel);
                jTable2.setModel(emptymodel);
                jTable3.setModel(emptymodel);
                jButton12.setEnabled(false);
                jButton15.setEnabled(false);
                jButton5.setEnabled(false);
                jComboBox1.setEnabled(false);
                jComboBox3.setEnabled(false);
                jComboBox4.setEnabled(false);
                return;
            }
            System.out.println(list.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Κατι πηγε στραβα. Δοκιμασε να επανεκκινησεις την εφαρμογη.", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
        }

        // εισαγουμε τη λογικη της γεμισης των πινακων 
        // δημιουργια μοντελων του πινακα. ιδιοτητες του jtable
        deathsmodel = new DefaultTableModel();
        recoveredmodel = new DefaultTableModel();
        confirmedmodel = new DefaultTableModel();

        // επιλογη τιτλων για τις στηλες
        deathsmodel.setColumnIdentifiers(new String[]{"Ημέρα", "Αριθμος", "Συνολο"});
        recoveredmodel.setColumnIdentifiers(new String[]{"Ημέρα", "Αριθμος", "Συνολο"});
        confirmedmodel.setColumnIdentifiers(new String[]{"Ημέρα", "Αριθμος", "Συνολο"});

        // για να διατηρησουμε ημερολογιακη σειρα στους πινακες δεδομενων θα εισαγουμε τα δεδομενα
        // σε data structure που μας προσφερει η Java τα οποια αυτοματα διατηρουν μια σειρα
        deathsMap = new TreeMap<>();
        confirmedMap = new TreeMap<>();
        recoveredMap = new TreeMap<>();

        LocalDate date, first, last;
        // πρωτη και τελευταια ημερομηνια αντιστροφα για τις συγκρισεις, και επειτα για τις επιλογες των μενου
        first = LocalDate.now();
        last = LocalDate.of(2020, 01, 22);

        // αναζητουμε τα δεδομενα για αυτη τη χώρα
        List<Coviddata> coviddataList;

        // deaths
        try {
            // αναζητουμε τα δεδομενα στη ΒΔ
            coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 1);
            if (!coviddataList.isEmpty()) {

                for (Coviddata coviddata : coviddataList) {

                    date = LocalDate.parse(coviddata.getTrndate());
                    deathsMap.put(date, coviddata);

                }
            }
        } catch (Exception ex) {
            System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
        }

        // recovered
        try {
            // αναζητουμε τα δεδομενα στη ΒΔ
            coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 2);
            if (!coviddataList.isEmpty()) {

                for (Coviddata coviddata : coviddataList) {

                    date = LocalDate.parse(coviddata.getTrndate());
                    recoveredMap.put(date, coviddata);

                }
            }

        } catch (Exception ex) {
            System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
        }

        // confirmed
        try {
            // αναζητουμε τα δεδομενα στη ΒΔ
            coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 3);
            if (!coviddataList.isEmpty()) {

                for (Coviddata coviddata : coviddataList) {

                    date = LocalDate.parse(coviddata.getTrndate());
                    confirmedMap.put(date, coviddata);

                }
            }
        } catch (Exception ex) {
            System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
        }

        // διαπεραση των SortedList για γέμισμα των πινακων
        if (deathsMap.size() > 0) {

            // πρωτη και τελευταια ημερομηνια για το πεδιο επιλογης
            first = deathsMap.firstKey();
            last = deathsMap.lastKey();

            for (LocalDate ldate : deathsMap.keySet()) {

                // εισαγωγη μιας γραμμης στον πίνακα
                deathsmodel.addRow(new String[]{ldate.toString(), String.valueOf(deathsMap.get(ldate).getQty()), String.valueOf(deathsMap.get(ldate).getProodqty())});

            }
        }
        if (confirmedMap.size() > 0) {

            // ελεγχος για ημερομηνιες
            if (deathsMap.firstKey().isBefore(first)) {
                first = deathsMap.firstKey();
            }
            if (deathsMap.lastKey().isAfter(last)) {
                last = deathsMap.lastKey();
            }

            for (LocalDate ldate : confirmedMap.keySet()) {

                // εισαγωγη μιας γραμμης στον πίνακα
                confirmedmodel.addRow(new String[]{ldate.toString(), String.valueOf(confirmedMap.get(ldate).getQty()), String.valueOf(confirmedMap.get(ldate).getProodqty())});

            }
        }

        if (recoveredMap.size() > 0) {

            // ελεγχος για ημερομηνιες
            if (deathsMap.firstKey().isBefore(first)) {
                first = deathsMap.firstKey();
            }
            if (deathsMap.lastKey().isAfter(last)) {
                last = deathsMap.lastKey();
            }

            for (LocalDate ldate : recoveredMap.keySet()) {

                // εισαγωγη μιας γραμμης στον πίνακα
                recoveredmodel.addRow(new String[]{ldate.toString(), String.valueOf(recoveredMap.get(ldate).getQty()), String.valueOf(recoveredMap.get(ldate).getProodqty())});

            }
        }

        // λειτουργιες μονο αν υπαρχουν δεδομενα να δειξουμε
        if (recoveredMap.size() > 0 || confirmedMap.size() > 0 || deathsMap.size() > 0) {
            filldates(first, last);

            // στοιχιση στο κεντρο
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            jTable1.setDefaultRenderer(Object.class, centerRenderer);
            jTable2.setDefaultRenderer(Object.class, centerRenderer);
            jTable3.setDefaultRenderer(Object.class, centerRenderer);

            // update τα jTable με τα models
            jTable1.setModel(deathsmodel);
            jTable2.setModel(recoveredmodel);
            jTable3.setModel(confirmedmodel);

            // εμφανιση πεδιου χωρας της οποιας τα στατιστικα εμφανιζονται
            jLabel1.setText(countryName);

            // enable τα κουμπια Διαγραφη Δεδομενων, Προβολη Χαρτη, Προβολη Διαγραμματος
            // μενου επιλογης τυπου διαγραμματος
            jButton12.setEnabled(true);
            jButton15.setEnabled(true);
            jButton5.setEnabled(true);
            jComboBox1.setEnabled(true);
            jComboBox3.setEnabled(true);
            jComboBox4.setEnabled(true);
        } else {
            // αλλιως εισαγουμε ενα αδειο model στους πινακες, για να ειναι αδειοι
            jTable1.setModel(emptymodel);
            jTable2.setModel(emptymodel);
            jTable3.setModel(emptymodel);

        }


    }//GEN-LAST:event_jComboBox8ItemStateChanged

    // προβολη διαγραμματος αναλογα την επιλογη στο dropdown menu
    private void jButton15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MousePressed
        // TODO add your handling code here:
        System.out.println("Πατηθηκε η προβολη σε ΔΙΑΓΡΑΜΜΑ για μια χώρα.");

        // συνεχιζουμε μονο αν υπαρχουν ημερομηνιες στις επιλογες των drop down menu
        // διαφορετικα σημαινει οτι η χωρα δεν εχει αποθηκευμενα δεδομενα coviddata στη ΒΔ
        if (!(jComboBox3.getSelectedItem() != null)) {
            JOptionPane.showMessageDialog(null, "Δεν υπαρχουν ΑΥΤΑ τα δεδομενα για τη χώρα ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // αρχικη και τελικη ημερομηνια
        LocalDate startdate = LocalDate.parse(jComboBox3.getSelectedItem().toString());
        LocalDate enddate = LocalDate.parse(jComboBox4.getSelectedItem().toString());

        // βρισκουμε τη χωρα για την οποια θα αναζητησουμε τα δεδομενα
        String countryName = jComboBox8.getSelectedItem().toString();
        Country country = dbController.getCountryByName(countryName);

        // για να διατηρησουμε ημερολογιακη σειρα στους πινακες δεδομενων θα εισαγουμε τα δεδομενα
        // σε data structure που μας προσφερει η Java τα οποια αυτοματα διατηρουν μια σειρα
        SortedMap<LocalDate, Coviddata> deathsMap = new TreeMap<>();
        SortedMap<LocalDate, Coviddata> confirmedMap = new TreeMap<>();
        SortedMap<LocalDate, Coviddata> recoveredMap = new TreeMap<>();

        // πρεπει να κανουμε την ιδια διαδικασια για τους 3 διαφορετικους πινακες
        LocalDate date;
        List<Coviddata> coviddataList;

        // η επιλογη του χρηστη απο το μενου
        int choice = jComboBox1.getSelectedIndex();

        switch (choice) {
            // ολα τα ειδη μαζι
            case 0:
                // deaths
                try {
                    // αναζητουμε τα δεδομενα στη ΒΔ
                    coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 1);
                    if (!coviddataList.isEmpty()) {

                        for (Coviddata coviddata : coviddataList) {

                            date = LocalDate.parse(coviddata.getTrndate());

                            if ((date.isAfter(startdate) && date.isBefore(enddate))
                                    || date.isEqual(startdate)
                                    || date.isEqual(enddate)) {

                                deathsMap.put(date, coviddata);

                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
                }

                // recovered
                try {
                    // αναζητουμε τα δεδομενα στη ΒΔ
                    coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 2);
                    if (!coviddataList.isEmpty()) {

                        for (Coviddata coviddata : coviddataList) {

                            date = LocalDate.parse(coviddata.getTrndate());

                            if ((date.isAfter(startdate) && date.isBefore(enddate))
                                    || date.isEqual(startdate)
                                    || date.isEqual(enddate)) {

                                recoveredMap.put(date, coviddata);

                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
                }

                // confirmed
                try {
                    // αναζητουμε τα δεδομενα στη ΒΔ
                    coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 3);
                    if (!coviddataList.isEmpty()) {

                        for (Coviddata coviddata : coviddataList) {

                            date = LocalDate.parse(coviddata.getTrndate());

                            if ((date.isAfter(startdate) && date.isBefore(enddate))
                                    || date.isEqual(startdate)
                                    || date.isEqual(enddate)) {

                                confirmedMap.put(date, coviddata);

                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
                }

                HashMap<String, SortedMap<LocalDate, Coviddata>> list = new HashMap<>();
                list.put("Deaths", deathsMap);
                list.put("Confirmed", confirmedMap);
                list.put("Recovered", recoveredMap);
                LineFrame frame = new LineFrame("Covid19 Cases", list);
                break;

            // μονο θανατοι    
            case 1:
                // deaths
                try {
                    // αναζητουμε τα δεδομενα στη ΒΔ
                    coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 1);
                    if (!coviddataList.isEmpty()) {

                        for (Coviddata coviddata : coviddataList) {

                            date = LocalDate.parse(coviddata.getTrndate());

                            if ((date.isAfter(startdate) && date.isBefore(enddate))
                                    || date.isEqual(startdate)
                                    || date.isEqual(enddate)) {
                                // αν η ημερομηνια ειναι ιση ή αναμεσα στις 2 ημερομηνιες στα πεδια επιλογης
                                // αποθηκευση το coviddata στο αντιστοιχο sortedmap
                                deathsMap.put(date, coviddata);

                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Δεν υπαρχουν δεδομενα Θανατων για τη χώρα");
                    JOptionPane.showMessageDialog(null, "Δεν υπαρχουν ΑΥΤΑ τα δεδομενα για τη χώρα ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                LineFrame frame1 = new LineFrame("Death Cases", deathsMap, "Deaths");
                break;
            // μονο επιβεβαιωμενα κρουσματα    
            case 2:
                // confirmed
                try {
                    // αναζητουμε τα δεδομενα στη ΒΔ
                    coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 3);
                    if (!coviddataList.isEmpty()) {

                        for (Coviddata coviddata : coviddataList) {

                            date = LocalDate.parse(coviddata.getTrndate());

                            if ((date.isAfter(startdate) && date.isBefore(enddate))
                                    || date.isEqual(startdate)
                                    || date.isEqual(enddate)) {
                                
                                // αν η ημερομηνια ειναι ιση ή αναμεσα στις 2 ημερομηνιες στα πεδια επιλογης
                                // αποθηκευση το coviddata στο αντιστοιχο sortedmap
                                confirmedMap.put(date, coviddata);

                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Δεν υπαρχουν δεδομενα επιβεβαιωμενων κρουσματων για τη χώρα");
                    JOptionPane.showMessageDialog(null, "Δεν υπαρχουν ΑΥΤΑ τα δεδομενα για τη χώρα ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                LineFrame frame2 = new LineFrame("Confirmed Cases", confirmedMap, "Confirmed");
                break;
            // μονο ασθενεις που ανάρρωσαν    
            case 3:
                // recovered
                try {
                    // αναζητουμε τα δεδομενα στη ΒΔ
                    coviddataList = dbController.getCoviddataByCountryAndDatakind(country, 2);
                    if (!coviddataList.isEmpty()) {

                        for (Coviddata coviddata : coviddataList) {

                            date = LocalDate.parse(coviddata.getTrndate());

                            if ((date.isAfter(startdate) && date.isBefore(enddate))
                                    || date.isEqual(startdate)
                                    || date.isEqual(enddate)) {
                                // αν η ημερομηνια ειναι ιση ή αναμεσα στις 2 ημερομηνιες στα πεδια επιλογης
                                // αποθηκευση το coviddata στο αντιστοιχο sortedmap                                
                                recoveredMap.put(date, coviddata);

                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Δεν υπαρχουν δεδομενα επιβεβαιωμενων κρουσματων για τη χώρα");
                    JOptionPane.showMessageDialog(null, "Δεν υπαρχουν ΑΥΤΑ τα δεδομενα για τη χώρα ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                LineFrame frame3 = new LineFrame("Recovered Cases", recoveredMap, "Recovered");
                break;

            // ?? Σωρρευτικα δεδομενα    
            case 4:
                break;

            default:
                break;
        }
    }//GEN-LAST:event_jButton15MousePressed

    // ελεγχος αν τα πεδια στις ημερομηνιες ειναι σωστα
    // στην περιπτωση που η 2η ημερομηνια ειναι χρονικα νωριτερα απο την 1η
    // σιωπηρα χωρις σφαλμα επιτρεπουμε την υπαρξη μονο της πρωτης ημερομηνιας 
    // να εμφανιζεται στα τραπεζια που δειχνουν τα δεδομενα
    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        if (!jComboBox3.isEnabled() || !jComboBox4.isEnabled()) {
            return;
        }
        LocalDate first = LocalDate.parse(jComboBox3.getSelectedItem().toString());
        LocalDate last = LocalDate.parse(jComboBox4.getSelectedItem().toString());

        if (first.isAfter(last)) {
            filltables(first, first);
            return;
        }

        filltables(first, last);

    }//GEN-LAST:event_jComboBox3ItemStateChanged

    // ελεγχος αν τα πεδια στις ημερομηνιες ειναι σωστα
    // στην περιπτωση που η 2η ημερομηνια ειναι χρονικα νωριτερα απο την 1η
    // σιωπηρα χωρις σφαλμα επιτρεπουμε την υπαρξη μονο της πρωτης ημερομηνιας 
    // να εμφανιζεται στα τραπεζια που δειχνουν τα δεδομενα
    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        if (!jComboBox3.isEnabled() || !jComboBox4.isEnabled()) {
            return;
        }
        LocalDate first = LocalDate.parse(jComboBox3.getSelectedItem().toString());
        LocalDate last = LocalDate.parse(jComboBox4.getSelectedItem().toString());

        if (first.isAfter(last)) {
            filltables(first, first);
            return;
        }

        filltables(first, last);

    }//GEN-LAST:event_jComboBox4ItemStateChanged

    // σε περιπτωση που αλλαξζουμε στην 1η οθονη και διαγραψουμε τα δεδομενα
    // θελουμε να μην μπορει ο χρηστης να αναζητησει, η να χειριστει κουμπια
    // γενικα. οποτε ξανα εισαγουμε τις χωρες στο μενου επιλογων
    // και απενεργοποιουμε τα κουμπια
    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
        jButton12.setEnabled(false);
        jButton15.setEnabled(false);
        jButton5.setEnabled(false);
        jComboBox1.setEnabled(false);
        jComboBox3.setEnabled(false);
        jComboBox4.setEnabled(false);
        fillCountriesMenu();
    }//GEN-LAST:event_formWindowGainedFocus

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(jFrame02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jFrame02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jFrame02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jFrame02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jFrame02().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
