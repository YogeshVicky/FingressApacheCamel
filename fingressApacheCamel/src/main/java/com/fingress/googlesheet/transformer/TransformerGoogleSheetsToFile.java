package com.fingress.googlesheet.transformer;

import java.util.Iterator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransformerGoogleSheetsToFile implements Processor {
	Log log = LogFactory.getLog(TransformerGoogleSheetsToFile.class);

	@Override
	public void process(Exchange exchange) {

		try {
			String filePath = exchange.getIn().getBody(String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(filePath);
			JsonNode valuesNode = jsonNode.get("values");

			StringBuilder csv = new StringBuilder();
			for (int i = 1; i < valuesNode.size(); i++) {
				JsonNode data = valuesNode.get(i);
				Iterator<JsonNode> elements = data.elements();
				while (elements.hasNext()) {
					csv.append(elements.next().asText());
					if (elements.hasNext()) {
						csv.append(",");
					}
				}
				csv.append("\n");
				exchange.getMessage().setBody(csv);
			}

		} catch (Exception e) {
			log.error("Invalid request", e);
			throw new RuntimeException();
		}

	}

}
