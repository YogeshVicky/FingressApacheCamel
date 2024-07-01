package com.fingress.googlesheet.router;

import org.apache.camel.builder.RouteBuilder; 
import org.apache.camel.component.google.sheets.GoogleSheetsComponent;
import org.apache.camel.component.google.sheets.GoogleSheetsConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.fingress.googlesheet.transformer.TransformerGoogleSheetsToFile;

@Component
public class RouterGoogleSheetsToFile extends RouteBuilder {
	Log log = LogFactory.getLog(RouterGoogleSheetsToFile.class);

	// Use own oauth2 credentials
	private static String clientId = "1094615859075-3stum4c78pljrnv50oru5e7khu6armrr.apps.googleusercontent.com";
	private static String clientSecret = "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";
	private static String accessToken = "ya29.a0AXooCgunjT_xrPE7F8pxus3aJbIlmJopoWqr0XDbH9CdXPJRQsiEtoSSiyT06i-pVyLTfZ5UYddV-CqeDZAu5z-J5JEZvXptlLqsUivH_J2piKly7HCIOwBlRIZoE4VFVr2DavPrd7rpg1OJ5S4KLvUl4A1ggYO3mxhfaCgYKAcISARESFQHGX2Mi9UX9Ryb6-k5-52-G-wCeGQ0171";
	private static String refreshToken = "1//04KYF4ryoY_AhCgYIARAAGAQSNwF-L9IrfEwyM8wYtENwsQrwLlYfCCqYKRzjkz8Wz6ytvN9SG4qICv3cqdtWb5wHZ8s0nHMEDMY";
	private static String applicationName = "Camel-Google-Sheets";

	@Override
	public void configure() {
		try {
			GoogleSheetsConfiguration configuration = new GoogleSheetsConfiguration();

			configuration.setClientId(clientId);
			configuration.setRefreshToken(refreshToken);
			configuration.setClientSecret(clientSecret);
			configuration.setAccessToken(accessToken);
			configuration.setApplicationName(applicationName);
			String spreadsheetId = "1i8jAVG3r6BINzdU0NzgAIuDUuz3FiVRyFF1Wm6WQq_0";
			String range = "Main";

			GoogleSheetsComponent component = new GoogleSheetsComponent();
			component.setConfiguration(configuration);
			getContext().addComponent("google-sheets", component);

			from("timer:run?repeatCount=1")
					.to("google-sheets:data/get?spreadsheetId=" + spreadsheetId + "&range=" + range).log("${body}")
					.convertBodyTo(String.class).process(new TransformerGoogleSheetsToFile())
					.to("file:file/Input?fileName=employee.csv").log("Sucess");
		} catch (Exception e) {
			log.error("Invalid Request" + e);
			throw new RuntimeException();
		}
	}

}
