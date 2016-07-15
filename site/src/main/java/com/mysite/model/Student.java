package com.mysite.model;

import au.com.jcloud.jcframe.annotation.DisplayOrder;
import au.com.jcloud.jcframe.annotation.DisplayValueColumn;
import au.com.jcloud.jcframe.model.BaseBean;

/**
 * Created by david on 22/05/16.
 */
@DisplayValueColumn("firstName")
@DisplayOrder("firstName,lastName,course,year")
public class Student extends BaseBean<Integer> {
    private String firstName;
    private String lastName;
    private String course;
    private int year;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
