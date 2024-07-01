package com.fingress.filereading.transformer;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransformerFileToJdbc implements Processor {
	Log log = LogFactory.getLog(TransformerFileToJdbc.class);

	@Override
	public void process(Exchange exchange) {
		File filePath = exchange.getIn().getBody(File.class);

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		try (BufferedReader read = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = read.readLine()) != null) {
				HashMap<String, Object> transformer = new HashMap<String, Object>();
				String[] values = line.split(",");

				int empId = Integer.parseInt(String.valueOf(values[0]));
				String name = String.valueOf(values[1]);
				Double salary = Double.parseDouble(String.valueOf(values[2]));
				Date doj = Date.valueOf(String.valueOf(values[3]));

				transformer.put("EmployeeId", empId);
				transformer.put("EmployeeName", name);
				transformer.put("EmployeeSalary", salary);
				transformer.put("Date_Of_Joining", doj);
				list.add(transformer);
			}
			exchange.getMessage().setBody(list);

		} catch (Exception e) {
			log.error("Unable to insert " + e);
			throw new RuntimeException();
		}

	}

}