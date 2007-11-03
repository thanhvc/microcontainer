package org.jboss.example.client;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;
import org.jboss.example.service.HRManager;
import org.jboss.example.service.util.AgeBasedSalaryStrategy;
import org.jboss.example.service.util.LocationBasedSalaryStrategy;
import org.jboss.example.service.util.SalaryStrategy;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistry;

public class Client {
    
	protected boolean useBus = false;
		
	private EmbeddedBootstrap bootstrap;
	private Kernel kernel;
	private KernelRegistry registry;
	private KernelBus bus;

	private URL url;
	private HRManager manager;

	private final static String HRSERVICE = "HRService";
	private final static String EMPLOYEE = "org.jboss.example.service.Employee";

	public static void main(String[] args) throws Exception {
		if ((args.length == 1 && !args[0].equals("bus")) || args.length > 1) {
			System.out.println("Usage: java -jar cmdLineClient-1.0.0.jar [bus]");
			System.exit(1);
		}

		new Client(args.length == 1);
    }

	public Client(final boolean useBus) throws Exception {
		
		this.useBus = useBus;
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		url = cl.getResource("META-INF/jboss-beans.xml");
	
		// Start JBoss Microcontainer
		bootstrap = new EmbeddedBootstrap();
		bootstrap.run();
		kernel = bootstrap.getKernel();
		registry = kernel.getRegistry();
		bus = kernel.getBus();
		
		new ConsoleInput(this, bootstrap, useBus, url);		
 	}
	
	void cacheServiceRef() {
		if (manager == null) {
			KernelControllerContext context = (KernelControllerContext) registry.getEntry(HRSERVICE);
			if (context != null) { manager = (HRManager) context.getTarget(); }
		}
	}

	private Object invoke(String serviceName, String methodName, Object[] args, String[] types) {
		Object result = null;
		try {
			result = bus.invoke(serviceName, methodName, args, types);
		} catch (Throwable t) {
			t.printStackTrace();
		}	
		return result;
	}
	
	void addEmployee() throws ParseException, NumberFormatException, IllegalArgumentException, IOException {
		Employee newEmployee = ConsoleInput.getEmployee();		
		Address address = ConsoleInput.getAddress();
		Date dateOfBirth = ConsoleInput.getDateOfBirth();
		
		newEmployee.setAddress(address);
		newEmployee.setDateOfBirth(dateOfBirth);
		
		boolean added;	
		if (useBus)
			added = (Boolean) invoke(HRSERVICE, "addEmployee", new Object[] {newEmployee}, new String[] {EMPLOYEE});
		else
			added = manager.addEmployee(newEmployee);			
		System.out.println("Added employee: " + added);
	}
	
	@SuppressWarnings("unchecked")
	void listEmployees() {			
		Set<Employee> employees;
		if (useBus)
			employees = (Set<Employee>) invoke(HRSERVICE, "getEmployees", new Object[] {}, new String[] {});
		else
			employees = manager.getEmployees();
		System.out.println("Employees: " + employees);					
	}
	
	void removeEmployee() throws IllegalArgumentException, IOException {			
		Employee employee = ConsoleInput.getEmployee();
		
		if (useBus)
			invoke(HRSERVICE, "removeEmployee", new Object[] {employee}, new String[] {EMPLOYEE});
		else
			manager.removeEmployee(employee);
	}
	
	void getSalary() throws IllegalArgumentException, IOException {
		Employee employee = ConsoleInput.getEmployee();

		Integer salary = null;
		if (useBus)
			salary = (Integer) invoke(HRSERVICE, "getSalary", new Object[] {employee}, new String[] {EMPLOYEE});
		else
			salary = manager.getSalary(employee);
		System.out.println("Salary: " + salary);			
	}
	
	void setSalary() throws NumberFormatException, IllegalArgumentException, IOException {
		Employee employee = ConsoleInput.getEmployee();	
		Integer salary = ConsoleInput.getSalary();		
		
		Employee actualEmployee;
		if (useBus) {
			actualEmployee = (Employee) invoke(HRSERVICE, "getEmployee", new Object[] {employee.getFirstName(), employee.getLastName()}, new String[] {"java.lang.String","java.lang.String"});	
			invoke(HRSERVICE, "setSalary", new Object[] {actualEmployee, salary}, new String[] {EMPLOYEE, "java.lang.Integer"});	
		} else {
			actualEmployee = manager.getEmployee(employee.getFirstName(), employee.getLastName());
			manager.setSalary(actualEmployee, salary);			
		}			
	}
	
	void toggleHiringFreeze() {
		boolean hiringFreeze;
		if (useBus) {
			hiringFreeze = (Boolean) invoke(HRSERVICE, "isHiringFreeze", new Object[] {}, new String[] {});	
			invoke(HRSERVICE, "setHiringFreeze", new Object[] {!hiringFreeze}, new String[] {"boolean"});	
		} else {
			hiringFreeze = manager.isHiringFreeze();
			manager.setHiringFreeze(!hiringFreeze);
		}		
	}
	
	@SuppressWarnings("unchecked")
	void printStatus() {
		boolean hiringFreeze;
		int totalEmployees;
		SalaryStrategy salaryStrategy;
		
		if (useBus) {
			try {
				hiringFreeze = (Boolean) invoke(HRSERVICE, "isHiringFreeze", new Object[] {}, new String[] {});
				Set<Employee> employees = (Set<Employee>) invoke(HRSERVICE, "getEmployees", new Object[] {}, new String[] {});
				totalEmployees = employees.size();				
				salaryStrategy = (SalaryStrategy) invoke(HRSERVICE, "getSalaryStrategy", new Object[] {}, new String[] {});
			} catch (Exception e) {
				System.out.println("HRService is not deployed.");
				return;
			}
		} else {
			hiringFreeze = manager.isHiringFreeze();
			totalEmployees = manager.getEmployees().size();
			salaryStrategy = manager.getSalaryStrategy();		
		}	
		
		System.out.println("Total number of employees: " + totalEmployees);
		System.out.println("Hiring Freeze: " + hiringFreeze);
		String strategy = "";
		if (salaryStrategy == null) { strategy = "None"; }
		else if (salaryStrategy instanceof AgeBasedSalaryStrategy ) { strategy = "AgeBased"; }
		else if (salaryStrategy instanceof LocationBasedSalaryStrategy ) { strategy = "LocationBased"; }
		
		System.out.print("Salary Strategy: " + strategy);
		if (salaryStrategy != null) {
			System.out.print(" - MinSalary: " + salaryStrategy.getMinSalary() + " MaxSalary: " + salaryStrategy.getMaxSalary());
		}
		System.out.println();
	}
}