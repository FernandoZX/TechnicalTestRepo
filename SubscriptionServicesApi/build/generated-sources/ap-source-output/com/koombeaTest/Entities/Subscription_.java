package com.koombeaTest.Entities;

import com.koombeaTest.Entities.Store;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-22T12:38:38")
@StaticMetamodel(Subscription.class)
public class Subscription_ { 

    public static volatile SingularAttribute<Subscription, Date> registereddate;
    public static volatile SingularAttribute<Subscription, Integer> id;
    public static volatile SingularAttribute<Subscription, Store> idStore;
    public static volatile SingularAttribute<Subscription, String> email;
    public static volatile SingularAttribute<Subscription, Boolean> activo;

}