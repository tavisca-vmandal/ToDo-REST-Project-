package com.telusko.demo.dao;

import com.telusko.demo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepo extends JpaRepository<Todo,Integer>
{

}
