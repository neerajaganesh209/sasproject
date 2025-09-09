# sasproject
Implement a menu-driven console application that supports: 
- Adding tasks (title, description, due date)
- Listing tasks (all, only completed, or only pending)
- Updating existing tasks - Marking tasks as completed
- Deleting tasks
- Store tasks in memory using Java collections (List / Map) - Map is used here
- Persist tasks between runs by saving them to a file (e.g., CSV or simple text format). - Jackson is used here with file stored in JSON file which can be stored in DBs in future.
- Load tasks from the file on application startup.
- Provide clear console output for all operations.


Setup
1. Create a project named app and file named JacksonJsonMap.java under app folder.
2. Create a POJO class Task.java for storing Task fields.
3. Create tasks.json file under src/main/resources.
4. Add these depencies in POm.xal file to run Jackson.
    <dependencies>
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
			<version>2.17.2</version>
		</dependency>
	 	
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.17.2</version>
		</dependency>

	</dependencies>
6. Run JacksonJsonMap.java as Java Project.
