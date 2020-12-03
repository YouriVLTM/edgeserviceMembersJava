package com.example.demo;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;


    private Comment comment1 = new Comment(
            "Comment1",
            "Dat is mooi.",
            "youri@hotmail.com",
            "test.png"
    );

    private Comment comment2 = new Comment(
            "Comment2",
            "hallo ik ben youri",
            "lorenzo@hotmail.com",
            "urlimage"
    );

    private Comment comment3 = new Comment(
            "Comment3",
            "hallo ik ben youri",
            "kato@hotmail.com",
            "urlimage"
    );

    private Comment comment4 = new Comment(
            "Comment4",
            "hallo ik ben youri",
            "wen@hotmail.com",
            "urlimage"
    );
    private Comment comment5 = new Comment(
            "Comment5",
            "hallo ik ben youri",
            "te@hotmail.com",
            "urlimage"
    );

    @BeforeEach
    public void beforeAllTests() {
        commentRepository.deleteAll();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        commentRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    public void givenComment_whenGetComments_thenReturnJsonComments() throws Exception {

        mockMvc.perform(get("/comments", this.comment1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void givenComment_whenGetCommentByKey_thenReturnJsonComment() throws Exception{

        mockMvc.perform(get("/comments/{key}", this.comment1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Comment1")))
                .andExpect(jsonPath("$.description", is("Dat is mooi.")))
                .andExpect(jsonPath("$.userEmail", is("youri@hotmail.com")));
    }

    @Test
    public void givenComment_whenPostComment_thenReturnJsonComment() throws Exception {
        Comment comment = new Comment( "PostTitle",
                "this is a post Title",
                "post@hotmail.com",
                "post.png");

        mockMvc.perform(post("/comments")
                .content(mapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("PostTitle")))
                .andExpect(jsonPath("$.description", is("this is a post Title")))
                .andExpect(jsonPath("$.userEmail", is("post@hotmail.com")))
                .andExpect(jsonPath("$.imageKey", is("post.png")));

    }

    @Test
    public void giveComment_whenPutComment_thenReturnJsonComment() throws Exception {

        comment2.setTitle("PutTitle");
        comment2.setDescription("this is a put Title");
        comment2.setUserEmail("put@hotmail.com");
        comment2.setImageKey("put.png");

        mockMvc.perform(put("/comments")
                .content(mapper.writeValueAsString(comment2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("PutTitle")))
                .andExpect(jsonPath("$.description", is("this is a put Title")))
                .andExpect(jsonPath("$.userEmail", is("put@hotmail.com")))
                .andExpect(jsonPath("$.imageKey", is("put.png")));
    }

    @Test
    public void givenComment_whenDeleteComment_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/comments/{key}", comment1.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoComment_whenDeleteComment_thenStatusNotFound() throws Exception {

        mockMvc.perform(delete("/comments/{key}", "0bb58513b1b57a7c7f8963f001f9896705b1ab2a22e320e4eb0ea2a985084fa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
