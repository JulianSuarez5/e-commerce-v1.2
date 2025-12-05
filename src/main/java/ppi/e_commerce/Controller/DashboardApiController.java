package ppi.e_commerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ppi.e_commerce.Dto.DashboardMetricsDto;
import ppi.e_commerce.Service.DashboardService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardApiController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardApiController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDto> getDashboardMetrics() {
        DashboardMetricsDto metrics = dashboardService.getDashboardMetrics();
        return ResponseEntity.ok(metrics);
    }
}
