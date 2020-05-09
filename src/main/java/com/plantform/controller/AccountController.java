package com.plantform.controller;

import com.plantform.entity.Account;
import com.plantform.entity.JavaWebToken;
import com.plantform.entity.MyResult;
import com.plantform.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    AccountRepository accountRepository;

    @ResponseBody
    @PostMapping("/login")
    public MyResult login(@RequestBody Account account){
        MyResult myResult = new MyResult();
        System.out.println(account.toString());
        Account account1 =  accountRepository.getAccountBy(account.getName(),account.getPassword());
        System.out.println(account1 == null);
        if(account1 != null ) {
            Map<String,Object> m = new HashMap<String,Object>();
            m.put("accountId",account1.getId());
            String token = JavaWebToken.createJavaWebToken(m);                // 根据存在用户的id生成token字符串
            myResult.setCode(200);
            myResult.setMsg("登陆成功");
            myResult.setToken(token);
            return myResult;
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/getAccountList/{pageNum}/{pageSize}")
    public Page<Account> getAccountList(@PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return accountRepository.findAll(pageable);
    }

    @ResponseBody
    @PostMapping("/getAccountListByOthers/{pageNum}/{pageSize}")
    public Page<Account> getAccountListByOthers(@PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize,
                                        @RequestBody Account account){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        String name = account.getName();
        String realName = account.getRealName();
        String phone = account.getPhone();
        if(name.isEmpty() || name == null){
            name = "";
        }
        if(realName.isEmpty() || realName == null){
            realName = "";
        }
        if(phone.isEmpty() || phone == null){
            phone = "";
        }
        System.out.println("name="+name+" realName="+realName+" phone="+phone);
        return accountRepository.findAllByOthers(name,realName,phone,pageable);
    }

    @ResponseBody
    @PostMapping("/addAccount")
    public MyResult addAccount(@RequestBody Account account){
        MyResult myResult = new MyResult();
        Account account1 = accountRepository.save(account);
        if(account1 != null){
            myResult.setCode(200);
            myResult.setMsg("添加成功");
        }
        return myResult;
    }

    @ResponseBody
    @GetMapping("/findAccountById/{id}")
    public Account findAccountById(@PathVariable("id") int id){
        return accountRepository.findAccountById(id);
    }

    @ResponseBody
    @PostMapping("/editAccountById/{id}")
    public MyResult editAccountById(@PathVariable("id") int id,
                                   @RequestBody Account editForm){
        System.out.println("id="+id+" name="+editForm.getName()+" password="+editForm.getPassword()+" realName="+editForm.getRealName()+" phone="+editForm.getPhone()+" commit="+editForm.getCommit());
        int result = accountRepository.update(editForm.getName(),editForm.getPassword(),editForm.getRealName(),editForm.getPhone(),editForm.getCommit(),id);
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
    @GetMapping("/deleteAccountById/{id}")
    public MyResult deleteAccountById(@PathVariable("id") int id){
        accountRepository.deleteById(id);
        MyResult myResult = new MyResult();
        myResult.setCode(200);
        myResult.setMsg("修改成功");
        return myResult;
    }
}
