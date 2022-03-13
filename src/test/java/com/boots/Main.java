package com.boots;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class Main {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void gettingUserInfoWithoutAuth() throws Exception {
        this.mockMvc.perform(get("/get-user-info").param("userName", "test"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails("test")
    public void gettingUserInfoWithAuth() throws Exception {
        this.mockMvc.perform(get("/get-user-info").param("userName", "test"))
                .andExpect(status().is2xxSuccessful());
    }
}
