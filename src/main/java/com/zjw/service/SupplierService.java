package com.zjw.service;

import com.zjw.domain.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> queryAll();

    int queryMaxId();

    boolean checkIdOrNameInDataBase(int idText, String nameText);

    void insert(Supplier supplierVar);

    void update(Supplier supplier);

    Supplier queryOneById(Integer id);

    void deleteById(Integer id);
}
