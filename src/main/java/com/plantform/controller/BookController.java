package com.plantform.controller;

import com.plantform.dto.BookDTO;
import com.plantform.entity.Account;
import com.plantform.entity.Book;
import com.plantform.entity.MyResult;
import com.plantform.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    BookRepository bookRepository;

    public static <T> Page<T> listConvertToPage1(List<T> list, int totalElements, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, totalElements);
    }

    //拿出所有数据
    @ResponseBody
    @GetMapping("/findAllByType/{pageNum}/{pageSize}/{type}")
    public Page<Book> findAllByType(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize,
                                    @PathVariable("type") String type){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Book> bookList =  bookRepository.findAllByType(type);
        int toltalElements = bookRepository.findAllByTypeCount(type);
        List<Book> bookList1 = new ArrayList<>();
        if(bookList != null && !bookList.isEmpty()){
            for(Book book : bookList){
                Book book1 = new Book();
                book1.setId(book.getId());
                book1.setPublishTime(book.getPublishTime());
                book1.setPublish(book.getPublish());
                book1.setImageUrl(book.getImageUrl());
                book1.setType(book.getType());
                book1.setName(book.getName());
                book1.setAuthor(book.getAuthor());
                book1.setIntroduction(book.getIntroduction());
                book1.setISBN(book.getISBN());
                bookList1.add(book1);
            }
        }
        Page<Book> bookPage = listConvertToPage1(bookList1, toltalElements, pageable);
        return bookPage;
    }

    //搜索图书资源
    @ResponseBody
    @GetMapping("/findBooksByKeyWords/{pageNum}/{pageSize}/{bookKeysWords}")
    public Page<Book> findBooksByKeyWords(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize,
                                    @PathVariable("bookKeysWords") String bookKeysWords){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Book> bookList =  bookRepository.findBooksByKeyWords(bookKeysWords);
        int toltalElements = bookRepository.findBooksByKeyWordsCount(bookKeysWords);
        List<Book> bookList1 = new ArrayList<>();
        if(bookList != null && !bookList.isEmpty()){
            for(Book book : bookList){
                Book book1 = new Book();
                book1.setId(book.getId());
                book1.setPublishTime(book.getPublishTime());
                book1.setPublish(book.getPublish());
                book1.setImageUrl(book.getImageUrl());
                book1.setType(book.getType());
                book1.setName(book.getName());
                book1.setAuthor(book.getAuthor());
                book1.setIntroduction(book.getIntroduction());
                book1.setISBN(book.getISBN());
                bookList1.add(book1);
            }
        }
        Page<Book> bookPage = listConvertToPage1(bookList1, toltalElements, pageable);
        return bookPage;
    }

    @ResponseBody
    @GetMapping("/findAllType")
    public List<String> findAllType(){
        return bookRepository.findAllType();
    }

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
                                                @RequestBody BookDTO bookDTO){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        String name = bookDTO.getName();
        String publish = bookDTO.getPublish();
        String type = bookDTO.getType();
        String addTimeStart = bookDTO.getAddTimeStart();
        String addTimeEnd = bookDTO.getAddTimeEnd();
        String publishTimeStart = bookDTO.getPublishTimeStart();
        String publishTimeEnd = bookDTO.getPublishTimeEnd();
        System.out.println("name="+name+" publish="+publish+" type="+type+" addTimeStart="+addTimeStart+" addTimeEnd="+addTimeEnd+" publishTimeStart="+publishTimeStart+" publishTimeEnd="+publishTimeEnd);
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
    public Book findBookById(@PathVariable("id") int id){
        return bookRepository.findBookById(id);
    }

    @ResponseBody
    @PostMapping("/editBookById/{id}")
    public MyResult editBookById(@PathVariable("id") int id,
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
