package it.cgmconsulting.mscategory.service;

import it.cgmconsulting.mscategory.entity.Category;
import it.cgmconsulting.mscategory.entity.CategoryPosts;
import it.cgmconsulting.mscategory.exception.ResourceNotFoundException;
import it.cgmconsulting.mscategory.payload.request.CategoryRequest;
import it.cgmconsulting.mscategory.payload.request.PostCategoryAssociationRequest;
import it.cgmconsulting.mscategory.payload.response.CategoryResponse;
import it.cgmconsulting.mscategory.repository.CategoryPostsRepository;
import it.cgmconsulting.mscategory.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryPostsRepository categoryPostsRepository;

    public ResponseEntity<?> createCategory(String categoryName) {
        categoryName = categoryName.toUpperCase().trim();
        if(categoryRepository.existsByCategoryName(categoryName))
            return new ResponseEntity("Category already present", HttpStatus.BAD_REQUEST);
        categoryRepository.save(new Category(categoryName));
        return new ResponseEntity("Category successfully created", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateCategory(CategoryRequest request) {
        String newCategoryName = request.getNewCategoryName().toUpperCase().trim();
        if(categoryRepository.existsByCategoryNameAndIdNot(newCategoryName, request.getCategoryId()))
            return new ResponseEntity("Category name already used", HttpStatus.BAD_REQUEST);
        Category category = findCategory(request.getCategoryId());
        category.setCategoryName(newCategoryName);
        category.setVisible(request.isNewVisibility());
        return new ResponseEntity<>("Category successfully updated", HttpStatus.OK);
    }

    protected Category findCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId)
        );
        return category;
    }


    public ResponseEntity<?> getAllCategories() {
        return new ResponseEntity(categoryRepository.getAllCategories(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllVisibleCategories() {
        return new ResponseEntity(categoryRepository.getAllVisibleCategories(), HttpStatus.OK);
    }

    public ResponseEntity<?> postCategoryAssociation(PostCategoryAssociationRequest request) {
        long postId = request.getPostId();
        List<Category> categories = categoryRepository.getAllCategoriesByIdsAndVisibleTrue(request.getCategoryIds());
        categoryPostsRepository.deleteCategoryPost(postId);
        for (Category cat : categories){
          categoryPostsRepository.insertCategoryPost(postId, cat.getId());
        }
        return new ResponseEntity<>("Categories updated for post " + postId , HttpStatus.OK);
    }

    public Set<String> getCategoriesByPost(long postId) {


        Set<String> categories = categoryPostsRepository.findCategoriesByPostId(postId);
        return categories;
    }

    public Set<Long> getPostsByCategory(String categoryName) {
        Set<Long> postIds = categoryPostsRepository.findByCategoryNameAndVisibleTrue(categoryName);
       return postIds;
    }

    public List<CategoryResponse> findAllCategoriesByPosts() {
        List<CategoryPosts> listCp = categoryPostsRepository.findAllCategoriesByPostId();
        List<CategoryResponse> cR = new ArrayList<>();
        Set<String> categories = new HashSet<>();
        Set<Long> postIds = listCp.stream().map(p -> p.getCategoryId().getPostId()).collect(Collectors.toSet());

        for(long postId: postIds){
            for(CategoryPosts categoryPosts : listCp){
                if(postId == categoryPosts.getCategoryId().getPostId())
                    categories.add(categoryPosts.getCategoryId().getCategory().getCategoryName());
            }
            cR.add(new CategoryResponse(postId,categories));
            categories = new HashSet<>();
        }
        return cR;
    }
}
