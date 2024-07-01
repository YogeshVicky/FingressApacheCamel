package com.fingress.filereading.transformer;

import java.util.HashMap;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransformerJdbcToFile implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange)  {
		Log log = LogFactory.getLog(TransformerJdbcToFile.class);

		String header = "EmployeeName,Date_Of_Joining\n";

		try {
			List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) exchange.getIn().getBody();
			StringBuilder filter = new StringBuilder();
			filter.append(header);

			for (HashMap<String, Object> list : data) {
				filter.append(list.get("EmployeeName")).append(',');
				filter.append(list.get("Date_Of_Joining")).append('\n');
			}
			exchange.getMessage().setBody(filter);
		} catch (Exception e) {
			log.error("Invalid request", e);
			throw new RuntimeException();
		}

	}

}
