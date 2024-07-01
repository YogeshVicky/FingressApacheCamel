package com.fingress.yamltransformer;

import java.util.HashMap;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransformerYamlJdbcToActivemq implements Processor {

	Log log = LogFactory.getLog(TransformerYamlJdbcToActivemq.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		String header = "EmployeeId,EmployeeName,EmployeeSalary,Date_Of_Joining\n";

		try {
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) exchange.getIn().getBody();
			StringBuilder filter = new StringBuilder();
			filter.append(header);
			for (HashMap<String, Object> list : data) {
				filter.append(list.get("EmployeeId")).append(',');
				filter.append(list.get("EmployeeName")).append(',');
				filter.append(list.get("EmployeeSalary")).append(',');
				filter.append(list.get("Date_Of_Joining")).append('\n');
			}
			exchange.getMessage().setBody(filter);
		} catch (Exception e) {
			log.error("Invalid request", e);
			throw new RuntimeException();
		}

	}

}
