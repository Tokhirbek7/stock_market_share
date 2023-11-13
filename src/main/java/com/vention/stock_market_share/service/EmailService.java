package com.vention.stock_market_share.service;

import com.vention.stock_market_share.code.CodeService;
import com.vention.stock_market_share.model.Stock;
import com.vention.stock_market_share.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public final UserFavoriteStockService userFavoriteStockService;
    private final CodeService codeService;
    @Value("${spring.mail.username}")
    private String fromMail;

    public boolean sendMailWithCode(String to) {
        try {
            String generatedCode = codeService.generateCode();
            codeService.save(generatedCode, to);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setSubject("Registration Confirmation");
            message.setText("Please verify your email with this code: " + generatedCode);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("An error occurred while sending the email", e);
            return false;
        }
    }

    @Scheduled(cron = "0 4 13 * * *")
    public void sendDailyStockEmails() {
        List<User> users = userFavoriteStockService.getAllUsersWhoLikedStock();
        for (User user : users) {
            List<Stock> favoriteStocks = userFavoriteStockService.getFavoriteStocks(user.getId());
            String emailText = generateEmailContent(user, favoriteStocks);
            sendEmail(user.getEmail(), emailText);
        }
    }

    private String generateEmailContent(User user, List<Stock> favoriteStocks) {
        StringBuilder content = new StringBuilder();
        content.append("Hello ").append(user.getFirstname()).append(" ").append(user.getLastname()).append(",\n\n");
        if (favoriteStocks.isEmpty()) {
            content.append("You haven't added any favorite stocks yet.");
        } else {
            content.append("Here's the information about your favorite stocks:\n\n");
            for (Stock stock : favoriteStocks) {
                content.append("Stock Symbol: ").append(stock.getSymbol()).append("\n");
                content.append("Stock Name: ").append(stock.getName()).append("\n");
                content.append("Currency: ").append(stock.getCurrency()).append("\n");
                content.append("Exchange: ").append(stock.getExchange()).append("\n");
                content.append("MIC Code: ").append(stock.getMicCode()).append("\n");
                content.append("Country: ").append(stock.getCountry()).append("\n");
                content.append("Type: ").append(stock.getType()).append("\n");
                content.append("Date: ").append(stock.getDate()).append("\n\n");
            }
        }
        content.append("Thank you for using our stock information service.\n");
        return content.toString();
    }

    private void sendEmail(String to, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setSubject("Information about liked stocks");
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("An error occurred while sending the email", e);
        }
    }
}
