package com.xptotec.walletplus.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xptotec.walletplus.dto.AddTransactionRequest;
import com.xptotec.walletplus.dto.PurchaseTransactionRequest;
import com.xptotec.walletplus.dto.RefundTransactionRequest;
import com.xptotec.walletplus.dto.UserRequest;
import com.xptotec.walletplus.dto.WithdrawTransactionRequest;
import com.xptotec.walletplus.model.Balance;
import com.xptotec.walletplus.model.Transaction;
import com.xptotec.walletplus.model.TransactionChannelEnum;
import com.xptotec.walletplus.model.TransactionStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WalletControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddTransaction() throws Exception {
        String cpf = "12345678901";
        UserRequest userRequest = new UserRequest();
        userRequest.setCpf(cpf);
        userRequest.setName("Test User");
        userRequest.setEmail("Test3@testes.com");

        // Criação de um novo usuário
        mockMvc.perform(post("/api/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        AddTransactionRequest request = new AddTransactionRequest();
        request.setCpf(cpf);
        request.setAmount(new BigDecimal("100.00"));
        request.setTimestamp(LocalDateTime.of(2023, 10, 1, 12, 0, 0));
        request.setChannel(TransactionChannelEnum.ATM);

        // Adição de uma nova transação
        MvcResult result = mockMvc.perform(post("/api/wallet/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction asyncResult = (Transaction) result.getAsyncResult();

        // Verificação dos resultados da transação
        assertEquals(cpf, asyncResult.getCpf());
        assertEquals(new BigDecimal("100.00"), asyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, asyncResult.getStatus());
    }

    @Test
    public void testWithdrawTransaction() throws Exception {
        String cpf = "12345678902";
        UserRequest userRequest = new UserRequest();
        userRequest.setCpf(cpf);
        userRequest.setName("Test User");
        userRequest.setEmail("Test@testes.com");

        // Criação de um novo usuário
        mockMvc.perform(post("/api/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        AddTransactionRequest addTransactionRequest = new AddTransactionRequest();
        addTransactionRequest.setCpf(cpf);
        addTransactionRequest.setAmount(new BigDecimal("100.00"));
        addTransactionRequest.setTimestamp(LocalDateTime.now());
        addTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Adição de uma nova transação
        MvcResult result = mockMvc.perform(post("/api/wallet/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction asyncResult = (Transaction) result.getAsyncResult();

        // Verificação dos resultados da transação
        assertEquals(cpf, asyncResult.getCpf());
        assertEquals(new BigDecimal("100.00"), asyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, asyncResult.getStatus());

        WithdrawTransactionRequest withdrawTransactionRequest = new WithdrawTransactionRequest();
        withdrawTransactionRequest.setCpf(cpf);
        withdrawTransactionRequest.setAmount(new BigDecimal("10.00"));
        withdrawTransactionRequest.setTimestamp(LocalDateTime.now());
        withdrawTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Realização de um saque
        MvcResult withdrawResult = mockMvc.perform(post("/api/wallet/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction withdrawAsyncResult = (Transaction) withdrawResult.getAsyncResult();

        // Verificação dos resultados do saque
        assertEquals(cpf, withdrawAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), withdrawAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, withdrawAsyncResult.getStatus());

        // Verificação do saldo
        MvcResult balancesResult = mockMvc.perform(get("/api/wallet/balance/" + cpf))
                .andExpect(status().isOk()).andReturn();

        Balance balance = objectMapper.readValue(balancesResult.getResponse().getContentAsString(), Balance.class);

        assertEquals(new BigDecimal("90.00"), balance.getAmount());
        assertEquals(cpf, balance.getCpf());
    }

    @Test
    public void testPurchaseTransaction() throws Exception {
        String cpf = "12345678903";
        UserRequest userRequest = new UserRequest();
        userRequest.setCpf(cpf);
        userRequest.setName("Test User");
        userRequest.setEmail("Test1@testes.com");

        // Criação de um novo usuário
        mockMvc.perform(post("/api/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        AddTransactionRequest addTransactionRequest = new AddTransactionRequest();
        addTransactionRequest.setCpf(cpf);
        addTransactionRequest.setAmount(new BigDecimal("100.00"));
        addTransactionRequest.setTimestamp(LocalDateTime.of(2023, 10, 2, 12, 0, 0));
        addTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Adição de uma nova transação
        MvcResult result = mockMvc.perform(post("/api/wallet/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction asyncResult = (Transaction) result.getAsyncResult();

        // Verificação dos resultados da transação
        assertEquals(cpf, asyncResult.getCpf());
        assertEquals(new BigDecimal("100.00"), asyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, asyncResult.getStatus());

        PurchaseTransactionRequest purchaseTransactionRequest = new PurchaseTransactionRequest();
        purchaseTransactionRequest.setCpf(cpf);
        purchaseTransactionRequest.setAmount(new BigDecimal("10.00"));
        purchaseTransactionRequest.setTimestamp(LocalDateTime.of(2023, 10, 2, 12, 0, 0));
        purchaseTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Realização de uma compra
        MvcResult purchaseResult = mockMvc.perform(post("/api/wallet/transaction/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction purchaseAsyncResult = (Transaction) purchaseResult.getAsyncResult();

        // Verificação dos resultados da compra
        assertEquals(cpf, purchaseAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), purchaseAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.PENDING, purchaseAsyncResult.getStatus());

        // Verificação do saldo
        MvcResult balancesResult = mockMvc.perform(get("/api/wallet/balance/" + cpf))
                .andExpect(status().isOk()).andReturn();

        Balance balance = objectMapper.readValue(balancesResult.getResponse().getContentAsString(), Balance.class);

        assertEquals(new BigDecimal("100.00"), balance.getAmount());
        assertEquals(cpf, balance.getCpf());

        // Completar a transação de compra
        MvcResult purchaseCompleteResult = mockMvc.perform(put("/api/wallet/transaction/purchase/".concat(purchaseAsyncResult.getId().toString()).concat("?status=COMPLETED"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Transaction purchaseCompleteAsyncResult = (Transaction) purchaseCompleteResult.getAsyncResult();

        // Verificação dos resultados da transação completada
        assertEquals(cpf, purchaseCompleteAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), purchaseCompleteAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, purchaseCompleteAsyncResult.getStatus());
    }

    @Test
    public void testCancelPurchaseTransaction() throws Exception {
        String cpf = "12345678904";
        UserRequest userRequest = new UserRequest();
        userRequest.setCpf(cpf);
        userRequest.setName("Test User");
        userRequest.setEmail("Test4@testes.com");

        // Criação de um novo usuário
        mockMvc.perform(post("/api/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        AddTransactionRequest addTransactionRequest = new AddTransactionRequest();
        addTransactionRequest.setCpf(cpf);
        addTransactionRequest.setAmount(new BigDecimal("100.00"));
        addTransactionRequest.setTimestamp(LocalDateTime.of(2023, 10, 3, 12, 0, 0));
        addTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Adição de uma nova transação
        MvcResult result = mockMvc.perform(post("/api/wallet/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction asyncResult = (Transaction) result.getAsyncResult();

        // Verificação dos resultados da transação
        assertEquals(cpf, asyncResult.getCpf());
        assertEquals(new BigDecimal("100.00"), asyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, asyncResult.getStatus());

        PurchaseTransactionRequest purchaseTransactionRequest = new PurchaseTransactionRequest();
        purchaseTransactionRequest.setCpf(cpf);
        purchaseTransactionRequest.setAmount(new BigDecimal("10.00"));
        purchaseTransactionRequest.setTimestamp(LocalDateTime.of(2023, 10, 3, 12, 0, 0));
        purchaseTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Realização de uma compra
        MvcResult purchaseResult = mockMvc.perform(post("/api/wallet/transaction/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction purchaseAsyncResult = (Transaction) purchaseResult.getAsyncResult();

        // Verificação dos resultados da compra
        assertEquals(cpf, purchaseAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), purchaseAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.PENDING, purchaseAsyncResult.getStatus());

        // Verificação do saldo
        MvcResult balancesResult = mockMvc.perform(get("/api/wallet/balance/" + cpf))
                .andExpect(status().isOk()).andReturn();

        Balance balance = objectMapper.readValue(balancesResult.getResponse().getContentAsString(), Balance.class);

        assertEquals(new BigDecimal("100.00"), balance.getAmount());
        assertEquals(cpf, balance.getCpf());

        // Cancelar a transação de compra
        MvcResult purchaseCANCELLEDResult = mockMvc.perform(put("/api/wallet/transaction/purchase/".concat(purchaseAsyncResult.getId().toString()).concat("?status=CANCELLED"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Transaction purchaseCancelledAsyncResult = (Transaction) purchaseCANCELLEDResult.getAsyncResult();

        // Verificação dos resultados da transação cancelada
        assertEquals(cpf, purchaseCancelledAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), purchaseCancelledAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.CANCELLED, purchaseCancelledAsyncResult.getStatus());
    }

    @Test
    public void testRefundTransaction() throws Exception {
        String cpf = "12345678906";
        UserRequest userRequest = new UserRequest();
        userRequest.setCpf(cpf);
        userRequest.setName("Test User");
        userRequest.setEmail("Test2@testes.com");

        // Criação de um novo usuário
        mockMvc.perform(post("/api/wallet/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        AddTransactionRequest addTransactionRequest = new AddTransactionRequest();
        addTransactionRequest.setCpf(cpf);
        addTransactionRequest.setAmount(new BigDecimal("100.00"));
        addTransactionRequest.setTimestamp(LocalDateTime.of(2023, 10, 4, 12, 0, 0));
        addTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Adição de uma nova transação
        MvcResult result = mockMvc.perform(post("/api/wallet/transaction/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction asyncResult = (Transaction) result.getAsyncResult();

        // Verificação dos resultados da transação
        assertEquals(cpf, asyncResult.getCpf());
        assertEquals(new BigDecimal("100.00"), asyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, asyncResult.getStatus());

        WithdrawTransactionRequest withdrawTransactionRequest = new WithdrawTransactionRequest();
        withdrawTransactionRequest.setCpf(cpf);
        withdrawTransactionRequest.setAmount(new BigDecimal("10.00"));
        withdrawTransactionRequest.setTimestamp(LocalDateTime.of(2023, 10, 4, 12, 0, 0));
        withdrawTransactionRequest.setChannel(TransactionChannelEnum.ATM);

        // Realização de um saque
        MvcResult withdrawResult = mockMvc.perform(post("/api/wallet/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction withdrawAsyncResult = (Transaction) withdrawResult.getAsyncResult();

        // Verificação dos resultados do saque
        assertEquals(cpf, withdrawAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), withdrawAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.COMPLETED, withdrawAsyncResult.getStatus());

        // Verificação do saldo
        MvcResult balancesResult = mockMvc.perform(get("/api/wallet/balance/" + cpf))
                .andExpect(status().isOk()).andReturn();

        Balance balance = objectMapper.readValue(balancesResult.getResponse().getContentAsString(), Balance.class);

        assertEquals(new BigDecimal("90.00"), balance.getAmount());
        assertEquals(cpf, balance.getCpf());

        // Realização de um reembolso
        RefundTransactionRequest refundTransactionRequest = new RefundTransactionRequest();
        refundTransactionRequest.setTransactionId(withdrawAsyncResult.getId());

        MvcResult refundResult = mockMvc.perform(post("/api/wallet/transaction/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundTransactionRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Transaction refundAsyncResult = (Transaction) refundResult.getAsyncResult();

        // Verificação dos resultados do reembolso
        assertEquals(cpf, refundAsyncResult.getCpf());
        assertEquals(new BigDecimal("10.00"), refundAsyncResult.getAmount());
        assertEquals(TransactionStatusEnum.REFUNDED, refundAsyncResult.getStatus());

        // Verificação do saldo após o reembolso
        MvcResult balancesRefundResult = mockMvc.perform(get("/api/wallet/balance/" + cpf))
                .andExpect(status().isOk()).andReturn();

        Balance balanceRefund = objectMapper.readValue(balancesRefundResult.getResponse().getContentAsString(), Balance.class);

        assertEquals(new BigDecimal("100.00"), balanceRefund.getAmount());
        assertEquals(cpf, balanceRefund.getCpf());
    }
}