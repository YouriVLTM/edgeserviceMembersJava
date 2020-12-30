package com.example.membersedgeservice;

import com.example.membersedgeservice.config.JwtTokenUtil;
import com.example.membersedgeservice.model.Image;
import com.example.membersedgeservice.model.ImageLike;
import com.example.membersedgeservice.model.ImgBoardUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageLikeControllerUnitTest {
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

    /*LIKES*/
    private ImageLike like1 = new ImageLike(true, "you@mail.com", "1", DigestUtils.sha256Hex("1" + "you@mail.com" + new Date(System.currentTimeMillis())).toString());
    private ImageLike like2 = new ImageLike(true, "me@mail.com", "1", DigestUtils.sha256Hex("1" + "me@mail.com" + new Date(System.currentTimeMillis())).toString());
    private ImageLike like3 = new ImageLike(true, "you@mail.com", "2", DigestUtils.sha256Hex("2" + "you@mail.com" + new Date(System.currentTimeMillis())).toString());
    private ImageLike like4 = new ImageLike(false, "me@mail.com", "2", DigestUtils.sha256Hex("2" + "me@mail.com" + new Date(System.currentTimeMillis())).toString());
    private ImageLike like5 = new ImageLike(true, "gust@mail.com", "2", DigestUtils.sha256Hex("2" + "gust@mail.com" + new Date(System.currentTimeMillis())).toString());

    /*IMAGES*/
    private Image image1 = new Image("AB.png","gust@gmail.com","hond");
    private Image image2 = new Image("ABC.png","you@gmail.com","kat");
    private Image image3 = new Image("ABCD.png","me@gmail.com","konijn");
    private Image image4 = new Image("ABCDE.png","you@gmail.com","vis");

    private List<ImageLike> allLikesFromImage1 = Arrays.asList(like1, like2);
    private List<ImageLike> allLikesFromUserYou = Arrays.asList(like1, like3);
    private List<ImageLike> allLikesFromImage1AndStateTrue = Arrays.asList(like1, like2);

    @BeforeEach
    public void initializeMockserver() throws Exception{
        mockServer = MockRestServiceServer.createServer(restTemplate);

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
    public void whenGetLikesByImagesKey_thenReturnAllLikesJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allLikesFromImage1))
                );

        mockMvc.perform(get("/likes/image/{imagekey}", "1").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].state",is(true)))
                .andExpect(jsonPath("$[0].userEmail",is("you@mail.com")))
                .andExpect(jsonPath("$[0].imageKey",is("1")))
                .andExpect(jsonPath("$[0].likeKey", is("dsfdsf")))
                .andExpect(jsonPath("$[1].state",is(true)))
                .andExpect(jsonPath("$[1].userEmail",is("me@mail.com")))
                .andExpect(jsonPath("$[1].imageKey",is("1")))
                .andExpect(jsonPath("$[1].likeKey", is("dsfdsf")));
    }

    @Test
    public void whenGetLikesByUserEmail_thenReturnAllLikesJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/user/you@mail.com")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allLikesFromUserYou))
                );

        mockMvc.perform(get("/likes/user/{userEmail}", "you@mail.com").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].state",is(true)))
                .andExpect(jsonPath("$[0].userEmail",is("you@mail.com")))
                .andExpect(jsonPath("$[0].imageKey",is("1")))
                .andExpect(jsonPath("$[1].state",is(true)))
                .andExpect(jsonPath("$[1].userEmail",is("you@mail.com")))
                .andExpect(jsonPath("$[1].imageKey",is("2")));
    }

    @Test
    public void whenGetLikesByImageKeyAndState_thenReturnAllLikesJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/1/state/true")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allLikesFromImage1AndStateTrue))
                );

        mockMvc.perform(get("/likes/image/{imageKey}/state/{state}", "1", true).header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].state",is(true)))
                .andExpect(jsonPath("$[0].userEmail",is("you@mail.com")))
                .andExpect(jsonPath("$[0].imageKey",is("1")))
                .andExpect(jsonPath("$[1].state",is(true)))
                .andExpect(jsonPath("$[1].userEmail",is("me@mail.com")))
                .andExpect(jsonPath("$[1].imageKey",is("1")));
    }

    @Test
    public void whenGetLikeByImageKeyAndUserEmail_thenReturnLikeJson() throws Exception{

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + likeServiceBaseUrl + "/likes/image/1/user/me@mail.com")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(like2))
                );

        mockMvc.perform(get("/likes/image/{imageKey}/user/{userEmail}", "1", "me@mail.com").header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state",is(true)))
                .andExpect(jsonPath("$.userEmail",is("me@mail.com")))
                .andExpect(jsonPath("$.imageKey",is("1")));
    }
}
