package com.tailoredshapes.inventoryserver.dao;

import java.util.Arrays;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.Looker;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import static com.tailoredshapes.underbar.UnderString.join;


public class CategorySaver<T> implements Saver<Category> {


  private Repository.FindBy<Category, T> findBy;
  private Looker<String, Category, T> fullNameFinderFactor;

  public CategorySaver(Repository.FindBy<Category, T> findBy,
                       Looker<String, Category, T> fullNameFinderFactor) {
    this.findBy = findBy;
    this.fullNameFinderFactor = fullNameFinderFactor;
  }

  @Override
  public Category saveChildren(DAO<Category> categoryDAO, Category object) {
    if (null == object.getName()) {
      String[] split = object.getFullname().split("\\.");

      object.setName(split[split.length - 1]);
      if (split.length > 1) {
        String[] strings = Arrays.copyOfRange(split, 0, split.length - 1);
        String parentCategory = join(strings, ".");
        object.setParent(findBy.findBy(fullNameFinderFactor.lookFor(parentCategory)));
      }

    }

    object.setParent(categoryDAO.upsert(object.getParent()));
    return object;
  }
}
