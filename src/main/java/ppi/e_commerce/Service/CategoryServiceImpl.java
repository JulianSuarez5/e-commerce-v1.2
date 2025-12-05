package ppi.e_commerce.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.CategoryDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.CategoryMapper;
import ppi.e_commerce.Repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findActiveCategories() {
        log.info("Fetching all active categories.");
        return categoryRepository.findActiveCategoriesOrderedByName().stream()
                .map(category -> {
                    CategoryDto dto = categoryMapper.toDto(category);
                    dto.setProductCount(categoryRepository.countProductsByCategory(category));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findById(Integer id) {
        log.info("Fetching category by ID: {}", id);
        return categoryRepository.findById(id)
                .map(category -> {
                    CategoryDto dto = categoryMapper.toDto(category);
                    dto.setProductCount(categoryRepository.countProductsByCategory(category));
                    return dto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
