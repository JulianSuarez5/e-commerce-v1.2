package ppi.e_commerce.Controller.Api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ppi.e_commerce.Dto.CreateSellerRequest;
import ppi.e_commerce.Dto.SellerDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Mapper.SellerMapper;
import ppi.e_commerce.Model.Seller;
import ppi.e_commerce.Model.User;
import ppi.e_commerce.Repository.UserRepository;
import ppi.e_commerce.Service.SellerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sellers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SellerApiController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerMapper sellerMapper;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createSeller(
            @Valid @RequestBody CreateSellerRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            Seller seller = new Seller();
            seller.setBusinessName(request.getBusinessName());
            seller.setDescription(request.getDescription());
            seller.setWebsite(request.getWebsite());
            seller.setTaxId(request.getTaxId());
            seller.setPhone(request.getPhone());
            seller.setAddress(request.getAddress());
            seller.setCity(request.getCity());
            seller.setState(request.getState());
            seller.setZipCode(request.getZipCode());
            seller.setCountry(request.getCountry());

            Seller createdSeller = sellerService.createSeller(user.getId(), seller);
            SellerDto dto = sellerMapper.toDto(createdSeller);
            dto.setProductCount(0L);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear vendedor: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerDto> getSellerById(@PathVariable Integer id) {
        Optional<Seller> sellerOpt = sellerService.findById(id);

        if (sellerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SellerDto dto = sellerMapper.toDto(sellerOpt.get());
        Long productCount = sellerService.countProductsBySeller(id);
        dto.setProductCount(productCount);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SellerDto> getSellerByUserId(@PathVariable Integer userId) {
        Optional<Seller> sellerOpt = sellerService.findByUserId(userId);

        if (sellerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SellerDto dto = sellerMapper.toDto(sellerOpt.get());
        Long productCount = sellerService.countProductsBySeller(sellerOpt.get().getId());
        dto.setProductCount(productCount);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<?> getSellerProducts(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (!sellerService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // TODO: Implementar paginación
        List<ppi.e_commerce.Model.Product> products = sellerService.findById(id)
                .map(Seller::getProducts)
                .orElse(List.of());

        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<List<SellerDto>> getAllSellers(
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) Boolean active) {

        List<Seller> sellers;
        if (Boolean.TRUE.equals(verified)) {
            sellers = sellerService.findAllVerified();
        } else if (Boolean.TRUE.equals(active)) {
            sellers = sellerService.findAllActive();
        } else {
            sellers = sellerService.findAllActive();
        }

        List<SellerDto> dtos = sellers.stream()
                .map(seller -> {
                    SellerDto dto = sellerMapper.toDto(seller);
                    Long productCount = sellerService.countProductsBySeller(seller.getId());
                    dto.setProductCount(productCount);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateSeller(
            @PathVariable Integer id,
            @Valid @RequestBody CreateSellerRequest request,
            Authentication authentication) {
        try {
            Optional<Seller> sellerOpt = sellerService.findById(id);
            if (sellerOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Seller seller = sellerOpt.get();
            String username = authentication.getName();

            // Verificar que el usuario es el dueño o es admin
            if (!seller.getUser().getUsername().equals(username)
                    && !authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            seller.setBusinessName(request.getBusinessName());
            seller.setDescription(request.getDescription());
            seller.setWebsite(request.getWebsite());
            seller.setTaxId(request.getTaxId());
            seller.setPhone(request.getPhone());
            seller.setAddress(request.getAddress());
            seller.setCity(request.getCity());
            seller.setState(request.getState());
            seller.setZipCode(request.getZipCode());
            seller.setCountry(request.getCountry());

            Seller updatedSeller = sellerService.updateSeller(seller);
            SellerDto dto = sellerMapper.toDto(updatedSeller);
            dto.setProductCount(sellerService.countProductsBySeller(id));

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar vendedor: " + e.getMessage());
        }
    }
}
