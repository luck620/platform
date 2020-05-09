package com.plantform.repository;

import com.plantform.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface AccountRepository extends JpaRepository<Account,Integer> {
    @Query(nativeQuery = true, value = "select a.* from account a where a.name=?1 and a.password=?2")
    Account getAccountBy(String name, String password);

    @Query(nativeQuery = true, value = "select a.* from account a " +
            "where case when ?1='' Then 1=1 ELSE a.name like %?1% END " +
            "and CASE WHEN ?2='' THEN 1=1 ELSE a.real_name like %?2% END " +
            "and CASE WHEN ?3='' THEN 1=1 ELSE a.phone like %?3% END ")
    Page<Account> findAllByOthers(String name, String realName, String phone, Pageable pageable);

    @Query(nativeQuery = true, value = "select a.* from account a where a.id = ?1")
    Account findAccountById(int id);

    @Query(nativeQuery = true, value = "update account a set a.name=?1,a.password=?2,a.real_name=?3,a.phone=?4,a.commit=?5 where a.id=?6 ")
    @Modifying
    @Transactional
    int update(String name,String password,String realName,String phone,String commit,int id);
}
