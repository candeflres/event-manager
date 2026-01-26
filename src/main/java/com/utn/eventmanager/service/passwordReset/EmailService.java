package com.utn.eventmanager.service.passwordReset;

import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetCode(String to, String code) {

        String html = baseTemplate(
                "Recuperación de contraseña",
                "Recibimos una solicitud para restablecer tu contraseña.",
                "Código de verificación",
                """
                <p style="margin:0 0 12px 0;">
                  Usá el siguiente código para continuar con el proceso:
                </p>
    
                <div style="
                    font-size:28px;
                    font-weight:700;
                    letter-spacing:6px;
                    background:#f1e4e9;
                    color:#351923;
                    padding:14px 20px;
                    border-radius:10px;
                    text-align:center;
                    margin:16px 0;
                ">
                  %s
                </div>
    
                <p style="margin:0;">
                  Si no solicitaste este cambio, podés ignorar este correo.
                </p>
                """.formatted(code),
                "Este código tiene una validez limitada por seguridad."
        );

        sendHtmlMail(to, "Código para recuperar contraseña", html);
    }
    @Async
    public void sendHtmlMail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }

    // ---------- TEMPLATE BASE ----------
    private String baseTemplate(String title, String intro, String badgeText, String body, String footerNote) {
        return """
        <!DOCTYPE html>
        <html>
          <body style="margin:0; padding:0; background:#f4f6f8; font-family:Arial, sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0" style="padding:24px 0;">
              <tr>
                <td align="center">
                  <table width="560" cellpadding="0" cellspacing="0"
                         style="background:#ffffff; border-radius:12px; padding:28px; box-shadow:0 10px 30px rgba(0,0,0,0.12);">
                    <tr>
                      <td>
                        <div style="font-size:12px; color:#999; margin-bottom:10px;">Event Manager</div>
                        <div style="font-size:22px; font-weight:700; color:#351923; margin:0 0 6px 0;">%s</div>
                        <div style="font-size:14px; color:#555; margin:0 0 18px 0;">%s</div>

                        <div style="display:inline-block; background:#f1e4e9; color:#351923; font-weight:700;
                                    padding:8px 12px; border-radius:999px; font-size:12px; margin-bottom:16px;">
                          %s
                        </div>

                        <div style="font-size:14px; color:#444; line-height:1.6;">
                          %s
                        </div>

                        <hr style="border:none; border-top:1px solid #eee; margin:18px 0;" />

                        <div style="font-size:12px; color:#888; line-height:1.5;">
                          %s
                        </div>
                      </td>
                    </tr>
                  </table>

                  <div style="font-size:11px; color:#9aa0a6; margin-top:14px;">
                    © 2026 Event Manager
                  </div>
                </td>
              </tr>
            </table>
          </body>
        </html>
        """.formatted(title, intro, badgeText, body, footerNote);
    }

    // ---------- CLIENTE ----------
    public void sendEventPending(User user, Event event) {
        String html = baseTemplate(
                "Recibimos tu evento",
                "Tu solicitud fue registrada correctamente.",
                "Estado: PENDIENTE",
                """
                <p style="margin:0 0 10px 0;">
                  <strong>Evento:</strong> %s<br/>
                  <strong>Fecha:</strong> %s
                </p>
                <p style="margin:0;">
                  Nuestro equipo lo revisará y te avisaremos cuando haya novedades.
                </p>
                """.formatted(esc(event.getName()), esc(String.valueOf(event.getEventDate()))),
                "Si no realizaste esta acción, podés ignorar este correo."
        );

        sendHtmlMail(user.getEmail(), "Evento recibido", html);
    }

    public void sendEventApproved(User user, Event event) {
        String html = baseTemplate(
                "¡Tu evento fue aprobado!",
                "Buenas noticias: el evento pasó a estado aprobado.",
                "Estado: APROBADO",
                """
                <p style="margin:0 0 10px 0;">
                  <strong>Evento:</strong> %s<br/>
                  <strong>Fecha:</strong> %s
                </p>
                <p style="margin:0;">
                  Si necesitamos coordinar algún detalle adicional, te contactaremos.
                </p>
                """.formatted(esc(event.getName()), esc(String.valueOf(event.getEventDate()))),
                "Gracias por elegirnos."
        );

        sendHtmlMail(user.getEmail(), "Evento aprobado", html);
    }

    public void sendEventRejected(User user, Event event) {
        String html = baseTemplate(
                "Tu evento requiere cambios",
                "Lo revisamos, pero no puede aprobarse en su estado actual.",
                "Estado: RECHAZADO",
                """
                <p style="margin:0 0 10px 0;">
                  <strong>Evento:</strong> %s<br/>
                  <strong>Fecha:</strong> %s
                </p>
                <p style="margin:0;">
                  Te recomendamos ajustar la información (fecha, descripción u opciones) y volver a enviarlo para una nueva revisión.
                </p>
                """.formatted(esc(event.getName()), esc(String.valueOf(event.getEventDate()))),
                "Si creés que es un error, respondé este correo o contactanos desde la plataforma."
        );

        sendHtmlMail(user.getEmail(), "Evento rechazado", html);
    }

    public void sendEventCompleted(User user, Event event) {
        String html = baseTemplate(
                "Evento completado",
                "Marcamos tu evento como completado.",
                "Estado: COMPLETADO",
                """
                <p style="margin:0 0 10px 0;">
                  <strong>Evento:</strong> %s<br/>
                  <strong>Fecha:</strong> %s
                </p>
                <p style="margin:0;">
                  Esperamos que haya salido todo excelente. ¡Gracias por confiar en nosotros!
                </p>
                """.formatted(esc(event.getName()), esc(String.valueOf(event.getEventDate()))),
                "Este correo es informativo. No hace falta que respondas."
        );

        sendHtmlMail(user.getEmail(), "Evento completado", html);
    }

    // ---------- EMPLEADO ----------


    public void sendNewEventToEmployee(User employee, Event event) {

        String html = baseTemplate(
                "Nuevo evento pendiente",
                "Se registró un nuevo evento que requiere revisión.",
                "Acción: REVISAR",
                """
                <p style="margin:0 0 10px 0;">
                  <strong>Evento:</strong> %s<br/>
                  <strong>Fecha:</strong> %s<br/>
                  <strong>Cliente:</strong> %s
                </p>
                <p style="margin:0;">
                  Ingresá al sistema para aprobar o rechazar.
                </p>
                """.formatted(
                        esc(event.getName()),
                        esc(String.valueOf(event.getEventDate())),
                        esc(event.getUser() != null ? event.getUser().getEmail() : "-")
                ),
                "Notificación automática del sistema."
        );

        sendHtmlMail(employee.getEmail(), "Nuevo evento pendiente", html);
    }

    public void sendUpdatedEventToEmployee(User employee, Event event) {

        String html = baseTemplate(
                "Evento actualizado por el cliente",
                "Un evento rechazado fue modificado y volvió a pendiente.",
                "Acción: REVISAR CAMBIOS",
                """
                <p style="margin:0 0 10px 0;">
                  <strong>Evento:</strong> %s<br/>
                  <strong>Cliente:</strong> %s
                </p>
                <p style="margin:0;">
                  El evento requiere una nueva revisión.
                </p>
                """.formatted(
                        esc(event.getName()),
                        esc(event.getUser() != null ? event.getUser().getEmail() : "-")
                ),
                "Notificación automática del sistema."
        );

        sendHtmlMail(employee.getEmail(), "Evento actualizado (pendiente)", html);
    }

    // ---------- util para evitar romper HTML si hay caracteres raros ----------
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;")
                .replace("\"","&quot;")
                .replace("'","&#39;");
    }
}
