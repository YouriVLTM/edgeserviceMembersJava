//package com.example.membersedgeservice;
//
//import com.example.membersedgeservice.config.JwtTokenUtil;
//import com.example.membersedgeservice.model.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jayway.jsonpath.JsonPath;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.web.client.ExpectedCount;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CommentControllerIntigrationTest {
//
//    @Value("${commentservice.baseurl}")
//    private String commentServiceBaseUrl;
//
//    @Value("${userservice.baseurl}")
//    private String userServiceBaseUrl;
//
//    @Value("${imageservice.baseurl}")
//    private String imageServiceBaseUrl;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    //login AUTHENTICATION
//    private String token;
//    ImgBoardUser user;
//
////    private MockRestServiceServer mockServer;
//    private ObjectMapper mapper = new ObjectMapper();
//
//
//    //INIT DATA Objects
//    private Comment comment1 = new Comment(
//            "Comment1",
//            "Dat is mooi.",
//            "r0703028@student.thomasmore.be",
//            ""
//
//    );
//    private Comment comment2 = new Comment(
//            "Comment2",
//            "Dat is speciaal.",
//            "r0703029@student.thomasmore.be",
//            ""
//    );
//    private Comment comment3 = new Comment(
//            "Comment3",
//            "Dat is speciaal.",
//            "r0703028@student.thomasmore.be",
//            ""
//    );
//
//    private Comment updateComment = new Comment(
//            "Upcommand",
//            "Dat is speciaal.",
//            "r0703029@student.thomasmore.be",
//            ""
//    );
//
//
//    private Comment newComment = new Comment(
//            "newcommand",
//            "Dat is speciaal.",
//            "r0703029@student.thomasmore.be",
//            ""
//    );
//    /*IMAGES*/
//    private Image image1 = new Image("AB.png","gust@gmail.com","hond");
//    private Image image2 = new Image("ABC.png","you@gmail.com","kat");
//
//
//    @BeforeEach
//    public void initializeMockserver() throws Exception{
//        JwtRequest login1 = new JwtRequest(
//                "r0703028@student.thomasmore.be",
//                "test"
//        );
//
//        ImgBoardUser user = new ImgBoardUser("Robin","Vranckx","r0703028@student.thomasmore.be",new BCryptPasswordEncoder().encode("test") );
//
//
//        MvcResult result = mockMvc.perform(post("/login")
//                .content(mapper.writeValueAsString(login1))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        token = JsonPath.parse(response).read("$.token").toString();
//
//
//    }
//
//    @BeforeEach
//    public void beforeTests() {
//        // Save All Images
//        try{
//            image1 =restTemplate.postForObject("http://" + imageServiceBaseUrl + "/images",
//                    image1,Image.class);
//        } catch (Exception e) {
//        }
//
//        try{
//            image2 =restTemplate.postForObject("http://" + imageServiceBaseUrl + "/images",
//                    image2,Image.class);
//        } catch (Exception e) {
//        }
//
//        // link to comment
//        comment1.setImageKey(image1.getKey());
//        comment3.setImageKey(image1.getKey());
//        comment2.setImageKey(image2.getKey());
//        updateComment.setImageKey(image2.getKey());
//        newComment.setImageKey(image2.getKey());
//
//        // SAVE ALL comments
//        try{
//            comment1 =restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
//                    comment1,Comment.class);
//        } catch (Exception e) {
//        }
//
//        try{
//            comment2 =restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
//                    comment2,Comment.class);
//        } catch (Exception e) {
//        }
//
//        try{
//            comment3 =restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
//                    comment3,Comment.class);
//        } catch (Exception e) {
//        }
//
//        try{
//            updateComment =restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
//                    updateComment,Comment.class);
//        } catch (Exception e) {
//        }
//
//    }
//
//    @AfterEach
//    public void afterTests() {
//        // DELETE ALL IMAGES
//        try{
//            restTemplate.delete("http://" + imageServiceBaseUrl + "/images/" + image1.getKey());
//        }catch(Exception e){
//        }
//        try{
//            restTemplate.delete("http://" + imageServiceBaseUrl + "/images/" + image2.getKey());
//        }catch(Exception e){
//        }
//
//        // DELETE ALL COMMENTS
//        try{
//            restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + comment1.getKey());
//        }catch(Exception e){
//        }
//        try{
//            restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + comment2.getKey());
//        }catch(Exception e){
//        }
//        try{
//            restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + comment3.getKey());
//        }catch(Exception e){
//        }
//        try{
//            restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + newComment.getKey());
//        }catch(Exception e){
//        }
//        try{
//            restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + updateComment.getKey());
//        }catch(Exception e){
//        }
//
//    }
//
//    @Test
//    public void whenGetCommentsByUserEmail_thenReturnAllCommentsJson() throws Exception{
//        mockMvc.perform(get("/comments/users/{userEmail}", comment1.getUserEmail()).header("Authorization", "Bearer " + token))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].title",is(comment1.getTitle())))
//                .andExpect(jsonPath("$[0].description",is(comment1.getDescription())))
//                .andExpect(jsonPath("$[0].userEmail",is(comment1.getUserEmail())))
//                .andExpect(jsonPath("$[0].imageKey",is(comment1.getImageKey())))
//                .andExpect(jsonPath("$[1].title",is(comment3.getTitle())))
//                .andExpect(jsonPath("$[1].description",is(comment3.getDescription())))
//                .andExpect(jsonPath("$[1].userEmail",is(comment3.getUserEmail())))
//                .andExpect(jsonPath("$[1].imageKey",is(comment3.getImageKey())));
//    }
//    @Test
//    public void whenGetCommentsByBADUserEmail_thenReturnUserBadRequest() throws Exception{
//        mockMvc.perform(get("/comments/users/{userEmail}", "fout").header("Authorization", "Bearer " + token))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertEquals("400 BAD_REQUEST \"User Not Found\"", result.getResolvedException().getMessage()));
//    }
//
//
//    @Test
//    public void whenGetCommentsByImagesKey_thenReturnAllCommentsJson() throws Exception{
//
//        mockMvc.perform(get("/comments/images/{key}", image1.getKey()).header("Authorization", "Bearer " + token))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].title",is(comment1.getTitle())))
//                .andExpect(jsonPath("$[0].description",is(comment1.getDescription())))
//                .andExpect(jsonPath("$[0].userEmail",is(comment1.getUserEmail())))
//                .andExpect(jsonPath("$[0].imageKey",is(comment1.getImageKey())))
//                .andExpect(jsonPath("$[1].title",is(comment3.getTitle())))
//                .andExpect(jsonPath("$[1].description",is(comment3.getDescription())))
//                .andExpect(jsonPath("$[1].userEmail",is(comment3.getUserEmail())))
//                .andExpect(jsonPath("$[1].imageKey",is(comment3.getImageKey())));
//    }
//    @Test
//    public void whenGetCommentsByBadImageKey_thenReturnBadRequest() throws Exception{
//        mockMvc.perform(get("/comments/images/{key}", "fout").header("Authorization", "Bearer " + token))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertEquals("400 BAD_REQUEST \"ImageKey Not Found\"", result.getResolvedException().getMessage()));
//    }
//
//
//    @Test
//    public void whenAddComment_thenReturnFilledImageUserCommentJson() throws Exception {
//        mockMvc.perform(post("/comments").header("Authorization", "Bearer " + token)
//                .param("userEmail", newComment.getUserEmail())
//                .param("imageKey", newComment.getImageKey())
//                .param("title", newComment.getTitle())
//                .param("description", newComment.getDescription())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title",is(newComment.getTitle())))
//                .andExpect(jsonPath("$.description",is(newComment.getDescription())))
//                .andExpect(jsonPath("$.user.email",is(newComment.getUserEmail())))
//                .andExpect(jsonPath("$.image.key",is(newComment.getImageKey())));
//    }
//    @Test
//    public void whenAddCommentByBadUserEmail_thenReturnBadRequest() throws Exception {
//
//        mockMvc.perform(post("/comments").header("Authorization", "Bearer " + token)
//                .param("userEmail", "fout")
//                .param("imageKey", "3d5201febd23107ac50830d0a2b1380efd3ca77fe1624c2ad4e6a6e2483f965d")
//                .param("title", "Comment3")
//                .param("description", "Dat is mooi.")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertEquals("400 BAD_REQUEST \"User Not Found\"", result.getResolvedException().getMessage()));
//
//    }
//    @Test
//    public void whenAddCommentByBadImageKey_thenReturnBadRequest() throws Exception {
//
//        mockMvc.perform(post("/comments").header("Authorization", "Bearer " + token)
//                .param("userEmail", "r0703028@student.thomasmore.be")
//                .param("imageKey", "fout")
//                .param("title", "Comment3")
//                .param("description", "Dat is mooi.")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertEquals("400 BAD_REQUEST \"ImageKey Not Found\"", result.getResolvedException().getMessage()));
//
//    }
//
//
//    @Test
//    public void whenUpdateComment_thenReturnFilledImageUserCommentJson() throws Exception {
//        mockMvc.perform(put("/comments").header("Authorization", "Bearer " + token)
//                .param("commentKey", updateComment.getKey())
//                .param("title", "WIE")
//                .param("description", "PLAY")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title",is("WIE")))
//                .andExpect(jsonPath("$.description",is("PLAY")));
//
//    }
//
//
//    @Test
//    public void whenDeleteComment_thenReturnStatusOk() throws Exception {
//        mockMvc.perform(delete("/comments/{key}", updateComment.getKey()).header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk());
//    }
//}
