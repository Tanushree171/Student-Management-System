package com.university.sms.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@Component
public class CaptchaGenerator {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 60;
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefhjkmnpqrstuvwxyz23456789";
    private final Random random = new Random();

    public String generateCaptchaText(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public String generateCaptchaImage(String text) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Background
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Noise lines
        for (int i = 0; i < 8; i++) {
            g.setColor(new Color(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
            g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT),
                       random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }

        // Text
        g.setFont(new Font("Arial", Font.BOLD, 32));
        for (int i = 0; i < text.length(); i++) {
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            double angle = (random.nextDouble() - 0.5) * 0.4;
            g.rotate(angle, 30 + i * 35, 40);
            g.drawString(String.valueOf(text.charAt(i)), 25 + i * 35, 42);
            g.rotate(-angle, 30 + i * 35, 40);
        }

        // Noise dots
        for (int i = 0; i < 100; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.fillRect(random.nextInt(WIDTH), random.nextInt(HEIGHT), 2, 2);
        }

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public void storeCaptcha(HttpSession session, String captchaText) {
        session.setAttribute("captcha", captchaText);
    }

    public boolean validateCaptcha(HttpSession session, String input) {
        String stored = (String) session.getAttribute("captcha");
        if (stored != null && stored.equalsIgnoreCase(input)) {
            session.removeAttribute("captcha");
            return true;
        }
        return false;
    }
}
