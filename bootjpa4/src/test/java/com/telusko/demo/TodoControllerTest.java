package com.telusko.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telusko.demo.dao.TodoRepo;
import com.telusko.demo.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepo todoRepo;

    List<Todo> mockTodoList;
    @Before
    public void setUp(){
        Todo todo1=new Todo();
        todo1.setItemId(1);
        todo1.setItemName("Mocking");

        Todo todo2=new Todo();
        todo2.setItemId(2);
        todo2.setItemName("Test");
        mockTodoList = new ArrayList<Todo>();

        mockTodoList.add(todo1);
        mockTodoList.add(todo2);
    }

    @Test
    public void getAllTodos() throws Exception {
        // given

        given(todoRepo.findAll()).willReturn(mockTodoList);

        // when + then
        this.mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(content().json
                        ("[{'itemId': 1,'itemName': 'Mocking'},{'itemId': 2,'itemName': 'Test'}]"));
    }

    @Test
    public void testEmptyTodo() throws Exception {
        // given
        List<Todo> todos=new ArrayList<>();
        given(todoRepo.findAll()).willReturn(todos);

        // when + then
        this.mockMvc.perform(get("/todos"))
                .andExpect(status().isNoContent());
    }
    @Test
    public void getOneTodo() throws Exception {
        // given
        Todo todo=new Todo();
        todo.setItemId(2);
        todo.setItemName("Search");
        given(todoRepo.findById(2)).willReturn(java.util.Optional.ofNullable(todo));

        // when + then
        this.mockMvc.perform(get("/todos/2"))
                .andExpect(status().isOk())
                 .andExpect(content().json
                ("{'itemId': 2,'itemName': 'Search'}"));
    }
    @Test
    public void notAvailableTodo() throws Exception {
        // given

        given(todoRepo.findById(3)).willReturn(java.util.Optional.ofNullable(null));

        // when + then
        this.mockMvc.perform(get("/todos/3"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void addTodoItem() throws Exception {
        // given
        Todo todo=new Todo();
        todo.setItemId(2);
        todo.setItemName("Search");

        given(todoRepo.save(todo)).willReturn(todo);
        given(todoRepo.findById(2)).willReturn(Optional.of(todo));

        // when + then
        ObjectMapper mapper = new ObjectMapper();
        this.mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isCreated());
    }

    @Test
    public void notAbleToAddTodo() throws Exception {
        // given
        Todo todo=new Todo();
        todo.setItemId(2);
        todo.setItemName("Search");

        given(todoRepo.save(todo)).willReturn(todo);
        given(todoRepo.findById(2)).willReturn(java.util.Optional.ofNullable(null));

        // when + then
        ObjectMapper mapper = new ObjectMapper();
        this.mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isConflict());
    }
    @Test
    public void updateTodoItem() throws Exception {
        // given
        Todo todo=new Todo();
        todo.setItemId(2);
        todo.setItemName("Search");

        given(todoRepo.save(todo)).willReturn(todo);

        // when + then
        ObjectMapper mapper = new ObjectMapper();
        this.mockMvc.perform(put("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTodoItem() throws Exception {

        given(todoRepo.findById(2)).willReturn(java.util.Optional.ofNullable(null));

        this.mockMvc.perform(delete("/todos/2"))
                .andExpect(status().isOk());
    }
    @Test
    public void notAbleToDeleteTodoItem() throws Exception {
        Todo todo=new Todo();
        todo.setItemId(2);
        todo.setItemName("Search");

        given(todoRepo.findById(2)).willReturn(Optional.of(todo));

        this.mockMvc.perform(delete("/todos/2"))
                .andExpect(status().isNotModified());
    }

}