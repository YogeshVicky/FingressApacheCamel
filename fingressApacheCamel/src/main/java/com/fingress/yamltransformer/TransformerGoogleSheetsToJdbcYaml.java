package com.fingress.yamltransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.api.services.sheets.v4.model.ValueRange;

public class TransformerGoogleSheetsToJdbcYaml implements Processor {
	Log log = LogFactory.getLog(TransformerGoogleSheetsToJdbcYaml.class);

	@Override
	public void process(Exchange exchange) {

		try {
			ValueRange range = (ValueRange) exchange.getIn().getBody();
			List<List<Object>> googleList = range.getValues();
			List<HashMap<String, Object>> jdbcList = new ArrayList<HashMap<String, Object>>();
			String[] Headers = { "EmployeeId", "EmployeeName", "EmployeeSalary", "Date_Of_Joining" };

			for (int i = 1; i < googleList.size(); i++) {
				List<Object> row = googleList.get(i);

				HashMap<String, Object> map = new HashMap<String, Object>();

				for (int j = 0; j < Headers.length && j < row.size(); j++) {

					map.put(Headers[j], row.get(j));
				}
				jdbcList.add(map);
			}
			log.info("Google List: " + jdbcList);
			exchange.getMessage().setBody(jdbcList);

		} catch (Exception e) {
			log.error("Invalid request", e);
			throw new RuntimeException();
		}

	}

}
