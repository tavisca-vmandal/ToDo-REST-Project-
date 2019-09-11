package com.telusko.demo.controller;

import com.telusko.demo.model.Todo;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.telusko.demo.dao.TodoRepo;

@RestController
@CrossOrigin(origins="*",allowedHeaders = "*")
public class TodoController
{
	@Autowired
	TodoRepo repo;

	@PostMapping("/todos")
	public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {

		repo.save(todo);
		if(repo.findById(todo.getItemId()).isPresent()) {
			return new ResponseEntity<>(todo,HttpStatus.CREATED);
		} else
			return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@GetMapping ("/todos")
	public ResponseEntity<List<Todo>> getTodos() {

		if(!repo.findAll().isEmpty())
			return new ResponseEntity<>(repo.findAll(),HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	@GetMapping("/todos/{itemId}")
	public ResponseEntity<Optional<Todo>> getTodo(@PathVariable("itemId")int id) {
		if(repo.findById(id).isPresent())
			return new ResponseEntity<>(repo.findById(id),HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/todos/{itemId}")
	public ResponseEntity<String> deleteTodo(@PathVariable("itemId")int id) {
		Todo item=repo.getOne(id);
		repo.delete(item);
		if(!repo.findById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

	}
	@PutMapping("/todos")
	public Todo updateTodo(@RequestBody Todo todo) {

		Todo t=repo.save(todo);
		return t;
	}
}
