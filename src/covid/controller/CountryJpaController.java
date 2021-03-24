package covid.controller;

import covid.model.Country;
import covid.model.Coviddata;
import covid.exceptions.IllegalOrphanException;
import covid.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CountryJpaController implements Serializable {

    public CountryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Country country) {
        if (country.getCoviddataCollection() == null) {
            country.setCoviddataCollection(new ArrayList<Coviddata>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Coviddata> attachedCoviddataCollection = new ArrayList<Coviddata>();
            for (Coviddata coviddataCollectionCoviddataToAttach : country.getCoviddataCollection()) {
                coviddataCollectionCoviddataToAttach = em.getReference(coviddataCollectionCoviddataToAttach.getClass(), coviddataCollectionCoviddataToAttach.getCoviddata());
                attachedCoviddataCollection.add(coviddataCollectionCoviddataToAttach);
            }
            country.setCoviddataCollection(attachedCoviddataCollection);
            em.persist(country);
            for (Coviddata coviddataCollectionCoviddata : country.getCoviddataCollection()) {
                Country oldCountryOfCoviddataCollectionCoviddata = coviddataCollectionCoviddata.getCountry();
                coviddataCollectionCoviddata.setCountry(country);
                coviddataCollectionCoviddata = em.merge(coviddataCollectionCoviddata);
                if (oldCountryOfCoviddataCollectionCoviddata != null) {
                    oldCountryOfCoviddataCollectionCoviddata.getCoviddataCollection().remove(coviddataCollectionCoviddata);
                    oldCountryOfCoviddataCollectionCoviddata = em.merge(oldCountryOfCoviddataCollectionCoviddata);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Country country) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country persistentCountry = em.find(Country.class, country.getCountry());
            Collection<Coviddata> coviddataCollectionOld = persistentCountry.getCoviddataCollection();
            Collection<Coviddata> coviddataCollectionNew = country.getCoviddataCollection();
            List<String> illegalOrphanMessages = null;
            for (Coviddata coviddataCollectionOldCoviddata : coviddataCollectionOld) {
                if (!coviddataCollectionNew.contains(coviddataCollectionOldCoviddata)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Coviddata " + coviddataCollectionOldCoviddata + " since its country field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Coviddata> attachedCoviddataCollectionNew = new ArrayList<Coviddata>();
            for (Coviddata coviddataCollectionNewCoviddataToAttach : coviddataCollectionNew) {
                coviddataCollectionNewCoviddataToAttach = em.getReference(coviddataCollectionNewCoviddataToAttach.getClass(), coviddataCollectionNewCoviddataToAttach.getCoviddata());
                attachedCoviddataCollectionNew.add(coviddataCollectionNewCoviddataToAttach);
            }
            coviddataCollectionNew = attachedCoviddataCollectionNew;
            country.setCoviddataCollection(coviddataCollectionNew);
            country = em.merge(country);
            for (Coviddata coviddataCollectionNewCoviddata : coviddataCollectionNew) {
                if (!coviddataCollectionOld.contains(coviddataCollectionNewCoviddata)) {
                    Country oldCountryOfCoviddataCollectionNewCoviddata = coviddataCollectionNewCoviddata.getCountry();
                    coviddataCollectionNewCoviddata.setCountry(country);
                    coviddataCollectionNewCoviddata = em.merge(coviddataCollectionNewCoviddata);
                    if (oldCountryOfCoviddataCollectionNewCoviddata != null && !oldCountryOfCoviddataCollectionNewCoviddata.equals(country)) {
                        oldCountryOfCoviddataCollectionNewCoviddata.getCoviddataCollection().remove(coviddataCollectionNewCoviddata);
                        oldCountryOfCoviddataCollectionNewCoviddata = em.merge(oldCountryOfCoviddataCollectionNewCoviddata);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = country.getCountry();
                if (findCountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country country;
            try {
                country = em.getReference(Country.class, id);
                country.getCountry();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Coviddata> coviddataCollectionOrphanCheck = country.getCoviddataCollection();
            for (Coviddata coviddataCollectionOrphanCheckCoviddata : coviddataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Country (" + country + ") cannot be destroyed since the Coviddata " + coviddataCollectionOrphanCheckCoviddata + " in its coviddataCollection field has a non-nullable country field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(country);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Country> findCountryEntities() {
        return findCountryEntities(true, -1, -1);
    }

    public List<Country> findCountryEntities(int maxResults, int firstResult) {
        return findCountryEntities(false, maxResults, firstResult);
    }

    private List<Country> findCountryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Country.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Country findCountry(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Country.class, id);
        } finally {
            em.close();
        }
    }

    public int getCountryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Country> rt = cq.from(Country.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
