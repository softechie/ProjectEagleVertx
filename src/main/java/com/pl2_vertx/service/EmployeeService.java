package com.pl2_vertx.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.error.BucketDoesNotExistException;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.pl2_vertx.DAO.EmployeeDao;
import io.vertx.core.json.Json;
import java.util.LinkedHashMap;
import com.pl2_vertx.config.DbConfig;
import com.pl2_vertx.dto.Employee;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

// singelton
public class EmployeeService {

    private EmployeeDao employeeDao = EmployeeDao.getService();

    public void addEmployee(Employee emp){employeeDao.addEmployee(emp);}
    public Employee getOneEmployee(String id){return employeeDao.getOneEmployee(id);}
    public Map<String, Employee> getAllEmployees(){return employeeDao.getAllEmployees();}
    public Employee getEmployeeByCol(Predicate<Employee> pred){return employeeDao.getEmployeeByCol(pred);}
    public List<String> getListOfColValues(Function<Employee, String> lambda) {return employeeDao.getListOfColValues(lambda);}
    public Map<String,Employee> getSortedEmployees() { return employeeDao.getSortedEmployees();}
    public void removeEmployee(String id){employeeDao.removeEmployee(id);}
    public void removeAllEmployees() { employeeDao.removeAllEmployees(); }
    public void updateEmployee(Employee emp){employeeDao.updateEmployee(emp);}

}
