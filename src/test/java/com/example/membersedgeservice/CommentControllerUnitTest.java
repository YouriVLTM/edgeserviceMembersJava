package com.example.membersedgeservice;

import com.example.membersedgeservice.model.Comment;
import com.example.membersedgeservice.model.Image;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerUnitTest {

    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private Comment comment1 = new Comment(
            "Comment1",
            "Dat is mooi.",
            "com1@hotmail.com",
            "123A"
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
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        /*Set Images keys*/
        image1.setKey("123A");
        image2.setKey("123B");
        image3.setKey("123C");
        image4.setKey("123D");
    }

    @Test
    public void whenGetCommentsByImagesKey_thenReturnAllCommentsJson() throws Exception{



        // GET all reviews from User 1
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments/images/123A")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allcommentFromImage123A))
                );

        mockMvc.perform(get("/comments/images/{imagekey}", "123A"))
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

        Comment newComment1 = new Comment(
                "Comment1",
                "Dat is mooi.",
                "com1@hotmail.com",
                "123A"
        );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newComment1))
                );

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

        mockMvc.perform(post("/comments")
                .param("userEmail", newComment1.getUserEmail())
                .param("imageKey", newComment1.getImageKey())
                .param("title", newComment1.getTitle())
                .param("description", newComment1.getDescription())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",is("Comment1")))
                .andExpect(jsonPath("$.description",is("Dat is mooi.")))
                .andExpect(jsonPath("$.user.userEmail",is("com1@hotmail.com")))
                .andExpect(jsonPath("$.image.key",is("123A")));

    }

}
