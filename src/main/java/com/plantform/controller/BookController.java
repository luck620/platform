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
        List<String> typeList = bookRepository.findAllType();
        List<String> typeList1 = new ArrayList<>();
        if(typeList.size() > 0){
            for(String type : typeList){
                if(type.equals("marxism")){
                    typeList1.add("马克思主义基本原理概念");
                }
                if(type.equals("maoZD")){
                    typeList1.add("毛泽东思想和中国特色社会主义理论体系");
                }
                if(type.equals("moral")){
                    typeList1.add("思想道德修养与法律基础");
                }
                if(type.equals("history")){
                    typeList1.add("中国近代史纲要");
                }
            }
        }
        return typeList1;
    }

    @ResponseBody
    @GetMapping("/getBookList/{pageNum}/{pageSize}")
    public Page<Book> getBookList(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Book> bookList = bookRepository.findAll();
        int totalElements = bookRepository.findAllCount();
        List<Book> bookList1 = new ArrayList<>();
        if(bookList.size()>0){
            for(Book book : bookList){
                Book book1 =  new Book();
                book1.setId(book.getId());
                book1.setIntroduction(book.getIntroduction());
                book1.setAuthor(book.getAuthor());
                book1.setName(book.getName());
                if(book.getType().equals("marxism")){
                    book1.setType("马克思主义基本原理概念");
                }
                if(book.getType().equals("maoZD")){
                    book1.setType("毛泽东思想和中国特色社会主义理论体系");
                }
                if(book.getType().equals("moral")){
                    book1.setType("思想道德修养与法律基础");
                }
                if(book.getType().equals("history")){
                    book1.setType("中国近代史纲要");
                }
                book1.setImageUrl(book.getImageUrl());
                book1.setPublish(book.getPublish());
                book1.setPublishTime(book.getPublishTime());
                book1.setISBN(book.getISBN());
                bookList1.add(book1);
            }
        }
        Page<Book> bookPage = listConvertToPage1(bookList1, totalElements, pageable);
        return bookPage;
    }

    //模糊查询
    @ResponseBody
    @PostMapping("/getBookListByOthers/{pageNum}/{pageSize}")
    public Page<Book> getBookListByOthers(@PathVariable("pageNum") Integer pageNum,
                                                @PathVariable("pageSize") Integer pageSize,
                                                @RequestBody BookDTO bookDTO){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        System.out.println("name="+bookDTO.getName()+" publish="+bookDTO.getPublish()+" type="+bookDTO.getType()
                +" author="+bookDTO.getAuthor()+" ISBN="+bookDTO.getIsbn()+" publishTimeStart="+bookDTO.getPublishTimeStart()+" publishTimeEnd="+bookDTO.getPublishTimeEnd());
        String type = "";
        if(bookDTO.getType().equals("马克思主义基本原理概念")){
            type = "marxism";
        }
        if(bookDTO.getType().equals("毛泽东思想和中国特色社会主义理论体系")){
            type = "maoZD";
        }
        if(bookDTO.getType().equals("思想道德修养与法律基础")){
            type = "moral";
        }if(bookDTO.getType().equals("中国近代史纲要")){
            type = "history";
        }

        List<Book> bookList = bookRepository.findAllByOthers(bookDTO.getName(),bookDTO.getPublish(),type,bookDTO.getAuthor(),bookDTO.getIsbn(),bookDTO.getPublishTimeStart(),bookDTO.getPublishTimeEnd());
        int totalElements = bookRepository.findAllByOthersCount(bookDTO.getName(),bookDTO.getPublish(),type,bookDTO.getAuthor(),bookDTO.getIsbn(),bookDTO.getPublishTimeStart(),bookDTO.getPublishTimeEnd());
        List<Book> bookList1 = new ArrayList<>();
        if(bookList.size()>0){
            for(Book book : bookList){
                Book book1 =  new Book();
                book1.setId(book.getId());
                book1.setIntroduction(book.getIntroduction());
                book1.setAuthor(book.getAuthor());
                book1.setName(book.getName());
                if(book.getType().equals("marxism")){
                    book1.setType("马克思主义基本原理概念");
                }
                if(book.getType().equals("maoZD")){
                    book1.setType("毛泽东思想和中国特色社会主义理论体系");
                }
                if(book.getType().equals("moral")){
                    book1.setType("思想道德修养与法律基础");
                }
                if(book.getType().equals("history")){
                    book1.setType("中国近代史纲要");
                }
                book1.setImageUrl(book.getImageUrl());
                book1.setPublish(book.getPublish());
                book1.setPublishTime(book.getPublishTime());
                book1.setISBN(book.getISBN());
                bookList1.add(book1);
            }
        }
        Page<Book> bookPage = listConvertToPage1(bookList1, totalElements, pageable);
        return bookPage;
    }

    @ResponseBody
    @PostMapping("/addBook")
    public MyResult addBook(@RequestBody BookDTO bookDTO){
        MyResult myResult = new MyResult();
        System.out.println(bookDTO.getPublishTime());
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setPublish(bookDTO.getPublish());
        if(bookDTO.getType().equals("马克思主义基本原理概念")){
            book.setType("marxism");
        }
        if(bookDTO.getType().equals("毛泽东思想和中国特色社会主义理论体系")){
            book.setType("maoZD");
        }
        if(bookDTO.getType().equals("思想道德修养与法律基础")){
            book.setType("moral");
        }if(bookDTO.getType().equals("中国近代史纲要")){
            book.setType("history");
        }
        book.setAddTime(LocalDateTime.now());
        book.setPublishTime(LocalDateTime.parse(bookDTO.getPublishTime(),df));
        book.setISBN(bookDTO.getIsbn());
        book.setAuthor(bookDTO.getAuthor());
        book.setIntroduction(bookDTO.getIntroduction());
        book.setImageUrl("http://qaath1lbd.bkt.clouddn.com/"+bookDTO.getImageUrl());
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
        Book book = bookRepository.findBookById(id);
        Book book1 = new Book();
        if(book != null){
                book1.setId(book.getId());
                book1.setIntroduction(book.getIntroduction());
                book1.setAuthor(book.getAuthor());
                book1.setName(book.getName());
                if(book.getType().equals("marxism")){
                    book1.setType("马克思主义基本原理概念");
                }
                if(book.getType().equals("maoZD")){
                    book1.setType("毛泽东思想和中国特色社会主义理论体系");
                }
                if(book.getType().equals("moral")){
                    book1.setType("思想道德修养与法律基础");
                }
                if(book.getType().equals("history")){
                    book1.setType("中国近代史纲要");
                }
                book1.setImageUrl(book.getImageUrl());
                book1.setPublish(book.getPublish());
                book1.setPublishTime(book.getPublishTime());
                book1.setISBN(book.getISBN());
            }
        return book1;
    }

    @ResponseBody
    @PostMapping("/editBookById/{id}")
    public MyResult editBookById(@PathVariable("id") int id,
                                    @RequestBody Book editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" type="+editForm.getType()+" publish="+editForm.getPublish()+" addTime="+editForm.getAddTime()+" publishTime="+editForm.getPublishTime());
        String imageUrl = "http://qaath1lbd.bkt.clouddn.com/"+editForm.getImageUrl();
        String type = "";
        if(editForm.getType().equals("马克思主义基本原理概念")){
            type = "marxism";
        }
        if(editForm.getType().equals("毛泽东思想和中国特色社会主义理论体系")){
            type = "maoZD";
        }
        if(editForm.getType().equals("思想道德修养与法律基础")){
            type = "moral";
        }if(editForm.getType().equals("中国近代史纲要")){
            type = "history";
        }
        int result = bookRepository.update(editForm.getName(),editForm.getPublish(),type,editForm.getAuthor(),editForm.getISBN(), editForm.getIntroduction(),editForm.getPublishTime().toString(),imageUrl,id);
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
