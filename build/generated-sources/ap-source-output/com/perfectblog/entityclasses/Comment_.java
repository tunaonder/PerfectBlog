package com.perfectblog.entityclasses;

import com.perfectblog.entityclasses.Post;
import com.perfectblog.entityclasses.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-27T22:31:39")
@StaticMetamodel(Comment.class)
public class Comment_ { 

    public static volatile SingularAttribute<Comment, Integer> id;
    public static volatile SingularAttribute<Comment, Post> postId;
    public static volatile SingularAttribute<Comment, String> commentText;
    public static volatile SingularAttribute<Comment, User> userId;

}