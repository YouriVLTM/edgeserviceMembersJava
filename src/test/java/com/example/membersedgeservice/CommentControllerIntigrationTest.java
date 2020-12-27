package com.example.membersedgeservice;

import com.example.membersedgeservice.config.JwtTokenUtil;
import com.example.membersedgeservice.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerIntigrationTest {

    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;


    @Autowired
    private MockMvc mockMvc;

    //login AUTHENTICATION
    private String token;
    ImgBoardUser user;

//    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private Comment comment1 = new Comment(
            "Comment1",
            "Dat is mooi.",
            "com1@hotmail.com",
            "123A",
            "com123A"

    );

    private Comment comment2 = new Comment(
            "Comment2",
            "Dat is speciaal.",
            "com2@hotmail.com",
            "123B"
    );

    private Comment comment3 = new Comment(
            "Comment3",
            "Dat is speciaal.",
            "com3@hotmail.com",
            "123A"
    );

    private Comment comment4 = new Comment(
            "Comment4",
            "Dat is speciaal.",
            "com4@hotmail.com",
            "123B"
    );

    /*IMAGES*/
    private Image image1 = new Image("AB.png","gust@gmail.com","hond");
    private Image image2 = new Image("ABC.png","you@gmail.com","kat");
    private Image image3 = new Image("ABCD.png","me@gmail.com","konijn");
    private Image image4 = new Image("ABCDE.png","you@gmail.com","vis");


    private List<Comment> allcommentFromImage123A = Arrays.asList(comment1, comment3);
    private List<Comment> allcommentFromImage123B = Arrays.asList(comment2, comment4);


    @BeforeEach
    public void initializeMockserver() throws Exception{

        /*Set Images keys*/
        image1.setKey("123A");
        image2.setKey("123B");
        image3.setKey("123C");
        image4.setKey("123D");

//        //Registeren
//        ImgBoardUser user1 = new ImgBoardUser(
//                "testF",
//                "testL",
//                "test@hotmail.com",
//                "test"
//        );
//
//        MvcResult result = mockMvc.perform(post("/user")
//                .content(mapper.writeValueAsString(user1))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstname",is("testF")))
//                .andExpect(jsonPath("$.lastname",is("testL")))
//                .andExpect(jsonPath("$.email",is("test@hotmail.com")))
//                .andReturn();
//
//        //login AUTHENTICATION
//        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//        user = new ImgBoardUser(
//                "testF",
//                "testL",
//                "test@hotmail.com",
//                "test2"
//        );
//        token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
//                new ArrayList<>()));
//
////        mockServer.expect(ExpectedCount.manyTimes(),
////                requestTo(new URI("http://" + userServiceBaseUrl + "/user/"+user.getEmail())))
////                .andExpect(method(HttpMethod.GET))
////                .andRespond(withStatus(HttpStatus.OK)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .body(mapper.writeValueAsString(user))
////                );

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


    }

    @Test
    public void whenGetCommentsByImagesKey_thenReturnAllCommentsJson() throws Exception{

        // GET all reviews from User 1
//        mockServer.expect(ExpectedCount.once(),
//                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments/images/123A")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(allcommentFromImage123A))
//                );

        mockMvc.perform(get("/comments/images/{imagekey}", "123A").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title",is("Comment1")))
                .andExpect(jsonPath("$[0].description",is("Dat is mooi.")))
                .andExpect(jsonPath("$[0].userEmail",is("com1@hotmail.com")))
                .andExpect(jsonPath("$[0].imageKey",is("123A")))
                .andExpect(jsonPath("$[1].title",is("Comment3")))
                .andExpect(jsonPath("$[1].description",is("Dat is speciaal.")))
                .andExpect(jsonPath("$[1].userEmail",is("com3@hotmail.com")))
                .andExpect(jsonPath("$[1].imageKey",is("123A")));
    }
    @Test
    public void whenAddComment_thenReturnFilledImageUserCommentJson() throws Exception {

        AddImageUserComment addImageUserComment = new AddImageUserComment(
                "0703028@student.thomasmore.be",
                "123",
                "ok",
                "ok"
        );

//        mockServer.expect(ExpectedCount.once(),
//                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments")))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(addImageUserComment))
//                );

       /* //TODO get images
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newComment1))
                );


        //TODO get users
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newComment1))
                );*/

        mockMvc.perform(post("/comments").header("Authorization", "Bearer " + token)
                .param("userEmail", "r0703028@student.thomasmore.be")
                .param("imageKey", "123")
                .param("title", "Comment1")
                .param("description", "Dat is mooi.")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",is("Comment1")))
                .andExpect(jsonPath("$.description",is("Dat is mooi.")))
                .andExpect(jsonPath("$.user.email",is("r0703028@student.thomasmore.be")))
                .andExpect(jsonPath("$.image.key",is("123")));

    }

    @Test
    public void whenUpdateComment_thenReturnFilledImageUserCommentJson() throws Exception {
        Comment newComment1 = new Comment(
                "Comment1",
                "Dat is mooi.",
                "com1@hotmail.com",
                "123A",
                "com123A"
        );

        Comment updateComment1 = new Comment(
                "upComment1",
                "upDat is mooi.",
                "com1@hotmail.com",
                "123A",
                "com123A"
        );

        // GET comment from key
//        mockServer.expect(ExpectedCount.once(),
//                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(newComment1))
//                );
//
//        // PUT comment from key
//        mockServer.expect(ExpectedCount.once(),
//                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments")))
//                .andExpect(method(HttpMethod.PUT))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(updateComment1))
//                );

        mockMvc.perform(put("/comments").header("Authorization", "Bearer " + token)
                .param("commentKey", updateComment1.getKey())
                .param("title", updateComment1.getTitle())
                .param("description", updateComment1.getDescription())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",is("upComment1")))
                .andExpect(jsonPath("$.description",is("upDat is mooi.")))
                .andExpect(jsonPath("$.user.email",is("com1@hotmail.com")))
                .andExpect(jsonPath("$.image.key",is("123A")));

    }

    @Test
    public void whenDeleteComment_thenReturnStatusOk() throws Exception {

        // DELETE comment key com123A
//        mockServer.expect(ExpectedCount.once(),
//                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments/com123A")))
//                .andExpect(method(HttpMethod.DELETE))
//                .andRespond(withStatus(HttpStatus.OK)
//                );

        mockMvc.perform(delete("/comments/{key}", "com123A").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
