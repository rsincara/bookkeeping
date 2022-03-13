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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class Balance {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test")
    public void addBalanceWithAuth() throws Exception {
        this.mockMvc.perform(post("/add-balance")
                        .param("userName", "test")
                        .param("balanceName", "test visa test")
                        .param("balanceAmount", "0")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void addBalanceWithoutAuth() throws Exception {
        this.mockMvc.perform(post("/add-balance")
                        .param("userName", "test")
                        .param("balanceName", "test visa test")
                        .param("balanceAmount", "0")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("test")
    public void removeBalance() throws Exception {
        this.mockMvc.perform(get("/remove-balance")
                        .param("userName", "test")
                        .param("balanceName", "test visa test")
                )
                .andExpect(status().is2xxSuccessful());
    }
}
