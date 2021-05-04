package in.co.itlabs.business.services;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	private Mailer mailer = null;

	public EmailService() throws IOException {
		logger.info("Reading file \"minierp-config.properties\"...");

		// read properties file
		String configFile = System.getenv("MINIERP_CONFIG");

		FileReader reader = new FileReader(configFile);
		Properties props = new Properties();
		props.load(reader);

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
	}

	public Mailer getMailer() {
		return mailer;
	}
}
