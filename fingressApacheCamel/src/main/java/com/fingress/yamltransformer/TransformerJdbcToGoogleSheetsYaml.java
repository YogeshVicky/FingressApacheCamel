package com.fingress.yamltransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.google.sheets.internal.GoogleSheetsConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.api.services.sheets.v4.model.ValueRange;

public class TransformerJdbcToGoogleSheetsYaml implements Processor {
	Log log = LogFactory.getLog(TransformerJdbcToGoogleSheetsYaml.class);

	String prefix = GoogleSheetsConstants.PROPERTY_PREFIX;

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		try {
			List<HashMap<String, Object>> jdbcList = (List<HashMap<String, Object>>) exchange.getIn().getBody();
			List<List<Object>> googleList = new ArrayList<>();
			ValueRange range = new ValueRange();

			for (HashMap<String, Object> data : jdbcList) {
				List<Object> rows = new ArrayList<>();
				for (Object value : data.values()) {
					rows.add(String.valueOf(value));
				}
				googleList.add(rows);
			}

			log.info("Google List: " + googleList);

			range.setMajorDimension("ROWS");
			range.setValues(googleList);
			exchange.getMessage().setBody(range);
			exchange.getMessage().setHeader(prefix + "valueInputOption", "USER_ENTERED");
			exchange.getMessage().setHeader(prefix + "values", range);

		} catch (Exception e) {
			log.error("Error processing exchange", e);
		}
	}

}
