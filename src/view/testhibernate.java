package view;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class testhibernate {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LogisticsSystem");
    }
}