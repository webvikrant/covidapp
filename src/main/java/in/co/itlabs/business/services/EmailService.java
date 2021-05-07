package in.co.itlabs.business.services;

import java.io.IOException;
import java.util.Properties;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	private Mailer mailer = null;

	public EmailService() {
		

		try {
			// read properties file
			Properties props = new Properties();
			props.load(getClass().getResourceAsStream("/config.properties"));

			String smtpServer = props.getProperty("emailer.smtp_server");
			String portString = props.getProperty("emailer.port");
			int port = Integer.valueOf(portString);
			String login = props.getProperty("emailer.login");
			String password = props.getProperty("emailer.password");

			System.out.println("Properties: " + smtpServer + ", " + port + ", " + login + ", " + password);

			logger.info("Creating mailer...");

			// initialize a connection pool for H2
			mailer = MailerBuilder.withSMTPServer(smtpServer, port, login, password).async().buildMailer();
			logger.info("Mailer created...");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	public Mailer getMailer() {
		return mailer;
	}
}
