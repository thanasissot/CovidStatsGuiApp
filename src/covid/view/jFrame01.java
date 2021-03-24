package covid.view;

import covid.controller.CountryJpaController;
import covid.controller.CoviddataJpaController;
import covid.controller.DBController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import covid.model.Country;
import covid.model.Coviddata;
import covid.service.DataMap;
import javax.swing.JFrame;

public class jFrame01 extends javax.swing.JFrame {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("CovidDBPU1");
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    CountryJpaController countryController = new CountryJpaController(emf);
    CoviddataJpaController coviddataController = new CoviddataJpaController(emf);
    DBController dbController = new DBController();

    public jFrame01() {
        initComponents();
        // προστιθεται ετσι ωστε οταν κλεινει το παραθυρο να μην τερματιζει
        // ολο το app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        datakindCombobox = new javax.swing.JComboBox<>();
        insertCountries = new javax.swing.JButton();
        insertData = new javax.swing.JButton();
        deleteCountries = new javax.swing.JButton();
        deleteData = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel2.setText("Επιλογή Είδους Δεδομένων :");

        datakindCombobox.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        datakindCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Θάνατοι", "Ασθενείς που έχουν ανακάμψει", "Επιβεβαιωμένα κρούσματα" }));

        insertCountries.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        insertCountries.setText("Εισαγωγή Χωρών");
        insertCountries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                insertCountriesMousePressed(evt);
            }
        });

        insertData.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        insertData.setText("Εισαγωγή Δεδομένων");
        insertData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                insertDataMousePressed(evt);
            }
        });

        deleteCountries.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        deleteCountries.setText("Διαγραφή Χωρών");
        deleteCountries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                deleteCountriesMousePressed(evt);
            }
        });

        deleteData.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        deleteData.setText("Διαγραφή Δεδομένων");
        deleteData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                deleteDataMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(datakindCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(insertCountries, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(insertData, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(deleteCountries, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(178, 178, 178))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(deleteData, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datakindCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(insertCountries, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(insertData, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteCountries, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteData, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // παρακατω παρατιθονται τα 4 event handler methods το καθενα
    // υπευθυνο για το πατημα του αντιστοιχου κουμπιου
    // μεθοδος για την εισαγωγη χωρων στη ΒΔ
    private void insertCountriesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertCountriesMousePressed
        System.out.println("Πατηθηκε η Εισαγωγη Χωρών");
        // βρισκουμε την επιλογη του χρηστη απο το dropdown menu
        int datakind = datakindCombobox.getSelectedIndex();
        // και την μορφοποιουμε στο καταλληλο string αναλογα με το ειδος
        // για το οποιο θα γινει η κληση στο Rest API
        // χρησιμοποιειται για το httpRequest
        // η επιλογη γινεται με το switch
        String datatype = "";
        switch (datakind) {
            case 0:
                datatype = "deaths";
                break;
            case 1:
                datatype = "recovered";
                break;
            case 2:
                datatype = "confirmed";
                break;
            default:
                break;
        }

        try {
            // Map με τα δεδομενα των χωρων
            HashMap<String, Country> map = DataMap.countriesFromJson(datatype);

            for (Map.Entry<String, Country> entry : map.entrySet()) {
                // ψαχνουμε αν η χωρα ειναι αποθηκευμενη στη ΒΔ
                try {
                    Country country = dbController.getCountryByName(entry.getKey());
                    System.out.println("Country " + country.getName() + " already exists in DB");
                } catch (Exception ex1) {
                    // αν δεν ειναι την δημιουργουμε
                    System.out.println("creating country");
                    countryController.create(entry.getValue());
                    continue;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        // εμφανιση επιτυχους αποθηκευσης
        JOptionPane.showMessageDialog(null, "Οι χώρες αποθηκευτηκαν στην ΒΔ ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_insertCountriesMousePressed

    // μεθοδος για την εισαγωγη δεδομενων στη ΒΔ
    private void insertDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insertDataMousePressed
        System.out.println("Πατηθηκε η Εισαγωγη Δεδομενων");
        // βρισκουμε την επιλογη του χρηστη απο το dropdown menu
        int datakind = datakindCombobox.getSelectedIndex();

        // και την μορφοποιουμε στο καταλληλο string αναλογα με το ειδος
        // για το οποιο θα γινει η κληση στο Rest API
        // χρησιμοποιειται για το httpRequest
        // η επιλογη γινεται με το switch       
        String datatype = "";
        switch (datakind) {
            case 0:
                datatype = "deaths";
                break;
            case 1:
                datatype = "recovered";
                break;
            case 2:
                datatype = "confirmed";
                break;
            default:
                break;
        }

        try {
            HashMap<String, ArrayList<Coviddata>> map = DataMap.coviddataFromJson(datatype);

            for (String countryName : map.keySet()) {
                // ψαχνουμε αν η χωρα ειναι αποθηκευμενη στη ΒΔ
                try {
                    // η χωρα υπάρχει
                    Country country = dbController.getCountryByName(countryName);

                    for (Coviddata coviddata : map.get(countryName)) {
                        coviddata.setCountry(country);
                        try {
                            coviddataController.create(coviddata);
                        } catch (Exception ex3) {

                            // Τα δεδομενα για τη χωρα ηταν ηδη αποθηκευμενα
                            // δεν προχωρουμε σε καμια ενεργεια
                        }
                    }

                } catch (Exception ex1) {

                    System.out.println("Τα δεδομενα για τη χωρα " + countryName + " δεν αποθηκευτηκαν"
                            + ". Η χωρα δεν βρεθηκε αποθηκευμενη στη βαση δεδομένων");

                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        JOptionPane.showMessageDialog(null, "Τα δεδομένα Coviddata " + datatype
                + " αποθηκευτηκαν στην βάση δεδομένων ", "Ενημέρωση", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_insertDataMousePressed

    // διαγραφη των χωρων απο τη ΒΔ
    private void deleteCountriesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteCountriesMousePressed
        // TODO add your handling code here:
        System.out.println("Πατηθηκε η Διαγραφη Χωρων");
        //ερώτημα επαλήθευσης με επιβεβαίωση από το χρήστη.
        int choice = JOptionPane.showConfirmDialog(null, "Διαγραφει ολων των χωρων από τη βάση δεδομένων\n"
                + "Είστε βέβαιοι;",
                "Διαγραφή Δεδομένων", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {

                transaction.begin();

                // αν υπαρχουν αποθηκευμενα δεδομενα coviddata δεν μπορουμε να διαγραψουμε τις χώρες
                // ελεγχος υπαρξης δεδομενων
                List<Coviddata> list = dbController.getAllCoviddata();
                if (list.isEmpty()) {
                    // διαγραφη των χωρών
                    em.createQuery("DELETE FROM Country").executeUpdate();
                    // επανερξαξη του "μετρητη" για τα id των χωρών
                    em.createNativeQuery("ALTER TABLE COUNTRY ALTER COLUMN COUNTRY RESTART WITH 1").executeUpdate();

                    transaction.commit();
                    JOptionPane.showMessageDialog(null, "Οι χώρες διαγράφηκαν από την ΒΔ", "Διαγραφή Χωρών",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Οι χώρες δεν διαγράφηκαν από την ΒΔ\n"
                            + "Διαγράψτε πρώτα τα δεδομένα", "Διαγραφή Χωρών", JOptionPane.INFORMATION_MESSAGE);
                    transaction.commit();
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }//GEN-LAST:event_deleteCountriesMousePressed

    // διαγραφη των δεδομενων απο τη ΒΔ
    private void deleteDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteDataMousePressed
        // TODO add your handling code here:
        System.out.println("Πατηθηκε η Διαγραφη Δεδομένων");
        int choice = JOptionPane.showConfirmDialog(null, "Tο σύστημα θα διαγράψει από τη βάση δεδομένων τα δεδομένα \n"
                + "(θάνατοι, επιβεβαιωμένα κρούσματα, ασθενείς που έχουν ανακάμψει).\n Είστε βέβαιοι;",
                "Διαγραφή Δεδομένων", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {

                transaction.begin();
                // διαγραφη των δεδομενων
                em.createQuery("DELETE FROM Coviddata").executeUpdate();
                // επανεναρξη "μετρητη" για την δημιουργια των ID των coviddata
                em.createNativeQuery("ALTER TABLE COVIDDATA ALTER COLUMN COVIDDATA RESTART WITH 1").executeUpdate();
                transaction.commit();
                JOptionPane.showMessageDialog(null, "Τα δεδομένα διαγράφηκαν από την ΒΔ", "Διαγραφή Δεδομένων",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                transaction.commit();
                System.out.println(ex);
            }
        }
    }//GEN-LAST:event_deleteDataMousePressed

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
            java.util.logging.Logger.getLogger(jFrame01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jFrame01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jFrame01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jFrame01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jFrame01().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> datakindCombobox;
    private javax.swing.JButton deleteCountries;
    private javax.swing.JButton deleteData;
    private javax.swing.JButton insertCountries;
    private javax.swing.JButton insertData;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
