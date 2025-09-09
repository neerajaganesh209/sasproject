
package app;

import java.time.LocalDate;
import java.util.*;

public class Task {

	private int id;
	private String title;
	private String description;
	private LocalDate dueDate;
	private boolean completed;

	public Task() {
	}

	public Task(int nextId, String title2, String description2, LocalDate dueDate2, boolean completed) {
		this.id = nextId;
		this.title = title2;
		this.description = description2;
		this.dueDate = dueDate2;
		this.completed = completed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", dueDate=" + dueDate
				+ ", completed=" + completed + "]";
	}

}
