package ppi.e_commerce.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ppi.e_commerce.Dto.CreateSellerRequest;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Dto.SellerDto;

import java.util.List;

public interface SellerService {

    SellerDto createSeller(String username, CreateSellerRequest request);

    SellerDto updateSeller(Integer sellerId, String username, CreateSellerRequest request);

    List<SellerDto> findSellers(boolean active, Boolean verified);

    SellerDto findSellerById(Integer id);

    SellerDto findSellerByUserId(Integer userId);

    Page<ProductDto> findSellerProducts(Integer sellerId, Pageable pageable);

    void deleteSeller(Integer sellerId);

}
