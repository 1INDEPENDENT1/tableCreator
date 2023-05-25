import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PurchaseList> purchaseListQuery = builder.createQuery(PurchaseList.class);
        Root<PurchaseList> purchaseListRoot = purchaseListQuery.from(PurchaseList.class);
        purchaseListQuery.select(purchaseListRoot);
        List<PurchaseList> purchaseLists = session.createQuery(purchaseListQuery).getResultList();
        for (PurchaseList purchaseList : purchaseLists) {
            LinkedPurchaseListKey key = new LinkedPurchaseListKey();
            key.setCourseName(purchaseList.getCourseName());
            key.setStudentName(purchaseList.getStudentName());
            LinkedPurchaseList linkedPurchaseList = new LinkedPurchaseList();
            linkedPurchaseList.setId(key);
            linkedPurchaseList.setCourseName(purchaseList.getCourseName());
            linkedPurchaseList.setStudentName(purchaseList.getStudentName());
            session.save(linkedPurchaseList);
        }
        try {
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}