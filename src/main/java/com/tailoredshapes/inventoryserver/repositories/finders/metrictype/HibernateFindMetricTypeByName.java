package com.tailoredshapes.inventoryserver.repositories.finders.metrictype;

import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateFindMetricTypeByName implements FinderFactory<MetricType, String, EntityManager>, Finder<MetricType, EntityManager> {
    private String name;

    @Override
    public MetricType find(EntityManager manager) {
        Query cq = manager.createQuery("select m from MetricType m where m.name = :name")
                          .setParameter("name", name);

        MetricType type;
        try {
            type = (MetricType) cq.getSingleResult();
        } catch (Exception e) {
            type = new MetricType().setName(name);
        }
        return type;
    }

    @Override
    public Finder<MetricType, EntityManager> lookFor(String strings) {
        this.name = strings;
        return this;
    }
}

