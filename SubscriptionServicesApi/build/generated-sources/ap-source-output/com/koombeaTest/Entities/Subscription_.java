package com.koombeaTest.Entities;

import com.koombeaTest.Entities.Store;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-19T17:31:11")
@StaticMetamodel(Subscription.class)
public class Subscription_ { 

    public static volatile SingularAttribute<Subscription, Date> registeredDate;
    public static volatile SingularAttribute<Subscription, Integer> id;
    public static volatile SingularAttribute<Subscription, Store> idStore;
    public static volatile SingularAttribute<Subscription, String> emailSubscriber;
    public static volatile SingularAttribute<Subscription, Boolean> activo;

}