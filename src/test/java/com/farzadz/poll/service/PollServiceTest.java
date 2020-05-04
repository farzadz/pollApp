//package com.farzadz.poll.service;
//
////import com.farzadz.poll.TestJPAConfig;
//
//import com.farzadz.poll.dataentry.dao.QuestionDAO;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
////@TestPropertySource(locations = "/foo.properties")
////
////@ContextConfiguration(classes = { TestJPAConfig.class }, loader = AnnotationConfigContextLoader.class)
//public class PollServiceTest {
//
//  @Autowired
//  private QuestionDAO questionDAO;
//
//  //  @BeforeClass
//  //  public void setup() {
//  //
//  //  }
//
//  @org.junit.Test
//  public void contextLoads() {
//    questionDAO.findAll().forEach(question -> {
//      System.out.println(question.getQuestionText());
//    });
//  }
//
//}