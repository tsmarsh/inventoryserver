package com.tailoredshapes.inventoryserver.repositories.finders.metrictype;

import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateFindByName implements FinderFactory<MetricType, String, EntityManager>, Finder<MetricType, EntityManager> {
    private String name;

    @Override
    public MetricType find(EntityManager manager) {
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

    @Override
    public Finder<MetricType, EntityManager> lookFor(String... strings) {
        this.name = strings[0];
        return this;
    }
}

