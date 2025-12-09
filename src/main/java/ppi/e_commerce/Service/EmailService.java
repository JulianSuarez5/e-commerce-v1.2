package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:E-Commerce Pro}")
    private String appName;

    /**
     * Enviar correo de texto simple
     */
    public boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(destinatario);
            msg.setSubject(asunto);
            msg.setText(mensaje);
            
            mailSender.send(msg);
            log.info("✅ Correo enviado a: {}", destinatario);
            return true;
            
        } catch (Exception ex) {
            log.error("❌ Error al enviar correo a {}: {}", destinatario, ex.getMessage());
            return false;
        }
    }

    /**
     * Enviar correo de recuperación de contraseña
     */
    public boolean enviarCorreoRecuperacion(String destinatario, String nombre, String contrasenaTemporal) {
        String asunto = "Recuperación de Contraseña - " + appName;
        String cuerpo = """
                Hola %s,

                Has solicitado restablecer tu contraseña.
                Tu nueva contraseña temporal es: %s
                Tienes 60 minutos para cambiarla desde tu perfil.

                %s
                """.formatted(nombre, contrasenaTemporal, appName);

        return enviarCorreo(destinatario, asunto, cuerpo);
    }
}
