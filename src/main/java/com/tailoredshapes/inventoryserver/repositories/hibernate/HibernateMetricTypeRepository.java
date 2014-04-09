package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateMetricTypeRepository implements MetricTypeRepository {

    private final EntityManager manager;

    @Inject
    public HibernateMetricTypeRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public MetricType findByName(String name) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<MetricType> cq = cb.createQuery(MetricType.class);
        Root<MetricType> root = cq.from(MetricType.class);
        cq.where(cb.equal(root.get("name"), name));

        MetricType type;
        try {
            type = manager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            type = new MetricType().setName(name);
        }
        return type;
    }
}

