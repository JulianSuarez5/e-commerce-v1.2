package ppi.e_commerce.Specifications;

import org.springframework.data.jpa.domain.Specification;
import ppi.e_commerce.Model.Product;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> filterBy(
            Integer categoryId,
            Integer brandId,
            Double minPrice,
            Double maxPrice,
            String query
    ) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter by active products
            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (query != null && !query.isBlank()) {
                String queryLower = "%" + query.toLowerCase() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), queryLower);
                Predicate descLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), queryLower);
                predicates.add(criteriaBuilder.or(nameLike, descLike));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
