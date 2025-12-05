package ppi.e_commerce.Dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardMetricsDto {

    private long totalUsers;
    private long pendingOrders;
    private BigDecimal totalSales;
    private List<ProductDto> topSellingProducts;

    public DashboardMetricsDto(long totalUsers, long pendingOrders, BigDecimal totalSales, List<ProductDto> topSellingProducts) {
        this.totalUsers = totalUsers;
        this.pendingOrders = pendingOrders;
        this.totalSales = totalSales;
        this.topSellingProducts = topSellingProducts;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public List<ProductDto> getTopSellingProducts() {
        return topSellingProducts;
    }

    public void setTopSellingProducts(List<ProductDto> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
    }
}
