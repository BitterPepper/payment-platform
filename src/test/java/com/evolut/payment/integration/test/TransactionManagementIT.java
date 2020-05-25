package com.evolut.payment.integration.test;

import com.evolut.payment.dto.AccountDto;
import com.evolut.payment.dto.AccountsResp;
import com.evolut.payment.dto.TransactionDto;
import com.evolut.payment.dto.TransactionsResp;
import com.evolut.payment.integration.util.DriverIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.evolut.payment.integration.util.DriverIT.ACC_GROUP_URI;
import static com.evolut.payment.integration.util.DriverIT.TRN_GROUP_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TransactionManagementIT {
    private Logger LOG = LoggerFactory.getLogger(TransactionManagementIT.class);
    private DriverIT driver;

    @BeforeEach
    public void init() throws Exception {
        driver = new DriverIT();
        driver.init();
    }

    @AfterEach
    public void destroy() throws Exception {
        driver.destroy();
    }

    @Test
    public void testTransactionCRUD() throws Exception {
        LOG.info("================== Transaction CRUD test ==================");

        String accCreateRq = driver.buildAccCreateReq("575757575NM", "Vitaly", "5000.01");
        String acc1Id = driver.sendCorrectPostReq(ACC_GROUP_URI, accCreateRq, AccountDto.class).getId();
        driver.buildAccCreateReq("787878787NM", "Dmitry", "4500.01");
        String acc2Id = driver.sendCorrectPostReq(ACC_GROUP_URI, accCreateRq, AccountDto.class).getId();

        // check accounts quantity - no any
        AccountsResp accountsResp = driver.sendCorrectGetReq(ACC_GROUP_URI, AccountsResp.class);
        assertEquals(2, accountsResp.getAccounts().size());

        // check transaction quantity - no any
        TransactionsResp transactionsResp = driver.sendCorrectGetReq(TRN_GROUP_URI, TransactionsResp.class);
        assertEquals(0, transactionsResp.getTransactions().size());

        // check create new transaction
        String trnCreateRq = driver.buildTrnCreateReq(acc1Id, acc2Id, "1000.02");
        TransactionDto trnResp = driver.sendCorrectPostReq(TRN_GROUP_URI, trnCreateRq, TransactionDto.class);
        assertEquals(trnResp.getAccFromId(), "123456789MY");
        assertEquals(trnResp.getAccToId(), "Sergii");
        LOG.info("================== Transaction CRUD test OK ==================");
    }
}
