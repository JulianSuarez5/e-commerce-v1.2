package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> findActiveCategories();
    CategoryDto findById(Integer id);
}
