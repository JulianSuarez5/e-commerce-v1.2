package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppi.e_commerce.Dto.BrandDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.BrandMapper;
import ppi.e_commerce.Repository.BrandRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandDto> findActiveBrands() {
        return brandRepository.findActiveBrandsOrderedByName().stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDto findById(Integer id) {
        return brandRepository.findById(id)
                .map(brandMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
    }
}
