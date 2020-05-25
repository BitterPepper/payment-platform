package com.evolut.payment.bootstrap;

import com.evolut.payment.manager.AccountManager;
import com.evolut.payment.manager.AccountManagerJDBCImpl;
import com.evolut.payment.manager.TransactionManager;
import com.evolut.payment.manager.TransactionManagerJDBCImpl;
import com.evolut.payment.service.AccountService;
import com.evolut.payment.service.AccountServiceImpl;
import com.evolut.payment.service.TransactionService;
import com.evolut.payment.service.TransactionServiceImpl;
import com.evolut.payment.utils.JDBCManager;
import com.evolut.payment.utils.ValidationHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hsqldb.jdbc.JDBCPool;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class AppBindingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(AccountManager.class).to(AccountManagerJDBCImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
        bind(TransactionManager.class).to(TransactionManagerJDBCImpl.class);
        bind(ValidationHelper.class);
    }

    @Provides @Singleton
    public JDBCManager provideJDBCManager() {
        JDBCPool connectionPool = new JDBCPool();
        connectionPool.setUrl("jdbc:hsqldb:res:/hsql/prod/payments");
        return new JDBCManager(connectionPool);
    }
}
