package com.example.book.service;

import com.example.book.domain.BookRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * MockitoExtension : repository를 가짜 객체로 만들 수 있음
 */
//서비스와 관련 된 애들만 메모리에 띄우면 된다.
@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
    @InjectMocks //bookService객체가 만들어 질 때 BookServiceUnitTest파일에 @Mock로 등록 된 모든 애들을 주입받는다.
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
}
