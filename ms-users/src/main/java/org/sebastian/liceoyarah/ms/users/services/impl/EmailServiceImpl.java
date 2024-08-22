package org.sebastian.liceoyarah.ms.users.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.sebastian.liceoyarah.ms.users.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Year;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Autowired
    public EmailServiceImpl(
            JavaMailSender mailSender
    ){
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String[] toUser, String subject, String username, String password) {

        try {
            //No puedo usar SimpleMailMessage porque ese solo sería texto plano y yo enviaré HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(emailUsername);
            helper.setTo(toUser);
            helper.setSubject(subject);

            // Para generar un diseño más elaborado para el email
            String content = buildEmailContent(username, password);
            helper.setText(content, true); // El segundo parámetro indica que el contenido es HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace(); // Manejar excepción de manera adecuada en tu aplicación
        }

    }

    @Override
    public void sendMailWithFile(String[] toUser, String subject, String message, File file) {

        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(System.getenv("EMAIL_USERNAME"));
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            mimeMessageHelper.addAttachment(file.getName(), file);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public String buildEmailContent(String username, String password) {

        int currentYear = Year.now().getValue(); // Obtener el año actual

        return "<!DOCTYPE html>"
                + "<html lang='es'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; }"
                + ".container { width: 80%; max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px; }"
                + ".header { background-color: #007bff; color: white; padding: 20px; border-radius: 10px 10px 0 0; text-align: center; }"
                + ".header h1 { margin: 0; font-size: 1.5em; }"
                + ".header img { height: 50px; width: auto; margin-top: 10px; }"
                + ".content { padding: 15px; }"
                + ".footer { text-align: center; margin-top: 20px; color: #aaa; }"
                + ".table { width: 100%; border-collapse: collapse; margin-top: 20px; }"
                + ".table td { padding: 8px; border-bottom: 1px solid #ddd; }"
                + ".table img { width: 20px; vertical-align: middle; margin-right: 8px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h1>Bienvenido al Master - Liceo Yarah</h1>"
                + "<img src='https://www.freeiconspng.com/thumbs/login-icon/user-login-icon-29.png' alt='Correo'>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Hola,</p>"
                + "<p>Se te han asignado credenciales para acceder al sistema:</p>"
                + "<table class='table'>"
                + "<tbody>"
                + "<tr>"
                + "<td><img src='https://img.icons8.com/ios/50/000000/user.png' alt='Usuario'> <strong>Usuario:</strong></td>"
                + "<td>" + username + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td><img src='https://img.icons8.com/ios/50/000000/key.png' alt='Contraseña'> <strong>Contraseña:</strong></td>"
                + "<td>" + password + "</td>"
                + "</tr>"
                + "</tbody>"
                + "</table>"
                + "<p>Por favor, utiliza estas credenciales para iniciar sesión en el sistema. Puede cambiar la contraseña más adelante si es el caso. Recuerde que los parámetros de acceso son únicos e intrasferibles y solo con permisos de consulta general al sistema</p>"
                + "<p>Si has recibido este correo por error, por favor ignóralo y elimínalo.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p style='margin: 0; padding-bottom: 5px;'>Liceo Yarah &copy; " + currentYear + ".</p>"
                + "<p style='margin: 0; padding-bottom: 5px;'>Todos los derechos reservados.</p>"
                + "<p style='margin: 0; padding-bottom: 5px;'>Desarrollado por: <strong><a href='https://www.linkedin.com/in/juan-sebastian-medina-toro-887491249/' target='_blank'>Juan Sebastian Medina Toro</a></strong></p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

}
