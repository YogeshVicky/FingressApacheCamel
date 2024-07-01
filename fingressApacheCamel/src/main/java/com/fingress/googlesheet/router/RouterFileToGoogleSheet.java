package com.fingress.googlesheet.router;

import org.apache.camel.builder.RouteBuilder; 
import org.apache.camel.component.google.sheets.GoogleSheetsComponent;
import org.apache.camel.component.google.sheets.GoogleSheetsConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.fingress.googlesheet.transformer.TransformerFileToGoogleSheets;

@Component
public class RouterFileToGoogleSheet extends RouteBuilder {
	Log log = LogFactory.getLog(RouterFileToGoogleSheet.class);
	
	// Use own oauth2 credentials
	private static String clientId = "1094615859075-po0nlqt2fjatsq510e65cd2kb5jq2j6k.apps.googleusercontent.com";
	private static String clientSecret = "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";
	private static String accessToken = "ya29.a0AXooCgtnCly6OdZnk5EZftSqybSyMPJXdkkdVretFA8cOV9K0V98kCNP4ir2An13JlMdVql3-MYOJUpf2sKSacVlbONSX3tMdVDTZZPQgduo2gGPL64B60S78NgUft8HY_vyOyA-9aYq1AtFLqEGWlj0g1E8u8g8qDXJaCgYKAWASARESFQHGX2Mil4C7QNaekHBqYgxDnlPEjA0171";
	private static String refreshToken = "1//04OUBpvu98dqkCgYIARAAGAQSNwF-L9Ir8VtnCdDJNNWm8L6NJXkxBKxEAYszgtFr7d4PEFzKmCLcl3LlyFfJd-U-re1YEbhL5Tw";
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
			@SuppressWarnings("unused")
			String range = "Camel Sheet1";

			GoogleSheetsComponent component = new GoogleSheetsComponent();
			component.setConfiguration(configuration);
			getContext().addComponent("google-sheets", component);

			from("file:file/Input?noop=true").process(new TransformerFileToGoogleSheets()).log("${body}")
					.to("google-sheets://data/append?spreadsheetId=" + spreadsheetId + "&range='Camel Sheet1'")
					.log("Success");
		} catch (Exception e) {
			log.error("Invalid Request" + e);
			throw new RuntimeException();
		}
	}

}
