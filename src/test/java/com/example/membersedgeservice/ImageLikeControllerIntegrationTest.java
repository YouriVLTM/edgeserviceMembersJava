package com.example.membersedgeservice;

import com.example.membersedgeservice.config.JwtTokenUtil;
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

@SpringBootTest
@AutoConfigureMockMvc
public class ImageLikeControllerIntegrationTest {
    @Value("${likeservice.baseurl}")
    private String likeServiceBaseUrl;

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    //login AUTHENTICATION
    private String token;
    ImgBoardUser user;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private ImageLike like1 = new ImageLike(
            true,
            "user1",
            "image1",
            "123A"
    );

    private ImageLike like2 = new ImageLike(
            true,
            "user2",
            "image2",
            "123B"
    );

    private ImageLike like3 = new ImageLike(
            false,
            "user3",
            "image1",
            "123C"
    );

    private ImageLike like4 = new ImageLike(
            false,
            "user4",
            "image2",
            "123D"
    );

    private ImageLike like5 = new ImageLike(
            true,
            "user4",
            "image1",
            "123E"
    );

    private ImageLike like6 = new ImageLike(
            true,
            "user5",
            "image5",
            "123F"
    );

    /*IMAGES*/
    private Image image1 = new Image("AB.png","gust@gmail.com","hond");
    private Image image2 = new Image("ABC.png","you@gmail.com","kat");
    private Image image3 = new Image("ABCD.png","me@gmail.com","konijn");
    private Image image4 = new Image("ABCDE.png","you@gmail.com","vis");


    private List<ImageLike> allLikesFromImage1 = Arrays.asList(like1, like3);
    private List<ImageLike> allLikesFromImage2 = Arrays.asList(like2, like4);
    private List<ImageLike> allLikesFromUser4 = Arrays.asList(like4, like5);
    private List<ImageLike> allLikesFromImage1AndStateTrue = Arrays.asList(like1, like5);


    @BeforeEach
    public void initializeMockserver() throws Exception{
        mockServer = MockRestServiceServer.createServer(restTemplate);

        /*Set Images keys*/
        image1.setKey("image1");
        image2.setKey("image2");
        image3.setKey("image3");
        image4.setKey("image4");

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
    public void whenGetLikesByImageKey_thenReturnAllLikesJson() throws Exception{

        // GET all reviews from User 1
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/image1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allLikesFromImage1))
                );

        mockMvc.perform(get("/likes/image/{imagekey}", "image1").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].state",is(true)))
                .andExpect(jsonPath("$[0].userEmail",is("user1")))
                .andExpect(jsonPath("$[0].imageKey",is("image1")))
                .andExpect(jsonPath("$[1].state",is(false)))
                .andExpect(jsonPath("$[1].userEmail",is("user3")))
                .andExpect(jsonPath("$[1].imageKey",is("image1")));
    }

    @Test
    public void whenGetLikesByUserEmail_thenReturnAllLikesJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/user/user4")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allLikesFromUser4))
                );

        mockMvc.perform(get("/likes/user/{userEmail}", "user4").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].state",is(false)))
                .andExpect(jsonPath("$[0].userEmail",is("user4")))
                .andExpect(jsonPath("$[0].imageKey",is("image2")))
                .andExpect(jsonPath("$[0].likeKey", is("123D")))
                .andExpect(jsonPath("$[1].state",is(true)))
                .andExpect(jsonPath("$[1].userEmail",is("user4")))
                .andExpect(jsonPath("$[1].imageKey",is("image1")))
                .andExpect(jsonPath("$[1].likeKey", is("123E")));
    }

    @Test
    public void whenGetLikesByImageKeyAndState_thenReturnAllLikesJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/image1/state/true")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allLikesFromImage1AndStateTrue))
                );

        mockMvc.perform(get("/likes/image/{imageKey}/state/{state}", "image1", true).header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].state",is(true)))
                .andExpect(jsonPath("$[0].userEmail",is("user1")))
                .andExpect(jsonPath("$[0].imageKey",is("image1")))
                .andExpect(jsonPath("$[0].likeKey", is("123A")))
                .andExpect(jsonPath("$[1].state",is(true)))
                .andExpect(jsonPath("$[1].userEmail",is("user4")))
                .andExpect(jsonPath("$[1].imageKey",is("image1")))
                .andExpect(jsonPath("$[1].likeKey", is("123E")));
    }

    @Test
    public void whenGetLikeByImageKeyAndUserEmail_thenReturnLikeJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/image5/user/user5")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(like6))
                );

        mockMvc.perform(get("/likes/image/{imageKey}/user/{userEmail}", "image5", "user5").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(true)))
                .andExpect(jsonPath("$.userEmail",is("user5")))
                .andExpect(jsonPath("$.imageKey",is("image5")))
                .andExpect(jsonPath("$.likeKey", is("123F")));
    }

    @Test
    public void whenAddLike_thenReturnFilledImageUserLikeJson() throws Exception {

        ImageLike like1 = new ImageLike(
                true,
                "dqfqdf",
                "qfdqdf",
                "ABC123"
        );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(like1))
                );

        mockMvc.perform(post("/likes").header("Authorization", "Bearer " + token)
                .param("userEmail", like1.getUserEmail())
                .param("imageKey", like1.getImageKey())
                .param("state", like1.getState().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(true)))
                .andExpect(jsonPath("$.likeKey",is("ABC123")))
                .andExpect(jsonPath("$.user.userEmail",is("dqfqdf")))
                .andExpect(jsonPath("$.image.key",is("qfdqdf")));

    }

    @Test
    public void whenUpdateLike_thenReturnFilledImageUserLikeJson() throws Exception {
        ImageLike like1 = new ImageLike(
                true,
                "dqfqdf",
                "qfdqdf",
                "123A"
        );

        ImageLike updatedLike = new ImageLike(
                false,
                "dqfqdf",
                "qfdqdf",
                "123A"
        );

        // GET comment from key
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/" + like1.getLikeKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(like1))
                );

        // PUT comment from key
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updatedLike))
                );

        mockMvc.perform(put("/likes").header("Authorization", "Bearer " + token)
                .param("likeKey", updatedLike.getLikeKey())
                .param("state", updatedLike.getState().toString())
                .param("imageKey", updatedLike.getImageKey())
                .param("userEmail", updatedLike.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeKey",is("123A")))
                .andExpect(jsonPath("$.state",is(false)))
                .andExpect(jsonPath("$.user.userEmail",is("dqfqdf")))
                .andExpect(jsonPath("$.image.key",is("qfdqdf")));

    }

    @Test
    public void whenDeleteLike_thenReturnStatusOk() throws Exception {

        // DELETE comment key com123A
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/123A")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/likes/{key}", "123A").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
