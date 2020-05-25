package com.evolut.payment.bootstrap;

import com.evolut.payment.endpoint.TransactionProvider;
import com.evolut.payment.utils.JDBCManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.evolut.payment.endpoint.AccountProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class RestAppBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(AccountProvider.class);

    Injector injector;

    public static void main(String[] args) {
        new RestAppBootstrap().init();
    }

    public void init (){
        injector = Guice.createInjector(new AppBindingModule());
        injector.getInstance(AccountProvider.class).init();
        injector.getInstance(TransactionProvider.class).init();
        setupAdditionalProperties();
    }

    public void destroy (){
        injector.getInstance(JDBCManager.class).cleanAndCloseDB();
        stop();
    }

    private static void setupAdditionalProperties() {
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"This page does not exist. Try a different address\"}";
        });
        //TODO add more verbose LOG
        before("/*", (request, response) -> {
            LOG.info("Received api call: " + request.url());
        });
        after((req, res) -> {
            res.header("Content-Encoding", "gzip");
            res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            res.header("Access-Control-Max-Age", "1800");
            res.type("application/json");
        });

        exception(Exception.class, (e, req, res) -> {
            LOG.error(e.getLocalizedMessage(), e);
            res.status(500);
            res.body(e.getMessage());
        });
    }
}
