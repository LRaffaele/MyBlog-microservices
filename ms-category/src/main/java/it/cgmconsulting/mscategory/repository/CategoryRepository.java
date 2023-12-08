package it.cgmconsulting.mscategory.repository;

import it.cgmconsulting.mscategory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryNameAndIdNot(String newCategoryName, long categoryId);



    @Query(value = "SELECT cat FROM Category cat ORDER BY cat.categoryName")
    List<Category> getAllCategories();

    @Query(value = "SELECT * FROM category cat WHERE cat.visible=true ORDER BY cat.category_name", nativeQuery = true)
    List<Category> getAllVisibleCategories();

    @Query(value="SELECT cat FROM Category cat WHERE cat.id IN (:categoryIds) AND cat.visible = true")
    List<Category> getAllCategoriesByIdsAndVisibleTrue(@Param("categoryIds") Set<Long> categoryIds);


}
