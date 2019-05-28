package com.tools.java;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BuilderPattern{
    public static void main(String[] args) {
        Student student = new Student.Builder(11, "lee")
                .age(18)
                .sex("男")
                .builder();
        log.info(student.toString());
    }
}

/**
 * 构建器模式（Builder模式）：
 *
 *
 */

class Student {

    private final int id;
    private final String name;

    //可选参数
    private final int age;
    private final String sex;
    private final String add;
    private final String phone;

    private Student(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
        this.sex = builder.sex;
        this.add = builder.add;
        this.phone = builder.phone;
    }

    public static class Builder{
        //必须参数
        private final int id;
        private final String name;

        //可选参数
        private int age;
        private String sex;
        private String add;
        private String phone;

        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder sex(String val){
            this.sex = val;
            return this;
        }

        public Builder add(String val){
            this.add = val;
            return this;
        }

        public Builder age(int val){
            this.age = val;
            return this;
        }

        public Builder phone(String val){
            this.phone = val;
            return this;
        }

        public Student builder(){
            return new Student(this);
        }
    }

    @Override
    public String toString() {
        return this.id+"-"+this.name+"-"+this.age+"-"+this.add+"-"+this.phone+"-"+this.sex;
    }
}
