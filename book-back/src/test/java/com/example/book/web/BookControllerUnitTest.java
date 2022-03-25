package com.example.book.web;
//단위테스트 -> 컨트롤러 관련 로직만 테스트 Controller, Filter, ControllerAdvice가 메모리에

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean //bookService가 IoC환경에 bean으로 등록 됨
    private BookService bookService;

    //BDDMockito 패턴
    @Test
    public void test_save() throws Exception {
     //given(테스트를 하기 위한 준비)
        Book book = new Book(null, "스프링따라하기", "인정");
        String content = new ObjectMapper().writeValueAsString(book);
        log.info(content);
        when(bookService.save(book)).thenReturn(new Book(1L,"스프링따라하기","인정"));

        //when(테스트 실행)
        ResultActions resultActions = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then(검증)
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링따라하기"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void test_getBooks() throws Exception {
        //given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스프링따라하기","인정"));
        books.add(new Book(2L,"스프링따라하기2","인정"));
        when(bookService.getBooks()).thenReturn(books);

        ResultActions resultActions = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("스프링따라하기"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void test_getBook() throws Exception {
        //given
        Long id = 1L;
        when(bookService.getBook(id)).thenReturn(new Book(1L,"스프링따라하기","인정"));

        //when
        ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test_update() throws Exception {
        //given
        Long id = 1L;
        Book book = new Book(null, "스프링따라하기", "인정");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.updateBook(id,book)).thenReturn(new Book(1L,"스프링따라하기","인정"));

        //when
        //when(테스트 실행)
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

}
