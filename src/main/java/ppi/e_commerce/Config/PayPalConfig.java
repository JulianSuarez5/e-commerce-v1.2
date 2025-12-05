package ppi.e_commerce.Config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class PayPalConfig {

    @Value("${app.paypal.client-id}")
    private String clientId;

    @Value("${app.paypal.client-secret}")
    private String clientSecret;

    @Value("${app.paypal.mode}")
    private String mode;

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        log.info("Configurando PayPal APIContext en modo: {}", mode);
        log.info("Usando PayPal Client ID: ...{}", clientId.substring(Math.max(0, clientId.length() - 6)));

        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);

        APIContext context = new APIContext(clientId, clientSecret, mode);
        log.info("APIContext de PayPal creado exitosamente.");

        return context;
    }
}
