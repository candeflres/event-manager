package com.utn.eventmanager.service.passwordReset;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetCode(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                  <body style="margin:0; padding:0; background-color:#f4f6f8; font-family: Arial, sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0">
                      <tr>
                        <td align="center" style="padding: 40px 0;">
                          <table width="500" cellpadding="0" cellspacing="0"
                            style="background:#ffffff; border-radius:8px; padding:30px; box-shadow:0 4px 12px rgba(0,0,0,0.1);">

                            <tr>
                              <td align="center">
                                <h2 style="color:#351923; margin-bottom:10px;">
                                  Recuperación de contraseña
                                </h2>
                                <p style="color:#555; font-size:14px;">
                                  Recibimos una solicitud para restablecer tu contraseña.
                                </p>
                              </td>
                            </tr>

                            <tr>
                              <td align="center" style="padding: 25px 0;">
                                <div style="
                                  background:#f1e4e9;
                                  color:#351923;
                                  font-size:28px;
                                  font-weight:bold;
                                  letter-spacing:4px;
                                  padding:15px 30px;
                                  border-radius:6px;
                                  display:inline-block;">
                                  %s
                                </div>
                              </td>
                            </tr>

                            <tr>
                              <td align="center">
                                <p style="color:#555; font-size:14px;">
                                  Este código vence en <strong>15 minutos</strong>.
                                </p>
                                <p style="color:#999; font-size:12px;">
                                  Si no solicitaste este cambio, podés ignorar este correo.
                                </p>
                              </td>
                            </tr>

                            <tr>
                              <td align="center" style="padding-top:20px;">
                                <p style="color:#999; font-size:12px;">
                                  Event Manager © 2026
                                </p>
                              </td>
                            </tr>

                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(code);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }
}