package test.model;

import java.util.Date;
import java.util.List;

import au.com.javacloud.jcframe.annotation.DisplayOrder;
import au.com.javacloud.jcframe.annotation.DisplayValueColumn;
import au.com.javacloud.jcframe.model.BaseBean;

/**
 * Created by david on 26/06/16.
 */
@DisplayValueColumn("name")
@DisplayOrder("name,testBean,oBoolean")
public class TestBean extends BaseBean<Integer> {
    private String name;
    private int pInt;
    private Integer oInt;
    private boolean pBoolean;
    private Boolean oBoolean;
    private Date date;
    private float pFloat;
    private Float oFloat;
    private double pDouble;
    private Double oDouble;
    private TestBean testBean;
    private List<TestBean> testBeanList;
    private List<String> stringList;
    private List<Integer> intList;
    private List<Double> doubleList;
    private String noGetter;
    private String noSetter;
    private String noGetterSetter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getpInt() {
        return pInt;
    }

    public void setpInt(int pInt) {
        this.pInt = pInt;
    }

    public Integer getoInt() {
        return oInt;
    }

    public void setoInt(Integer oInt) {
        this.oInt = oInt;
    }

    public boolean ispBoolean() {
        return pBoolean;
    }

    public void setpBoolean(boolean pBoolean) {
        this.pBoolean = pBoolean;
    }

    public Boolean getoBoolean() {
        return oBoolean;
    }

    public void setoBoolean(Boolean oBoolean) {
        this.oBoolean = oBoolean;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getpFloat() {
        return pFloat;
    }

    public void setpFloat(float pFloat) {
        this.pFloat = pFloat;
    }

    public Float getoFloat() {
        return oFloat;
    }

    public void setoFloat(Float oFloat) {
        this.oFloat = oFloat;
    }

    public double getpDouble() {
        return pDouble;
    }

    public void setpDouble(double pDouble) {
        this.pDouble = pDouble;
    }

    public Double getoDouble() {
        return oDouble;
    }

    public void setoDouble(Double oDouble) {
        this.oDouble = oDouble;
    }

    public TestBean getTestBean() {
        return testBean;
    }

    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }

    public List<TestBean> getTestBeanList() {
        return testBeanList;
    }

    public void setTestBeanList(List<TestBean> testBeanList) {
        this.testBeanList = testBeanList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Integer> getIntList() {
        return intList;
    }

    public void setIntList(List<Integer> intList) {
        this.intList = intList;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }

    public void setNoGetter(String noGetter) {
        this.noGetter = noGetter;
    }

    public String getNoSetter() {
        return noSetter;
    }
}
