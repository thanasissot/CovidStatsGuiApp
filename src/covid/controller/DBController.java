package covid.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import covid.model.Country;
import covid.model.Coviddata;

public class DBController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("CovidDBPU1");
    private EntityManager em;

    Country c = new Country();
    Coviddata cd = new Coviddata();

    //constructor
    public DBController() {
        //αν δεν υπάρχει ήδη entity manager, τον κατασκευάζει
        try {
            if (em == null) {
                em = emf.createEntityManager();
            }
        } catch (Exception e) {
            System.err.println("Error! No Persistence Unit!");
        }
    }

    //μέθοδος για ανάκτηση του ενεργού Entity Manager
    public EntityManager getEM() {
        return em;
    }

    // μέθοδοι που περιλαμβάνουν queries για ευκολία χρησιμοποίησης
    // σε καθε μεθοδο περιλαμβανουμε ενα τροπο αναηζητησης δεδομενων απο τη ΒΔ
    // ο οποιος μας διευκολυνει σε καποιες συγκεκριμενες λειτουργιες
    // αντι να χρησιμοποιουμε generic μια αναζητηση για ολες και να ξεδιαλυνουμε
    // τι δεδομενα μας ενδιαφερουν. οι μεθοδοι ειναι self explanatory απο τον τιτλο
    // και ακολουθουν την συμβαση ενος REST API. 
    // η παραμετρος που εισαγουμε στη μεθοδου createQuery() εμφανιζει ακριβως
    // την αναζητηση στη ΒΔ η οποια γινεται μεσω του αντιστοιχου EntityManager
    // μια λειτουργικοτητα που προσφερει αυτοματα το JPA και αρα δεν χρειαζεται
    // παραπανω επεξηγηση.
    // σε αυτο το σημειο να αναφερουμε οτι βοηθηκαμε απο την κλάση DBController 
    // μιας εκ των 3 ΤΟΠ περσινων εργασιων, η οποια μας βοηθησε στη δομη των μεθοδων
    // οπου επειτα εισαγαμε τις συγκεκριμενες αναζητησεις και λειτουργιες που ζητουσαμε
    // για το δικο μας project
    public Country getCountryByName(String countryName) {
        Query query = em.createQuery("SELECT c FROM Country c WHERE c.name = :name");
        query.setParameter("name", countryName);
        return (Country) query.getSingleResult();
    }

    public List<Country> getAllCountries() {
        TypedQuery<Country> query = em.createNamedQuery("Country.findAll", Country.class);
        List<Country> results = query.getResultList();
        return results;
    }

    public List<String> getAllCountries(Boolean sort) {
        TypedQuery<Country> query = em.createNamedQuery("Country.findAll", Country.class);
        List<Country> results = query.getResultList();
        ArrayList<String> countries = new ArrayList<>();
        for (Country country : results) {
            countries.add(country.getName());
        }
        Collections.sort(countries);
        return countries;
    }

    public List<Coviddata> getCoviddataByCountry(Country country) {
        Query query = em.createQuery("SELECT c FROM Coviddata c WHERE c.country = :country");
        query.setParameter("country", country);
        return query.getResultList();
    }
    
    public List<Coviddata> getCoviddataByCountryAndDatakind(Country country, int datakind) {
        Query query = em.createQuery("SELECT c FROM Coviddata c WHERE c.country = :country AND c.datakind = :datakind");
        query.setParameter("country", country);
        query.setParameter("datakind", datakind);
        return query.getResultList();
    }

    public Coviddata getCoviddataByDate(Country country, String trndate, int datakind) {
        Query query = em.createQuery("SELECT c FROM Coviddata c WHERE c.country = :country AND c.trndate = :trndate AND c.datakind = :datakind");
        query.setParameter("country", country);
        query.setParameter("trndate", trndate);
        query.setParameter("datakind", datakind);
        return (Coviddata) query.getSingleResult();
    }

    public List<Coviddata> getAllCoviddata() {
        Query query = em.createQuery("SELECT c FROM Coviddata c");
        return query.getResultList();
    }

    public void deleteCountryByID(Country country) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Country c WHERE c.country = " + country.getCountry()).executeUpdate();
        em.getTransaction().commit();
    }

    public void deleteCovviddataByCountry(Country country) {
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM Coviddata c WHERE c.country = :country");
        query.setParameter("country", country);
        query.executeUpdate();
        em.getTransaction().commit();
    }

}
