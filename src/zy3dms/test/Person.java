package zy3dms.test;

public class Person {

	private String name;  
    private int num;  
    private String sex;  
    private int age;  
      
    public Person() {  
        // TODO Auto-generated constructor stub  
    }  
  
    public Person(String name, int num, String sex, int age) {  
        super();  
        this.name = name;  
        this.num = num;  
        this.sex = sex;  
        this.age = age;  
    }  
  
  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public int getNum() {  
        return num;  
    }  
  
    public void setNum(int num) {  
        this.num = num;  
    }  
  
    public String getSex() {  
        return sex;  
    }  
  
    public void setSex(String sex) {  
        this.sex = sex;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }

}
