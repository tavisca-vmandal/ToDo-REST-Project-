package com.telusko.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.telusko.demo.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



public class RestControllerTest extends AbstractTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void getTodoList() throws Exception {
        String uri = "/todos";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Todo[] todoList = super.mapFromJson(content, Todo[].class);
        assertTrue(todoList.length > 0);
    }
    @Test
    public void createTodo() throws Exception {
        String uri = "/todos";
        Todo todo = new Todo();
        todo.setItemId(3);
        todo.setItemName("Testing");

        String inputJson = super.mapToJson(todo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        String expected="{\"itemId\":3,\"itemName\":\"Testing\"}";
        assertEquals( expected,content);
    }

    @Test
    public void updateTodo() throws Exception {
        String uri = "/todos";
        Todo todo = new Todo();
        todo.setItemId(2);
        todo.setItemName("Refactor");

        String inputJson = super.mapToJson(todo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        String expected="{\"itemId\":2,\"itemName\":\"Refactor\"}";
        assertEquals(expected,content);
    }
    @Test
    public void deleteTodo() throws Exception {
        String uri = "/todos/2";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("deleted",content);
    }
}