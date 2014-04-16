package com.tailoredshapes.inventoryserver.filters;

import javax.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.*;
import java.io.IOException;

@Singleton
public class TransactionFilter implements Filter{

    private Provider<EntityManager> manager;

    @Inject
    public TransactionFilter(Provider<EntityManager> manager) {
        this.manager = manager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        EntityTransaction transaction = manager.get().getTransaction();
        transaction.begin();
        try{
            filterChain.doFilter(servletRequest, servletResponse);
            transaction.commit();
        }
        catch(Exception e){
            transaction.rollback();
        }
    }

    @Override
    public void destroy() {

    }
}
