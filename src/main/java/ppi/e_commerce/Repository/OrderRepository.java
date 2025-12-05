package ppi.e_commerce.Repository;

import ppi.e_commerce.Model.Order;
import ppi.e_commerce.Model.OrderStatus;
import ppi.e_commerce.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserOrderByCreationDateDesc(User user);

    List<Order> findAllByOrderByCreationDateDesc();

    List<Order> findByStatusOrderByCreationDateDesc(OrderStatus status);

    List<Order> findTop10ByOrderByCreationDateDesc();

    Long countByUser(User user);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'COMPLETED'")
    Optional<BigDecimal> findTotalSales();

    long countByStatus(OrderStatus status);

    List<Order> findByUserAndStatus(User user, OrderStatus status);
}
