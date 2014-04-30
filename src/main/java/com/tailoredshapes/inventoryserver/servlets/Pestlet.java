package com.tailoredshapes.inventoryserver.servlets;

import com.google.inject.Provider;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.validators.Validator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@Singleton
public class Pestlet<T extends Idable<T>> extends HttpServlet {

    private final Provider<T> provider;
    private final Provider<Responder<T>> responder;
    private final Provider<Responder<Collection<T>>> collectionResponder;
    private final Provider<DAO<T>> dao;
    private final Provider<UrlBuilder<T>> urlBuilder;
    private final Provider<Repository<T, ?>> repository;
    private final Validator<T> validator;

    @Inject
    public Pestlet(Provider<T> provider,
                   Provider<Responder<T>> responder,
                   Provider<Responder<Collection<T>>> collectionResponder,
                   Provider<DAO<T>> dao, Provider<UrlBuilder<T>> urlBuilder,
                   Provider<Repository<T, ?>> repository,
                   Validator<T> validator) {

        this.provider = provider;
        this.responder = responder;
        this.collectionResponder = collectionResponder;
        this.dao = dao;
        this.urlBuilder = urlBuilder;
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        T t = provider.get();
        if (validator.validate(t)) {
            String tUrl = urlBuilder.get().build(t);
            if (!new URL(tUrl).getPath().equals(req.getRequestURI())) {
                resp.sendRedirect(tUrl);
            }

            responder.get().respond(t, resp.getWriter());
        } else {
            collectionResponder.get().respond(repository.get().list(), resp.getWriter());
        }

        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        T t = provider.get();
        if (t.getId() == null) {
            t = dao.get().create(t);
        } else {
            t = dao.get().update(t);
        }

        resp.sendRedirect(urlBuilder.get().build(t));
        responder.get().respond(t, resp.getWriter());
        resp.getWriter().close();
    }
}
