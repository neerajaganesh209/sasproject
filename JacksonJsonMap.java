package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonJsonMap {

	static Logger logger = Logger.getLogger(JacksonJsonMap.class.getName());
	static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	private static int nextId = 1;
	private static Map<Integer, Task> tasks = new HashMap<>();

	public static void main(String[] args) throws IOException {
		JacksonJsonMap ts = new JacksonJsonMap();
		Scanner sc = new Scanner(System.in);
		int choice;

		loadTasks();
		displayMenu();

		do {
			choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				addTask(sc);
				break;
			case 2:
				listTasks(sc);
				break;
			case 3:
				updateTask(sc);
				break;
			case 4:
				setTaskComplete(sc);
				break;
			case 5:
				deleteTask(sc);
				break;
			case 6:
				saveTasks();
			}
		} while (choice != 0);
	}

	private static void loadTasks() throws StreamReadException, DatabindException, IOException {
		File loadfile = new File("src/main/resources/tasks.json");

		tasks = mapper.readValue(loadfile, new TypeReference<Map<Integer, Task>>() {
		});
		nextId = tasks.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;

		if (loadfile.length() == 0 || !loadfile.exists()) {
			tasks = new HashMap<>();
			nextId = 1;
			saveTasks();
			return;
		}
	}

	private static void displayMenu() {
		System.out.println("\n Task Console Application");
		System.out.println("---------------------------");
		System.out.println("1 -> Add Task");
		System.out.println("2 -> List Tasks");
		System.out.println("3 -> Update Task");
		System.out.println("4 -> Mark Task as Completed");
		System.out.println("5 -> Delete Task");
		System.out.println("---------------------------");
		System.out.println("Choose an option:");
	}

	private static Task findPendingTask() {
		return tasks.values().stream().filter(task -> !task.isCompleted()).findAny().orElse(null);
	}

	private static Task findCompletedTask() {
		return tasks.values().stream().filter(task -> task.isCompleted()).findAny().orElse(null);
	}

	private static void addTask(Scanner s) throws IOException {
		System.out.println("---------------------------");
		System.out.println("\n Add Task");
		System.out.println("---------------------------");
		Scanner s1 = new Scanner(System.in);
		Task task = new Task();
		System.out.print("Enter title: ");
		String title = s1.nextLine();
		System.out.print("Enter description: ");
		String desc = s1.nextLine();
		System.out.print("Enter due date (YYYY-MM-DD): ");
		LocalDate dueDate = LocalDate.parse(s1.nextLine());
		int sid = nextId++;
		task.setId(sid);
		task.setTitle(title);
		task.setDescription(desc);
		task.setDueDate(dueDate);
		task.setCompleted(false);

		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		tasks.put(task.getId(), task);

		saveTasks();

		String saveJson = mapper.writeValueAsString(task);
		System.out.println(saveJson);

		logger.log(Level.INFO, "Msg: Task added successfully!");
		System.out.println("Task added successfully!");
	}

	private static void listTasks(Scanner sc) throws JsonProcessingException {

		if (tasks.isEmpty()) {
			System.out.println("No tasks found.");
			return;
		}

		System.out.println("List Options:");
		System.out.println("1. All Tasks");
		System.out.println("2. Completed Tasks");
		System.out.println("3. Pending Tasks");
		System.out.print("Enter choice: ");
		int choice = sc.nextInt();

		if (choice == 1) {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks));
		} else if (choice == 2) {
			Task complete = findCompletedTask();
			if (complete != null) {
				System.out.println("Found Completed Task:");
				Map<Integer, Task> completedTasks = tasks.entrySet().stream()
						.filter(entry -> entry.getValue().isCompleted()).collect(Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
				System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(completedTasks));
			} else {
				System.out.println("No Completed tasks found");
			}
		} else if (choice == 3) {
			Task pending = findPendingTask();
			if (pending != null) {
				System.out.println("Found Pending Task:");
				Map<Integer, Task> pendingTasks = tasks.entrySet().stream()
						.filter(entry -> !entry.getValue().isCompleted()).collect(Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
				System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pendingTasks));
			} else {
				System.out.println("No Pending tasks found");
			}
		}
	}

	private static void updateTask(Scanner sc) throws StreamWriteException, DatabindException, IOException {
		System.out.println("---------------------------");
		System.out.println("\n Update Task");
		System.out.println("---------------------------");
		int id = sc.nextInt();
		sc.nextLine();
		Task task = tasks.get(id);

		if (task != null) {
			System.out.println("New Title:");
			String title = sc.nextLine();
			if (!title.isEmpty())
				task.setTitle(title);
			System.out.println("New description:");
			String description = sc.nextLine();
			if (!description.isEmpty())
				task.setDescription(description);
			System.out.println("New Due date:");
			String dueDate = sc.nextLine();
			if (!dueDate.isEmpty())
				task.setDueDate(LocalDate.parse(dueDate));

			saveTasks();
			System.out.println("Task updated");
			logger.log(Level.INFO, "Msg: Task updated");
		} else {
			System.out.println("Task not found");
			logger.log(Level.INFO, "Msg: Task not found!");
		}
	}

	private static void setTaskComplete(Scanner sc) throws StreamWriteException, DatabindException, IOException {
		System.out.println("---------------------------");
		System.out.println("\n Mark Task as Completed");
		System.out.println("---------------------------");
		System.out.println("Enter Task ID to mark completed");
		int id = sc.nextInt();
		sc.nextLine();
		Task t1 = tasks.get(id);
		if (t1 != null) {
			t1.setCompleted(true);
			saveTasks();
			System.out.println("Task marked as completed");
			logger.log(Level.INFO, "Msg: Task marked as completed!");
		} else {
			System.out.println("Task not found");
		}
	}

	private static void deleteTask(Scanner sc) throws StreamWriteException, DatabindException, IOException {
		System.out.println("---------------------------");
		System.out.println("\n Delete Task");
		System.out.println("---------------------------");
		int id = Integer.parseInt(sc.nextLine());

		Task task = tasks.remove(id);

		if (task != null) {
			saveTasks();
			System.out.println("Task deleted Successfully!");
			logger.log(Level.INFO, "Msg: Task deleted Successfully!");
		} else {
			System.out.println("Task not found!");
			logger.log(Level.INFO, "Msg: Task not found!");
		}
	}

	private static void saveTasks() throws StreamWriteException, DatabindException, IOException {
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/tasks.json"), tasks);
	}

}
