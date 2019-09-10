package com.telusko.demo.controller;

import com.telusko.demo.model.Todo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.telusko.demo.dao.TodoRepo;

@RestController
@CrossOrigin(origins="*",allowedHeaders = "*")
public class TodoController
{
	@Autowired
	TodoRepo repo;

	@RequestMapping("/")
	public String home()
	{
		return "home.jsp";
	}
	@PostMapping("/todos")
	public Todo addTodo(@RequestBody Todo todo) {
		repo.save(todo);
		return todo;
	}

	@GetMapping ("/todos")
	public List<Todo> getTodos() {
		return repo.findAll();
	}

	@GetMapping("/todos/{itemId}")
	public Optional<Todo> getTodo(@PathVariable("itemId")int id) {
		return repo.findById(id);
	}

	@DeleteMapping("/todos/{itemId}")
	public String deleteTodo(@PathVariable("itemId")int id) {
		Todo item=repo.getOne(id);
		repo.delete(item);
		return "deleted";
	}
	@PutMapping("/todos")
	public Todo updateTodo(@RequestBody Todo todo) {
		repo.save(todo);
		return todo;
	}
}
