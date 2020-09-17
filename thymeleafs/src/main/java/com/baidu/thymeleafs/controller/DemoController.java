package com.baidu.thymeleafs.controller;

import com.baidu.thymeleafs.entity.DemoEntity;
import com.baidu.thymeleafs.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @ClassName DemoController
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/14
 * @Version V1.09999999999999999
 **/
@Controller
public class DemoController {



    @GetMapping("/test1")
    public String test1(ModelMap map){

        map.put("name","崔康普");
        map.put("arr", Arrays.asList("2","3","4","5"));


        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setHobby("1,2");
        demoEntity.setId(1);
        demoEntity.setName("title");
        demoEntity.setType(1);

        map.addAttribute("demo",demoEntity);
        return "test";
    }

    @GetMapping("/stu")
    public String student(ModelMap map){
        Student student=new Student();
        student.setCode("007");
        student.setPass("9527");
        student.setAge(18);
        student.setLikeColor("<font color='red'>红色</font>");
        map.addAttribute("stu",student);
        return "student";
    }


    @GetMapping("list")
    public String list(ModelMap map){
        Student s1=new Student("001","111",18,"red");
        Student s2=new Student("002","222",19,"red");
        Student s3=new Student("003","333",16,"blue");
        Student s4=new Student("004","444",28,"blue");
        Student s5=new Student("005","555",68,"blue");

        //转为List
        map.addAttribute("stuList", Arrays.asList(s1,s2,s3,s4,s5));
        return "list";
    }
}
