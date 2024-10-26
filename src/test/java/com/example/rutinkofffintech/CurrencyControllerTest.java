package com.example.rutinkofffintech;

import com.example.rutinkofffintech.TASK_8.controllers.CurrencyController;
import com.example.rutinkofffintech.TASK_8.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void testGetCurrencyRate() throws Exception {
        Mockito.when(currencyService.getCurrencyRate("USD")).thenReturn(74.5);

        mockMvc.perform(get("/currencies/rates/USD"))
                .andExpect(status().isOk());
    }
}
