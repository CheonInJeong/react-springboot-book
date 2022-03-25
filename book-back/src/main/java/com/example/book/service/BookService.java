package com.example.book.service;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor //final이 붙어 있는 것들의 컨스트럭터를 만들어줌 -> 자동 DI
@Service //기능을 정의할 수 있고, 트랜잭션을 관리 할 수 있음
public class BookService {
    private final BookRepository bookRepository;

    @Transactional //서비스 함수가 종료 될 때 commit될지 rollback 할지 결졍
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true) //jpa는 엔티티가 변경되었는지 계속 감지를 하는데 readOnly를 true로 놓으면 감지 비활성화, update의 정합성을 유지함.
    //insert의 유령데이터현상(팬텀현상)못막음
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요"));
    }

    @Transactional(readOnly = true)
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book updateBook(Long id, Book book) {
        Book bookEntity = bookRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("id를 확인해주세요")); //Book Object 영속화->스프링내부에서 가지고 있음(영속성컨텍스트)
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return null;
    } //함수종료 -> 트렌잭션종료-> 영속화 되어 있는 데이터를 DB로 갱신(flush) -> commit ----> 더티체킹

    @Transactional
    public String deleteBook(Long id) {
        bookRepository.deleteById(id);
        return "ok";
    }
}
