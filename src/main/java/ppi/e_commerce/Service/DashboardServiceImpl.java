package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppi.e_commerce.Dto.DashboardMetricsDto;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Mapper.ProductMapper;
import ppi.e_commerce.Model.OrderStatus;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.ProductRepository;
import ppi.e_commerce.Repository.UserRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public DashboardServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository, ProductMapper productMapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public DashboardMetricsDto getDashboardMetrics() {
        long totalUsers = userRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        BigDecimal totalSales = orderRepository.findTotalSales().orElse(BigDecimal.ZERO);
        List<ProductDto> topSellingProducts = productRepository.findTop5ByOrderBySalesDesc().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return new DashboardMetricsDto(totalUsers, pendingOrders, totalSales, topSellingProducts);
    }
}
