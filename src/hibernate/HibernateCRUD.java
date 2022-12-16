package hibernate;

import entities.User;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateCRUD {
    public static <T>void createObject(T t) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(t);
        entityManager.getTransaction().commit();
    }
    public static <T>void updateObject(T t) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(t);
        entityManager.getTransaction().commit();
    }
    public static <T>void deleteObject(T t) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Object managed = entityManager.merge(t);
        entityManager.remove(managed);
        entityTransaction.commit();
        entityManager.close();
    }
    public static<T> List getAllEntity(T t){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
        query.select(query.from(t.getClass()));
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }
    public static User getUserByLoginData(String login, String password){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery <User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(criteriaBuilder.and(criteriaBuilder.like(root.get("login"), login), criteriaBuilder.like(root.get("password"), password)));
        Query q;
        try {
            q = entityManager.createQuery(query);
            return (User) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
    public static List<User> getAllUsers(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
        query.select(query.from(User.class));
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }

    public static User getUserByLogin(String login){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery <User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(criteriaBuilder.like(root.get("login"), login));
        Query q;
        try {
            q = entityManager.createQuery(query);
            return (User) q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
    public static User getUserById(int id){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = null;
        try {
            entityManager.getTransaction().begin();
            user = entityManager.find(User.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such user by given Id");
        }
        return user;
    }
}
