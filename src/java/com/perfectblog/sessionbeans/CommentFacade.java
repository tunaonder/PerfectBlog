/*
 * Created by Sait Tuna Onder on 2018.02.26  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package com.perfectblog.sessionbeans;

import com.perfectblog.entityclasses.Comment;
import com.perfectblog.entityclasses.Post;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Onder
 */
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {

    @PersistenceContext(unitName = "PerfectBlogPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommentFacade() {
        super(Comment.class);
    }

    public List<Comment> findCommentsByPostId(Integer postID) {

        return (List<Comment>) em.createNamedQuery("Comment.findCommentsByPostId")
                .setParameter("postId", postID)
                .getResultList();
    }
}
