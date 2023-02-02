package com.example.demo;

import org.arquillian.ape.api.UsingDataSet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.wildfly.common.Assert;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.TypedQuery;

@ExtendWith(ArquillianExtension.class)
@Transactional(TransactionMode.COMMIT)
public class DemoIT {

    @PersistenceUnit(name = "default", unitName = "default")
    private EntityManagerFactory emf;

    @Deployment
    @TargetsContainer("jboss")
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClasses(Demo.class).addAsResource("META-INF/beans.xml")
            .addAsResource("META-INF/persistence.xml").addAsManifestResource("META-INF/TEST-MANIFEST.MF",
                "MANIFEST.MF");
    }

    @Test
    @UsingDataSet("arquillianTest.xml")
    public void should_populate_and_rollback_data() {
        final EntityManager em = emf.createEntityManager();
        final TypedQuery<Demo> query = em.createQuery("select d from Demo d", Demo.class);
        List<Demo> list = query.getResultList();
        Assert.assertTrue(list.size() == 1);

        Demo demo = new Demo();
        demo.setId(555L);
        demo.setName("user data");
        em.persist(demo);

        list = query.getResultList();
        Assert.assertTrue(list.size() == 2);
    }
}
