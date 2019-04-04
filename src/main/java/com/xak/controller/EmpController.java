package com.xak.controller;

import com.xak.pojo.Emp;
import com.xak.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * writer: xiaankang
 * date: 2019/3/19.
 */
@Controller
@RequestMapping("/emp")
public class EmpController {


    @Autowired
    private EmpService empService;

    @RequestMapping(value = "/add.do",method = RequestMethod.POST)
    public ModelAndView addEmpRequest(HttpServletRequest request, HttpServletResponse response){
        String name=request.getParameter("name");
        String salary=request.getParameter("salary");

        Emp emp=new Emp();
        emp.setEmpName(name);
        emp.setSalary(Integer.parseInt(salary));

        empService.addEmp(emp);

        return new ModelAndView("welcom");
    }

    @RequestMapping("/getJson.do")
    @ResponseBody
    public Emp getJson(){
        Emp emp=new Emp();
        emp.setEmpId(123);
        emp.setEmpName("libai");
        emp.setSalary(2222);
        return emp;
    }
    @RequestMapping("/getJson1.do")
    @ResponseBody
    public String getJson1(){
        return "hello json";
    }


}
