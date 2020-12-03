package com.example.demo;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository commentRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenComment_whenGetComments_thenReturnJsonComments() throws Exception {

        Comment comment1 = new Comment(
                "Comment1",
                "Dat is mooi.",
                "com1@hotmail.com",
                "com1.png"
        );

        Comment comment2 = new Comment(
                "Comment2",
                "Dat is speciaal.",
                "com2@hotmail.com",
                "com2.png"
        );


        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment2);

        given(commentRepository.findAll()).willReturn(commentList);

        mockMvc.perform(get("/comments"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title",is("Comment1")))
                .andExpect(jsonPath("$[0].description",is("Dat is mooi.")))
                .andExpect(jsonPath("$[0].userEmail",is("com1@hotmail.com")))
                .andExpect(jsonPath("$[0].imageKey",is("com1.png")))
                .andExpect(jsonPath("$[1].title",is("Comment2")))
                .andExpect(jsonPath("$[1].description",is("Dat is speciaal.")))
                .andExpect(jsonPath("$[1].userEmail",is("com2@hotmail.com")))
                .andExpect(jsonPath("$[1].imageKey",is("com2.png")));
    }

     @Test
    public void givenComment_whenGetCommentByKey_thenReturnJsonComment() throws Exception{
         Comment comment1 = new Comment(
                 "Comment1",
                 "Dat is mooi.",
                 "com1@hotmail.com",
                 "com1.png"
         );

         given(commentRepository.findByKey(comment1.getKey())).willReturn(comment1);

        mockMvc.perform(get("/comments/{key}", comment1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Comment1")))
                .andExpect(jsonPath("$.description", is("Dat is mooi.")))
                .andExpect(jsonPath("$.userEmail", is("com1@hotmail.com")))
                .andExpect(jsonPath("$.imageKey", is("com1.png")));
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
        Comment comment1 = new Comment(
                "Comment1",
                "Dat is mooi.",
                "com1@hotmail.com",
                "com1.png"
        );
        given(commentRepository.findByKey(comment1.getKey())).willReturn(comment1);

        comment1.setTitle("PutTitle");
        comment1.setDescription("this is a put Title");
        comment1.setUserEmail("put@hotmail.com");
        comment1.setImageKey("put.png");


        mockMvc.perform(put("/comments")
                .content(mapper.writeValueAsString(comment1))
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
       Comment comment1 = new Comment(
               "Comment1",
               "Dat is mooi.",
               "com1@hotmail.com",
               "com1.png"
       );

       given(commentRepository.findByKey(comment1.getKey())).willReturn(comment1);

        mockMvc.perform(delete("/comments/{key}", comment1.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

     @Test
    public void givenNoComment_whenDeleteComment_thenStatusNotFound() throws Exception {
         given(commentRepository.findByKey("0bb58513b1b57a7c7f8963f001f9896705b1ab2a22e320e4eb0ea2a985084fa")).willReturn(null);
        mockMvc.perform(delete("/comments/{key}", "0bb58513b1b57a7c7f8963f001f9896705b1ab2a22e320e4eb0ea2a985084fa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
