package com.pl2_vertx.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.log4j.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pl2_vertx.dto.Employee;
import com.pl2_vertx.dto.Log;
import com.pl2_vertx.producer.Producer;
import com.pl2_vertx.service.EmployeeService;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class EmployeeController {

	private static final Logger logger= LogManager.getLogger(EmployeeController.class.getName());
	Date date= new Date();
	long time = date.getTime();
	
	
	private static EmployeeService empService = new EmployeeService();
	private static Producer producer = new Producer();
	private static Timestamp ts = new Timestamp(new Date().getTime());

	public static void addOne(RoutingContext routingContext) {
		
		logger.info("Entering addOne()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
		System.out.println("1.Entry: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
       
			
		JsonObject json = routingContext.getBodyAsJson();
		Employee emp = new Employee();
		emp.setName(json.getString("name"));
		emp.setEmail(json.getString("email"));
		emp.setPhone(json.getString("phone"));
		emp.setTenure(json.getString("tenure"));
		empService.addEmployee(emp);

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(emp));

		producer.sendLog(new Log(" 3 Employee Added."));
		System.out.println(" 5 Logic executed successfully for get employee....");
		
		logger.info("Exiting addOne()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
		System.out.println(" 6 Exit: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
	}

	public static void addAll(RoutingContext routingContext) { // getting data from postman
		
		// printing the entry time to the method 
		logger.info("Entering addAll()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
				
			System.out.println("Entry: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));


		JsonArray jsonObjectArray = routingContext.getBodyAsJsonArray(); // jsonobjectarray

		jsonObjectArray.forEach((eachObj) -> { // looping the data
			System.out.println(eachObj);

			Employee emp = new Employee();
			emp.setName(((JsonObject) eachObj).getString("name"));
			emp.setEmail(((JsonObject) eachObj).getString("email"));
			emp.setPhone(((JsonObject) eachObj).getString("phone"));
			emp.setDoj(((JsonObject) eachObj).getString("doj"));
			empService.addEmployee(emp);
			System.out.println("Logic executed successfully for get employee....");
		});

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(jsonObjectArray));

		producer.sendLog(new Log("Employees Added."));
logger.info("Exiting addAll()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
		System.out.println("Exit: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
	}

	public static void getOne(RoutingContext routingContext) {
	logger.info("Entering getOne()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
	System.out.println("Entry: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));

		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Employee emp = empService.getOneEmployee(id);
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(emp));

			producer.sendLog(new Log("Employee Retrieved."));
		logger.info("Exiting getOne()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
			
			System.out.println("Exit: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		}
	}

	public static void getAll(RoutingContext routingContext) {
		//stem.out.println("Entry: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		logger.info("Entering getAll()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
		System.out.println("Entry: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(empService.getAllEmployees().values()));

		producer.sendLog(new Log("All Employees Retrieved."));
logger.info("Exiting getAll()" + new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		
		//stem.out.println("Exit: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
		System.out.println("Exit: "+new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()));
	}

	// Get a list of column values from employee table
	public static void getListOfColValues(RoutingContext routingContext, Function<Employee, String> lambda) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(empService.getListOfColValues(lambda)));

		producer.sendLog(new Log("Retrieve Column Values for All Employees."));
	}

	// Get an employee by column value
	public static void getEmployeeByCol(RoutingContext routingContext, Predicate<Employee> pred) {
		Employee emp = empService.getEmployeeByCol(pred);
		if (emp == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(emp));
		}

		producer.sendLog(new Log("Employee Retrieved by Column."));
	}

	// Get list of sorted employees
	public static void getSortedEmployees(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(empService.getSortedEmployees().values()));

		producer.sendLog(new Log("All Employees Retrieved Sorted by Name."));
	}

	public static void removeOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			empService.removeEmployee(id);
			routingContext.response().setStatusCode(200).end();
			producer.sendLog(new Log("Employee Removed."));
		}

	}

	// Delete all
	public static void removeAll(RoutingContext routingContext) {
		empService.removeAllEmployees();
		routingContext.response().setStatusCode(200).end();
		producer.sendLog(new Log("Remove All Employees."));
	}

	public static void updateOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Employee emp = empService.getOneEmployee(id);
			if (emp == null) {
				routingContext.response().setStatusCode(400).end();
				return;
			}
			JsonObject json = routingContext.getBodyAsJson();
			emp.setName(json.getString("name"));
			emp.setEmail(json.getString("email"));
			emp.setPhone(json.getString("phone"));
			emp.setTenure(json.getString("tenure"));
			empService.updateEmployee(emp);
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(emp));

			producer.sendLog(new Log("Employee Updated."));
		}

	}

}
