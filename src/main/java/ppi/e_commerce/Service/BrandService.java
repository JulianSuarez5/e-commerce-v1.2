package ppi.e_commerce.Service;

import ppi.e_commerce.Dto.BrandDto;
import java.util.List;

public interface BrandService {
    List<BrandDto> findActiveBrands();
    BrandDto findById(Integer id);
}
