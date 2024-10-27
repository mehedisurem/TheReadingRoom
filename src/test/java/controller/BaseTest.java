//package controller;
//
//import model.Model;
//import model.User;
//import org.junit.jupiter.api.BeforeEach;
//
//public class BaseTest {
//    protected Model model;
//    protected User testUser;
//
//    @BeforeEach
//    void setUp() {
//        model = new Model();
//        try {
//            model.setup(); // Initialize database
//            testUser = new User("testUser", "password", "Test", "User");
//            model.setCurrentUser(testUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}