package com.example.membersedgeservice;


import com.example.membersedgeservice.config.JwtTokenUtil;
import com.example.membersedgeservice.model.Comment;
import com.example.membersedgeservice.model.Image;
import com.example.membersedgeservice.model.ImageLike;
import com.example.membersedgeservice.model.ImgBoardUser;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerUnitTest {

    @Value("${imageservice.baseurl}")
    private String imageServiceBaseurl;


    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;
    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;
    @Value("${likeservice.baseurl}")
    private String likeServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;
    //login AUTHENTICATION
    private String token;
    ImgBoardUser user;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();
    /*IMAGES*/
    private Image image1 = new Image("AB.png","gust@gmail.com","hond","ABC123");
    private Image image2 = new Image("ABC.png","you@gmail.com","kat","ABC1234");
    private Image image3 = new Image("ABCD.png","me@gmail.com","konijn","ABC1235");
    private Image image4 = new Image("ABCDE.png","you@gmail.com","vis","ABC1236");

    private List<Image> allImageFromEmailYou = Arrays.asList(image2, image4);

    @BeforeEach
    public void initializeMockserver() throws Exception{
        mockServer = MockRestServiceServer.createServer(restTemplate);

        /*Set Images keys*/
        image1.setKey("123A");
        image2.setKey("123B");
        image3.setKey("123C");
        image4.setKey("123D");

        //login AUTHENTICATION
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        user = new ImgBoardUser(
                "testF",
                "testL",
                "test@hotmail.com",
                "test2"
        );
        token = jwtTokenUtil.generateToken(new User(user.getEmail(), user.getPassword(),
                new ArrayList<>()));

        mockServer.expect(ExpectedCount.manyTimes(),
                requestTo(new URI("http://" + userServiceBaseUrl + "/user/"+user.getEmail())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(user))
                );


    }
    @Test
    public void whenGetImagesByUserEmail_thenReturnAllImagesJson() throws Exception{


        // GET all Images from User 1
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + imageServiceBaseurl + "/images/user/you@gmail.com")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allImageFromEmailYou))
                );

        mockMvc.perform(get("/images/user/{useremail}", "you@gmail.com").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].source",is("ABC.png")))
                .andExpect(jsonPath("$[0].description",is("kat")))
                .andExpect(jsonPath("$[0].userEmail",is("you@gmail.com")))
                .andExpect(jsonPath("$[1].source",is("ABCDE.png")))
                .andExpect(jsonPath("$[1].description",is("vis")))
                .andExpect(jsonPath("$[1].userEmail",is("you@gmail.com")));
    }
    @Test
    public void whenAddImages_thenReturnImageJson() throws Exception {

        Image newImage1 = new Image("POST.png","post@gmail.com","post");

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + imageServiceBaseurl + "/images")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newImage1))
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

        mockMvc.perform(post("/images").header("Authorization", "Bearer " + token)
                .param("source", newImage1.getSource())
                .param("description", newImage1.getDescription())
                .param("userEmail", newImage1.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source",is("POST.png")))
                .andExpect(jsonPath("$.description",is("post")))
                .andExpect(jsonPath("$.userEmail",is("post@gmail.com")));

    }
    @Test
    public void whenUpdateImage_thenReturnImageJson() throws Exception {
        Image newImage = new Image("test.png","test@gmail.com","test", "ABC123");

        Image updateImage = new Image("uptest.png","uptest@gmail.com","uptest", "ABC123");



        // PUT comment from key
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + imageServiceBaseurl + "/images")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updateImage))
                );

        mockMvc.perform(put("/images").header("Authorization", "Bearer " + token)
                .param("source", updateImage.getSource())
                .param("userEmail", updateImage.getUserEmail())
                .param("description", updateImage.getDescription())
                .param("key", updateImage.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source",is("uptest.png")))
                .andExpect(jsonPath("$.description",is("uptest")))
                .andExpect(jsonPath("$.userEmail",is("uptest@gmail.com")));

    }
    @Test
    public void whenDeleteImage_thenDeleteAllAndReturnStatusOk() throws Exception {
        Image image1= new Image("testSource", "r0703028@student.thomasmore.be", "test", "ABC123");
        List<Image> imageList= new ArrayList<Image>();
        imageList.add(image1);

        Comment comment1 = new Comment("Comment1","Dat is mooi.", "com1@hotmail.com","ABC123", "com123A");
        List<Comment> commentList= new ArrayList<Comment>();
        commentList.add(comment1);

        ImageLike like1 = new ImageLike(true, "r0703028@student.thomasmore.be", "ABC123");
        List<ImageLike> likesList= new ArrayList<ImageLike>();
        likesList.add(like1);


        mockServer.expect(ExpectedCount.manyTimes(),
                requestTo(new URI("http://" + imageServiceBaseurl + "/images/"+image1.getKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(image1))
                );
        //comment
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments/images/"+image1.getKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(commentList))
                );
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + commentServiceBaseUrl + "/comments/"+comment1.getKey())))//
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));
        //end comment
        //like
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/"+image1.getKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(likesList))
                );
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/"+like1.getLikeKey())))//
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));
        //end like



        // DELETE image key com123A
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + imageServiceBaseurl + "/images/ABC123")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/images/{key}", "ABC123").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
