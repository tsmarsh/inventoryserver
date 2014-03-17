package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HibernateMetricTypeRepository implements MetricTypeRepository {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateMetricTypeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MetricType findByName(String name) {
        Session currentSession = sessionFactory.getCurrentSession();
        List<MetricType> metrics = currentSession.createCriteria(MetricType.class)
                .add(Restrictions.eq("name", name))
                .list();

        if (metrics.isEmpty()) {
            return new MetricType().setName(name);
        }

        return metrics.get(0);
    }
}

