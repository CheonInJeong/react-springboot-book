package com.example.book.domain;
//메모리에 db 관련 된 bean이 IoC에 등록

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //가짜 디비로 테스트, Replace.NONE은 실제 db로 테스트
@DataJpaTest //repository를 다 Ioc에 등록해 줌
public class BookRepositoryUnitTest {
    @Autowired
    private BookRepository bookRepository;
}
