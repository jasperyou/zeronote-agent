package com.zeronote.accounting.service;

import com.zeronote.accounting.dto.TransactionRequest;
import com.zeronote.accounting.dto.TransactionResponse;
import com.zeronote.accounting.model.Transaction;
import com.zeronote.accounting.model.TransactionCategory;
import com.zeronote.accounting.model.TransactionScenario;
import com.zeronote.accounting.model.TransactionType;
import com.zeronote.accounting.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private AiAnalysisService aiAnalysisService;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private TransactionRequest testRequest;
    private Transaction testTransaction;
    private AiAnalysisService.AiAnalysisResult testAnalysis;
    
    @BeforeEach
    void setUp() {
        // 设置测试数据
        testRequest = new TransactionRequest();
        testRequest.setAmount(new BigDecimal("25.50"));
        testRequest.setDescription("午餐");
        testRequest.setMerchant("星巴克");
        testRequest.setLocation("北京朝阳区");
        testRequest.setTransactionDate(LocalDateTime.now());
        
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setAmount(new BigDecimal("25.50"));
        testTransaction.setType(TransactionType.EXPENSE);
        testTransaction.setCategory(TransactionCategory.FOOD_DINING);
        testTransaction.setScenario(TransactionScenario.REGULAR);
        testTransaction.setDescription("午餐");
        testTransaction.setMerchant("星巴克");
        testTransaction.setLocation("北京朝阳区");
        testTransaction.setTransactionDate(LocalDateTime.now());
        testTransaction.setCreatedAt(LocalDateTime.now());
        testTransaction.setUpdatedAt(LocalDateTime.now());
        
        testAnalysis = AiAnalysisService.AiAnalysisResult.builder()
                .type(TransactionType.EXPENSE)
                .category(TransactionCategory.FOOD_DINING)
                .scenario(TransactionScenario.REGULAR)
                .merchant("星巴克")
                .description("午餐")
                .analysis("AI分析：这是一笔餐饮支出")
                .build();
    }
    
    @Test
    void testCreateTransaction_Success() {
        // 准备
        when(aiAnalysisService.analyzeTransaction(any(), any(), any(), any()))
                .thenReturn(testAnalysis);
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(testTransaction);
        
        // 执行
        TransactionResponse response = transactionService.createTransaction(testRequest);
        
        // 验证
        assertNotNull(response);
        assertEquals(testTransaction.getId(), response.getId());
        assertEquals(testTransaction.getAmount(), response.getAmount());
        assertEquals(testTransaction.getType(), response.getType());
        assertEquals(testTransaction.getCategory(), response.getCategory());
        assertEquals(testTransaction.getDescription(), response.getDescription());
        assertEquals(testTransaction.getMerchant(), response.getMerchant());
        
        verify(aiAnalysisService).analyzeTransaction(
                testRequest.getAmount(),
                testRequest.getDescription(),
                testRequest.getMerchant(),
                testRequest.getLocation()
        );
        verify(transactionRepository).save(any(Transaction.class));
    }
    
    @Test
    void testGetTransactionById_Exists() {
        // 准备
        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(testTransaction));
        
        // 执行
        Optional<TransactionResponse> response = transactionService.getTransactionById(1L);
        
        // 验证
        assertTrue(response.isPresent());
        assertEquals(testTransaction.getId(), response.get().getId());
        assertEquals(testTransaction.getAmount(), response.get().getAmount());
        
        verify(transactionRepository).findById(1L);
    }
    
    @Test
    void testGetTransactionById_NotExists() {
        // 准备
        when(transactionRepository.findById(999L))
                .thenReturn(Optional.empty());
        
        // 执行
        Optional<TransactionResponse> response = transactionService.getTransactionById(999L);
        
        // 验证
        assertFalse(response.isPresent());
        
        verify(transactionRepository).findById(999L);
    }
    
    @Test
    void testDeleteTransaction_Exists() {
        // 准备
        when(transactionRepository.existsById(1L))
                .thenReturn(true);
        doNothing().when(transactionRepository).deleteById(1L);
        
        // 执行
        boolean result = transactionService.deleteTransaction(1L);
        
        // 验证
        assertTrue(result);
        verify(transactionRepository).existsById(1L);
        verify(transactionRepository).deleteById(1L);
    }
    
    @Test
    void testDeleteTransaction_NotExists() {
        // 准备
        when(transactionRepository.existsById(999L))
                .thenReturn(false);
        
        // 执行
        boolean result = transactionService.deleteTransaction(999L);
        
        // 验证
        assertFalse(result);
        verify(transactionRepository).existsById(999L);
        verify(transactionRepository, never()).deleteById(any());
    }
} 