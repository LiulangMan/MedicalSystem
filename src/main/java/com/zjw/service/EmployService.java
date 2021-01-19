package com.zjw.service;

import com.zjw.domain.Employ;

import java.util.List;

public interface EmployService {
    void insert(Employ employ);

    void deleteById(Integer id);

    void update(Employ employ);

    Employ selectByIdForOne(Integer id);

    List<Employ> queryAll();

    Employ queryByLoginNameForOne(String username);

    void deleteByUserName(String userName);

    List<Employ> queryAllByName(String name);

    List<Employ> queryAllByAddress(String address);
}
