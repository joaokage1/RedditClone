package com.joao.reddit.clone.services;

import com.joao.reddit.clone.exceptions.SpringRedditException;
import com.joao.reddit.clone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class MailService {

    private final JavaMailSender sender;
    private final MailContentBuilder builder;

    @Async
    public void sendMail(NotificationEmail email){
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("springreddit@email.com");
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getSubject());
            helper.setText(getBuilder().build(email.getBody()));
        };

        try {
            getSender().send(preparator);
            log.info("Activation email sent!!");
        } catch (MailException e){
            throw new SpringRedditException("Exception occurred when sending mail to " + email.getRecipient());
        }
    }
}
