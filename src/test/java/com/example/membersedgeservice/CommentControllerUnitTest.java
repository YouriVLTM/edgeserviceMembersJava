package com.example.membersedgeservice;

import com.example.membersedgeservice.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

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

    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;


    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void givenComment_whenGetCommentByKey_thenReturnJsonComment() throws Exception{
         Comment comment1 = new Comment(
                 "Comment1",
                 "Dat is mooi.",
                 "com1@hotmail.com",
                 "com1.png"
         );

        /* given(commentRepository.findByKey(comment1.getKey())).willReturn(comment1);

        mockMvc.perform(get("/comments/{key}", comment1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Comment1")))
                .andExpect(jsonPath("$.description", is("Dat is mooi.")))
                .andExpect(jsonPath("$.userEmail", is("com1@hotmail.com")))
                .andExpect(jsonPath("$.imageKey", is("com1.png")));

         */
    }
}
