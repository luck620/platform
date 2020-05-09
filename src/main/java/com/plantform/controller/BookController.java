package com.plantform.controller;

import com.plantform.dto.BookDTO;
import com.plantform.entity.Account;
import com.plantform.entity.Book;
import com.plantform.entity.MyResult;
import com.plantform.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@RestController
@RequestMapping("/book")
public class BookController {

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    BookRepository bookRepository;

    @ResponseBody
    @GetMapping("/getBookList/{pageNum}/{pageSize}")
    public Page<Book> getBookList(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return bookRepository.findAll(pageable);
    }

    @ResponseBody
    @PostMapping("/getBookListByOthers/{pageNum}/{pageSize}")
    public Page<Book> getBookListByOthers(@PathVariable("pageNum") Integer pageNum,
                                                @PathVariable("pageSize") Integer pageSize,
                                                @RequestBody Book book){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        String name = book.getName();
        String publish = book.getPublish();
        String type = book.getType();
        String addTimeStart = "1998-01-01";
        String addTimeEnd = "2020-05-08";
        String publishTimeStart = "1998-01-01";
        String publishTimeEnd = "2020-05-08";
        if(name.isEmpty() || name == null){
            name = "";
        }
        if(publish.isEmpty() || publish == null){
            publish = "";
        }
        if(type.isEmpty() || type == null){
            type = "";
        }
        System.out.println("name="+name+" publish="+publish+" type="+type);
        return bookRepository.findAllByOthers(name,publish,type,addTimeStart,addTimeEnd,publishTimeStart,publishTimeEnd,pageable);
    }

    @ResponseBody
    @PostMapping("/addBook")
    public MyResult addBook(@RequestBody BookDTO bookDTO){
        MyResult myResult = new MyResult();
        System.out.println(bookDTO.getPublishTime());
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setPublish(bookDTO.getPublish());
        book.setType(bookDTO.getType());
        book.setAddTime(LocalDateTime.now());
        book.setPublishTime(LocalDateTime.parse(bookDTO.getPublishTime(),df));
        Book book1 = bookRepository.save(book);
        if(book1 != null){
            myResult.setCode(200);
            myResult.setMsg("添加成功");
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/findBookById/{id}")
    public Book findAccountById(@PathVariable("id") int id){
        return bookRepository.findBookById(id);
    }

    @ResponseBody
    @PostMapping("/editBookById/{id}")
    public MyResult editAccountById(@PathVariable("id") int id,
                                    @RequestBody Book editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" type="+editForm.getType()+" publish="+editForm.getPublish()+" addTime="+editForm.getAddTime()+" publishTime="+editForm.getPublishTime());
        int result = bookRepository.update(editForm.getName(),editForm.getPublish(),editForm.getType(),editForm.getAddTime().toString(),editForm.getPublishTime().toString(),id);
        System.out.println("result= "+result);
        MyResult myResult = new MyResult();
        if(result == 1){
            myResult.setCode(200);
            myResult.setMsg("修改成功");
            return myResult;
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/deleteBookById/{id}")
    public MyResult deleteBookById(@PathVariable("id") int id){
        bookRepository.deleteById(id);
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("修改成功");
        return myResult;
    }
}
