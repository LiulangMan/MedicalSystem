package com.zjw.service.impl;

import com.zjw.domain.Supplier;
import com.zjw.mapper.SupplierMapper;
import com.zjw.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/16 18:29
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper mapper;

    @Override
    public List<Supplier> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public int queryMaxId() {
        return mapper.selectMaxId();
    }

    @Override
    public boolean checkIdOrNameInDataBase(int idText, String nameText) {
        return mapper.selectByPrimaryKey(idText) != null || mapper.selectOneByName(nameText) != null;
    }

    @Override
    public void insert(Supplier supplierVar) {
        mapper.insert(supplierVar);
    }

    @Override
    public void update(Supplier supplier) {
        mapper.updateByPrimaryKey(supplier);
    }

    @Override
    public Supplier queryOneById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

}
