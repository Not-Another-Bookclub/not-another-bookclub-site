//package com.codeup.springboot_blog;
//
//import com.codeup.springboot_blog.daos.PostRepository;
//import com.codeup.springboot_blog.daos.UserRepository;
//import com.codeup.springboot_blog.models.User;
//import com.codeup.springboot_blog.models.Post;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import javax.servlet.http.HttpSession;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.junit.Assert.assertNotNull;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringbootBlogApplication.class)
//@AutoConfigureMockMvc
//public class PostsIntegrationTests {
//
//    private User testUser;
//    private HttpSession httpSession;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    UserRepository userDao;
//
//    @Autowired
//    PostRepository postDao;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Before
//    public void setup() throws Exception {
//
//       User testUser = userDao.findByUsername("testUser");
//
//        // Creates the test user if not exists
//        if(testUser == null){
//            User newUser = new User();
//            newUser.setUsername("testUser");
//            newUser.setPassword(passwordEncoder.encode("pass"));
//            newUser.setEmail("testUser@codeup.com");
//            testUser = userDao.save(newUser);
//        }
//
//        // Throws a Post request to /login and expect a redirection to the Ads index page after being logged in
//        httpSession = this.mvc.perform(post("/login").with(csrf())
//                .param("username", "testUser")
//                .param("password", "pass"))
//                .andExpect(status().is(HttpStatus.FOUND.value()))
//                .andExpect(redirectedUrl("/posts"))
//                .andReturn()
//                .getRequest()
//                .getSession();
//    }
//
//    @Test
//    public void contextLoads() {
//        // Sanity Test, just to make sure the MVC bean is working
//        assertNotNull(mvc);
//    }
//
//    @Test
//    public void testIfUserSessionIsActive() throws Exception {
//        // It makes sure the returned session is not null
//        assertNotNull(httpSession);
//    }
//
//    @Test
//    public void testCreateAd() throws Exception {
//        // Makes a Post request to /ads/create and expect a redirection to the Ad
//        this.mvc.perform(
//                post("/posts/create").with(csrf())
//                        .session((MockHttpSession) httpSession)
//                        // Add all the required parameters to your request like this
//                        .param("title", "test")
//                        .param("body", "for sale"))
//                .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    public void testShowAd() throws Exception {
//
//        Post existingAd = postDao.findAll().get(0);
//        System.out.println("existingAd.getTitle() = " + existingAd.getTitle());
//
//        // Makes a Get request to /ads/{id} and expect a redirection to the Ad show page
//        this.mvc.perform(get("/posts/" + existingAd.getId()))
//                .andExpect(status().isOk())
//                // Test the dynamic content of the page
//                .andExpect(content().string(containsString(existingAd.getTitle())));
//    }
//
//    @Test
//    public void testAdsIndex() throws Exception {
//        Post existingAd = postDao.findAll().get(0);
//
//        // Makes a Get request to /ads and verifies that we get some of the static text of the ads/index.html template and at least the title from the first Ad is present in the template.
//        this.mvc.perform(get("/posts"))
//                .andExpect(status().isOk())
//                // Test the static content of the page
//                .andExpect(content().string(containsString("Here's a list of all posts")))
//                // Test the dynamic content of the page
//                .andExpect(content().string(containsString(existingAd.getTitle())));
//    }
//
//    @Test
//    public void testEditAd() throws Exception {
//        // Gets the first Ad for tests purposes
//        Post existingAd = postDao.findPostsByTitle("test").get(0);
//
//        // Makes a Post request to /ads/{id}/edit and expect a redirection to the Ad show page
//        this.mvc.perform(
//                post("/posts/" + existingAd.getId() + "/edit").with(csrf())
//                        .session((MockHttpSession) httpSession)
//                        .param("title", "edited title")
//                        .param("body", "edited description"))
//                .andExpect(status().is3xxRedirection());
//
//        // Makes a GET request to /ads/{id} and expect a redirection to the Ad show page
//        this.mvc.perform(get("/posts/" + existingAd.getId()))
//                .andExpect(status().isOk())
//                // Test the dynamic content of the page
//                .andExpect(content().string(containsString("edited title")))
//                .andExpect(content().string(containsString("edited description")));
//    }
//
//    @Test
//    public void testDeleteAd() throws Exception {
//        // Creates a test Ad to be deleted
//        this.mvc.perform(
//                post("/posts/create").with(csrf())
//                        .session((MockHttpSession) httpSession)
//                        .param("title", "ad to be deleted")
//                        .param("body", "won't last long"))
//                .andExpect(status().is3xxRedirection());
//
//        // Get the recent Ad that matches the title
//        Post existingAd = postDao.findPostsByTitle("ad to be deleted").get(0);
//        System.out.println("existingAd.getTitle() = " + existingAd.getTitle());
//        // Makes a Post request to /ads/{id}/delete and expect a redirection to the Ads index
//        this.mvc.perform(
//                post("/posts/delete").with(csrf())
//                        .session((MockHttpSession) httpSession)
//                        .param("id", String.valueOf(existingAd.getId())))
//                .andExpect(status().is3xxRedirection());
//    }
//
//}