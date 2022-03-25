package com.example.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 어노테이션을 작성을 해야 스프링 IoC에 빈으로 등록이 되는데 JpaRepository를 상속받으면 생략가능하다.
//JpaRepository는 CRUD 함수를 들고 있다.
public interface BookRepository extends JpaRepository<Book, Long> {
}
