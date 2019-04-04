package com.xak.pojo;

/**
 * writer: xiaankang
 * date: 2019/3/19.
 */
public class Emp {
    private int empId;
    private String empName;
    private int salary;

    public void setEmpId(int id){
        this.empId=id;
    }
    public int getEmpId(){
        return this.empId;
    }

    public void setEmpName(String name){
        this.empName=name;
    }
    public String getEmpName(){
        return this.empName;
    }

    public void setSalary(int sal){
        this.salary=sal;
    }
    public int getSalary(){
        return this.salary;
    }

}
