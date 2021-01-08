package com.example.membersedgeservice.intigration;

import com.example.membersedgeservice.config.JwtTokenUtil;
import com.example.membersedgeservice.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntigrationTest {
        @Value("${userservice.baseurl}")
        private String userServiceBaseUrl;
    @Value("${imageservice.baseurl}")
    private String imageServiceBaseurl;
    @Value("${likeservice.baseurl}")
    private String likeServiceBaseUrl;

    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;
        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private MockMvc mockMvc;


        private String token;
        private ImageLike like1 = new ImageLike(true, "r0703028@student.thomasmore.be", "1");
        private Image image1= new Image("testSource", "r0703028@student.thomasmore.be", "test");
        private Comment comment1 = new Comment(
            "Comment1",
            "Dat is mooi.",
            "com1@hotmail.com",
            "123A",
            "com123A"

        );
        private ObjectMapper mapper = new ObjectMapper();
        @BeforeEach
        public void beforeAllTests() {
            ImgBoardUser user = new ImgBoardUser("Robin","Vranckx","r0703028@student.thomasmore.be","test") ;
            ImgBoardUser user1 = new ImgBoardUser("testF","testL","test@hotmail.com","test");

            try{
                ResponseEntity response = restTemplate.exchange("http://" + userServiceBaseUrl + "/user/"+user.getEmail(), HttpMethod.DELETE,null,
                        String.class);
            } catch (Exception e) {
            }
            try{

            ResponseEntity response2 =restTemplate.exchange("http://" + userServiceBaseUrl + "/user/"+user1.getEmail(), HttpMethod.DELETE,null,
                    String.class);
            } catch (Exception e) {
            }
            try{
                restTemplate.delete("http://" + imageServiceBaseurl + "/images/" + image1.getKey());

            } catch (Exception e) {
            }
            try{
                restTemplate.delete("http://" + likeServiceBaseUrl + "/likes/" + like1.getLikeKey());

            } catch (Exception e) {
            }
            try{
            user =restTemplate.postForObject("http://" + userServiceBaseUrl + "/user",
                    user,ImgBoardUser.class);
            } catch (Exception e) {
            }
            try{
            user1 =restTemplate.postForObject("http://" + userServiceBaseUrl + "/user",
                    user1,ImgBoardUser.class);
            } catch (Exception e) {
            }

        }
        @AfterEach
        public void afterAllTests() {
            //Watch out with deleteAll() methods when you have other data in the test database!
            ImgBoardUser user2 = new ImgBoardUser(
                    "testF2",
                    "testL2",
                    "test2@hotmail.com",
                    "test2"
            );
            try{
                ResponseEntity response = restTemplate.exchange("http://" + userServiceBaseUrl + "/user/"+user2.getEmail(), HttpMethod.DELETE,null,
                        String.class);
            }catch(Exception e){
            }
            try{
                restTemplate.delete("http://" + imageServiceBaseurl + "/images/" + image1.getKey());
                       }catch(Exception e){
    }
            try{
                restTemplate.delete("http://" + likeServiceBaseUrl + "/likes/" + like1.getLikeKey());
            }catch(Exception e){
            }
            try{
                restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + comment1.getKey());
            }catch(Exception e){
            }
        }

        @Test
        public void whenRegister_thenReturnUser() throws Exception {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            ImgBoardUser user1 = new ImgBoardUser(
                    "testF2",
                    "testL2",
                    "test2@hotmail.com",
                    "test2"
            );

            MvcResult result = mockMvc.perform(post("/user")
                    .content(mapper.writeValueAsString(user1))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstname",is("testF2")))
                    .andExpect(jsonPath("$.lastname",is("testL2")))
                    .andExpect(jsonPath("$.email",is("test2@hotmail.com")))
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            String encryptedPassword = JsonPath.parse(response).read("$.password").toString();
            assertTrue(encoder.matches("test2",encryptedPassword ));

        }
        @Test
        public void whenLogin_thenReturnToken() throws Exception {

            JwtRequest login1 = new JwtRequest(
                    "r0703028@student.thomasmore.be",
                    "test"
            );

            ImgBoardUser user = new ImgBoardUser("Robin","Vranckx","r0703028@student.thomasmore.be",new BCryptPasswordEncoder().encode("test") );


            MvcResult result = mockMvc.perform(post("/login")
                    .content(mapper.writeValueAsString(login1))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            token = JsonPath.parse(response).read("$.token").toString();
            assertFalse(token.isEmpty());
        }
        @Test
         public void whengetUser_thenReturnuser() throws Exception {
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
            ImgBoardUser user = new ImgBoardUser("Robin","Vranckx","r0703028@student.thomasmore.be","test2");
            //whenLogin_thenReturnToken();
            String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                    new ArrayList<>()));

            mockMvc.perform(get("/user/"+user.getEmail()).header("Authorization", "Bearer " + token)
                    .content(mapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstname",is("Robin")))
                    .andExpect(jsonPath("$.lastname",is("Vranckx")))
                    .andExpect(jsonPath("$.email",is("r0703028@student.thomasmore.be")))
                    .andExpect(jsonPath("$.password",is(IsNull.nullValue())));

        }
        @Test
        public void whenUpdatePassword_thenReturnUser() throws Exception {
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
            ImgBoardUser user = new ImgBoardUser(
                    "testF",
                    "testL",
                    "test@hotmail.com",
                    "test2"
            );                 //whenLogin_thenReturnToken();
            String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                    new ArrayList<>()));
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();



            MvcResult result=mockMvc.perform(put("/user").header("Authorization", "Bearer " + token)
                    .content(mapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstname", is("testF")))
                    .andExpect(jsonPath("$.lastname", is("testL")))
                    .andExpect(jsonPath("$.email",is("test@hotmail.com")))
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            String encryptedPassword = JsonPath.parse(response).read("$.password").toString();
            assertTrue(encoder.matches("test2",encryptedPassword ));
        }
    @Test
    public void whenUpdatePasswordFromOtherUSer_thenReturnNull() throws Exception {
        ImgBoardUser user1 = new ImgBoardUser("Robin","Vranckx","r0703028@student.thomasmore.be","test") ;

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        ImgBoardUser user = new ImgBoardUser(
                "testF",
                "testL",
                "test@hotmail.com",
                "test2"
        );                 //whenLogin_thenReturnToken();
        String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                new ArrayList<>()));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();



        MvcResult result=mockMvc.perform(put("/user").header("Authorization", "Bearer " + token)
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.equals(""));
    }
        @Test
        public void whenDeleteUser_thenReturnStatus() throws Exception {
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
            ImgBoardUser user = new ImgBoardUser(
                    "testF",
                    "testL",
                    "test@hotmail.com",
                    "test"
            );            //whenLogin_thenReturnToken();
            String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                    new ArrayList<>()));


            mockMvc.perform(delete("/user/"+user.getEmail()).header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());

        }
    @Test
    public void whenDeleteUser_thendeleteEverythingAndReturnStatus() throws Exception {
        try{
            comment1 = restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
                    comment1,Comment.class);
        } catch (Exception e) {
        }
        try{
            image1 = restTemplate.postForObject("http://" + imageServiceBaseurl + "/images",
                    image1,Image.class);
        } catch (Exception e) {
        }
        try{
            like1 = restTemplate.postForObject("http://" + likeServiceBaseUrl + "/likes",
                    like1,ImageLike.class);
        } catch (Exception e) {
        }

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        ImgBoardUser user = new ImgBoardUser(
                "testF",
                "testL",
                "test@hotmail.com",
                "test"
        );            //whenLogin_thenReturnToken();
        String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                new ArrayList<>()));

        mockMvc.perform(delete("/user/"+user.getEmail()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }
    @Test
    public void whenDeleteUserFromOtherUSer_thenReturn403() throws Exception {
        ImgBoardUser user1 = new ImgBoardUser("Robin","Vranckx","r0703028@student.thomasmore.be","test") ;
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        ImgBoardUser user = new ImgBoardUser(
                "testF",
                "testL",
                "test@hotmail.com",
                "test"
        );            //whenLogin_thenReturnToken();
        String token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                new ArrayList<>()));


        mockMvc.perform(delete("/user/"+user1.getEmail()).header("Authorization", "Bearer " + token))
                .andExpect(status().is(403));

    }
}
