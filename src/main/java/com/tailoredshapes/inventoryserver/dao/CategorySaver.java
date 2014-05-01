package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.Arrays;

public class CategorySaver<T> extends Saver<Category> {

    private final DAO<Category> categoryDAO;
    private final Repository<Category, T> categoryRepository;
    private final FinderFactory<Category, String, T> fullNameFinderFactor;

    @Inject
    public CategorySaver(DAO<Category> categoryDAO,
                         Repository<Category, T> categoryRepository,
                         FinderFactory<Category, String, T> fullNameFinderFactor) {
        this.categoryDAO = categoryDAO;
        this.categoryRepository = categoryRepository;
        this.fullNameFinderFactor = fullNameFinderFactor;
    }

    @Override
    public Category saveChildren(Category object) {
        if (null == object.getName()) {
            String[] split = object.getFullname().split("\\.");

            object.setName(split[split.length - 1]);
            if (split.length > 1) {
                String[] strings = Arrays.copyOfRange(split, 0, split.length - 1);
                String parentCategory = StringUtils.join(strings, ".");
                object.setParent(categoryRepository.findBy(fullNameFinderFactor.lookFor(parentCategory)));
            }

        }

        object.setParent(upsert(object.getParent(), categoryDAO));
        return object;
    }
}
