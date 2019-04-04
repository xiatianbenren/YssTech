package com.xak.service.impl;

import com.xak.dao.EmpDao;
import com.xak.pojo.Emp;
import com.xak.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * writer: xiaankang
 * date: 2019/3/19.
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpDao empDao;

    @Override
    public void addEmp(Emp emp) {
        empDao.add(emp);
    }
}
