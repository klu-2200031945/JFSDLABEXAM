package com.klef.jfsdexam;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.*;
import java.util.List;

public class ClientDemo {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();

        try (Session session = factory.openSession()) {
            // Insert records
            Transaction tx = session.beginTransaction();

            Customer customer1 = new Customer();
            customer1.setName("RAVI");
            customer1.setEmail("RAVI@example.com");
            customer1.setAge(19);
            customer1.setLocation("VIJAYAWADA");

            Customer customer2 = new Customer();
            customer2.setName("SURYA");
            customer2.setEmail("SURYA@example.com");
            customer2.setAge(20);
            customer2.setLocation("VIZAG");

            session.save(customer1);
            session.save(customer2);

            tx.commit();

            // Apply Restrictions using CriteriaBuilder
            System.out.println("=== Customers in New York ===");
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);
            query.select(root).where(cb.equal(root.get("location"), "New York"));

            List<Customer> customers = session.createQuery(query).getResultList();
            customers.forEach(c -> System.out.println(c.getName() + " - " + c.getEmail()));

            System.out.println("=== Customers older than 25 ===");
            query = cb.createQuery(Customer.class);
            query.select(root).where(cb.gt(root.get("age"), 25));

            customers = session.createQuery(query).getResultList();
            customers.forEach(c -> System.out.println(c.getName() + " - " + c.getAge()));
        } finally {
            factory.close();
        }
    }
}
