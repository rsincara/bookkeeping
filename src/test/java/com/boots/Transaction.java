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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class Transaction {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test")
    public void addTransactionWithAuth() throws Exception {
        this.mockMvc.perform(post("/add-transaction")
                        .param("userName", "test")
                        .param("balanceName", "test visa")
                        .param("transactionType", "Доход")
                        .param("amount", "300")
                        .param("date", "2022-03-13")
                        .param("commentary", "test")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void addTransactionWithoutAuth() throws Exception {
        this.mockMvc.perform(post("/add-transaction")
                        .param("userName", "test")
                        .param("balanceName", "test visa")
                        .param("transactionType", "Доход")
                        .param("amount", "300")
                        .param("date", "2022-03-13")
                        .param("commentary", "test")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
