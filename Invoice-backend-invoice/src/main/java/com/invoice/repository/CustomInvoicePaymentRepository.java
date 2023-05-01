package com.invoice.repository;

import com.invoice.entity.InvoicePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Map;
import java.util.UUID;

@Repository
public class CustomInvoicePaymentRepository
{
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public InvoicePayment updateInvoicePayment(Map<String, Object> data, UUID id) {
        StringBuilder updateQuery = new StringBuilder();
        updateQuery.append("UPDATE InvoicePayment c SET ");
        boolean firstLoop = true;
        for (String key : data.keySet()) {
            System.out.println("Key :  " + key);


            if (!firstLoop) {
                updateQuery.append(" , ");
            }

            updateQuery.append(" c." + key + " = :" + key + "");
            firstLoop = false;
        }

        updateQuery.append(" WHERE id = '" + id + "'");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query query = entityManager
                .createQuery(updateQuery.toString());

        for (String key : data.keySet()) {
            query.setParameter(key, data.get(key));
        }

        int rowsUpdated = query.executeUpdate();

        System.out.println("");
        System.out.println("Rows updated : " + rowsUpdated);
        System.out.println("");

        entityManager.getTransaction().commit();
        InvoicePayment result = entityManager.find(InvoicePayment.class,id);
        entityManager.close();
        return result;
    }

}
