package com.evolut.payment.integration.test;

import com.evolut.payment.dto.AccountDto;
import com.evolut.payment.dto.AccountsResp;
import com.evolut.payment.integration.util.DriverIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static com.evolut.payment.integration.util.DriverIT.ACC_GROUP_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class AccountManagementIT {
    private Logger LOG = LoggerFactory.getLogger(AccountManagementIT.class);
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
    public void testAccountCRUD() throws Exception {
        LOG.info("================== Account CRUD test ==================");

        //TODO add tests: add acc with duplicate serial

        // check accounts quantity - no any
        AccountsResp accountsResp = driver.sendCorrectGetReq(ACC_GROUP_URI, AccountsResp.class);
        assertEquals(0, accountsResp.getAccounts().size());

        // check create account
        String accCreateRq = driver.buildAccCreateReq("123456789MY", "Sergii", "1000.01");
        AccountDto accountResp = driver.sendCorrectPostReq(ACC_GROUP_URI, accCreateRq, AccountDto.class);
        assertEquals(accountResp.getSerial(), "123456789MY");
        assertEquals(accountResp.getOwner(), "Sergii");
        assertEquals(accountResp.getBalance(), BigDecimal.valueOf(1000.01));
        assertEquals(accountResp.getBlocked(), BigDecimal.ZERO);

        // check accounts quantity - one account
        accountsResp = driver.sendCorrectGetReq(ACC_GROUP_URI, AccountsResp.class);
        assertEquals(1, accountsResp.getAccounts().size());

        // check create wrong account
        accCreateRq = driver.buildAccCreateReq("", "", "2000.222");
        String[] errorMessages = {"serial must not be blank",  "owner must not be blank;", "balance numeric value out of bounds"};
        driver.sendIncorrectPostReq(ACC_GROUP_URI, accCreateRq);

        // check accounts quantity - one account
        accountsResp = driver.sendCorrectGetReq(ACC_GROUP_URI, AccountsResp.class);
        assertEquals(1, accountsResp.getAccounts().size());

        // check create one more account
        accCreateRq = driver.buildAccCreateReq("765432176MY", "Aleksii", "2000.01");
        accountResp = driver.sendCorrectPostReq(ACC_GROUP_URI, accCreateRq, AccountDto.class);
        assertEquals(accountResp.getSerial(), "765432176MY");
        assertEquals(accountResp.getOwner(), "Aleksii");
        assertEquals(accountResp.getBalance(), BigDecimal.valueOf(2000.01));
        assertEquals(accountResp.getBlocked(), BigDecimal.ZERO);

        // check accounts quantity - two accounts
        accountsResp = driver.sendCorrectGetReq(ACC_GROUP_URI, AccountsResp.class);
        assertEquals(2, accountsResp.getAccounts().size());

        // check get account by serial
        String checkAccountSerial = accountResp.getSerial();
        accountResp = driver.sendCorrectGetReq(ACC_GROUP_URI + "/getBySerial/" + checkAccountSerial, AccountDto.class);
        assertEquals(accountResp.getSerial(), "765432176MY");
        assertEquals(accountResp.getOwner(), "Aleksii");
        assertEquals(accountResp.getBalance(), BigDecimal.valueOf(2000.01));

        // check get account by not existing serial
        driver.sendIncorrectGetReq(ACC_GROUP_URI + "/getBySerial/123456789");
        LOG.info("================== Account CRUD test OK ==================");
    }
}