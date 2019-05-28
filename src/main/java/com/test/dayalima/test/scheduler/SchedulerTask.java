package com.test.dayalima.test.scheduler;

import com.test.dayalima.test.service.LogApiHitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class SchedulerTask {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LogApiHitService logApiHitService;

    @Autowired
    Environment env;

    @Scheduled(fixedRate = 3600000, initialDelay = 10000)
    public void sendReportSummary() {
        sendMail(env.getProperty("spring.mail.username"),env.getProperty("mail.destination"),"Summary API", logApiHitService.getSummary().toString());
    }

    private void sendMail(String fromEmail, String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);

            mailSender.send(message);
        } catch (MessagingException ex) {

        }
    }
}

