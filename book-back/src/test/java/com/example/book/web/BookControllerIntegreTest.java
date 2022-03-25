package com.example.book.web;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import com.example.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//통합테스트(모든 bean을 똑같이 IoC 올리고 테스트)
/**
 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //실제 톰캣에 올리는게 아니라 다른 톰켓으로 테스트
 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //실제 톰캣으로 테스트
 @Transactional //각 각의 테스트 함수가 종료 될 때 마다 트랜잭션을 rollback 해주는 어노테이션
 @AutoConfigureMockMvc MockMvc를 IoC에 등록
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //실제 톰캣에 올리는게 아니라 다른 톰켓으로 테스트
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void test_save() throws Exception {
        //given(테스트를 하기 위한 준비)
        Book book = new Book(null, "스프링따라하기", "인정");
        String content = new ObjectMapper().writeValueAsString(book);


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
        bookRepository.saveAll(books);

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
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스프링따라하기","인정"));
        books.add(new Book(2L,"스프링따라하기2","인정"));
        books.add(new Book(3L,"스프링따라하기3","인정"));
        bookRepository.saveAll(books);

        Long id = 2L;

        //when
        ResultActions resultActions = mockMvc.perform(get("/book/{id}",id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("스프링따라하기2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test_update() throws Exception {

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"스프링따라하기","인정"));
        books.add(new Book(2L,"스프링따라하기2","인정"));
        books.add(new Book(3L,"스프링따라하기3","인정"));
        bookRepository.saveAll(books);

        //given
        Long id = 1L;
        Book book = new Book(null, "java따라하기", "인정");
        String content = new ObjectMapper().writeValueAsString(book);


        //when(테스트 실행)
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultActions.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
