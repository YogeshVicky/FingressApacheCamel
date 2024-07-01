package com.fingress.googlesheet.transformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.google.sheets.internal.GoogleSheetsConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.api.services.sheets.v4.model.ValueRange;

public class TransformerFileToGoogleSheets implements Processor {
	Log log = LogFactory.getLog(TransformerFileToGoogleSheets.class);
	String prefix = GoogleSheetsConstants.PROPERTY_PREFIX;

	@Override
	public void process(Exchange exchange) {
		File filePath = exchange.getIn().getBody(File.class);

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			ValueRange range = new ValueRange();
			List<List<Object>> values = new ArrayList<>();

			String line;
			while ((line = reader.readLine()) != null) {
				List<Object> list = new ArrayList<Object>();
				String[] comma = line.split(",");

				list.add(comma[0]);
				list.add(comma[1]);
				list.add(comma[2]);
				list.add(comma[3]);
				values.add(list);
			}
			range.setMajorDimension("ROWS");
			range.setValues(values);
			exchange.getMessage().setBody(range);
			exchange.getMessage().setHeader(prefix + "valueInputOption", "USER_ENTERED");
			exchange.getMessage().setHeader(prefix + "values", range);

		}

		catch (Exception e) {
			log.error("Invalid request", e);
			throw new RuntimeException();
		}

	}

}
