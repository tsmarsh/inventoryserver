package com.tailoredshapes.inventoryserver.servlets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class Pestlet<T extends Idable<T>> extends HttpServlet {

    private final Provider<T> provider;
    private final Provider<Responder<T>> responder;
    private final Provider<DAO<T>> dao;
    private final Provider<UrlBuilder<T>> urlBuilder;

    @Inject
    public Pestlet(Provider<T> provider, Provider<Responder<T>> responder, Provider<DAO<T>> dao, Provider<UrlBuilder<T>> urlBuilder) {

        this.provider = provider;
        this.responder = responder;
        this.dao = dao;
        this.urlBuilder = urlBuilder;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        responder.get().respond(provider.get(), resp.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        T t = provider.get();
        if (t.getId() == null) {
            t = dao.get().create(t);
        } else {
            t = dao.get().update(t);
        }

        responder.get().respond(t, resp.getOutputStream());
        resp.sendRedirect(urlBuilder.get().build(t));
    }
}
