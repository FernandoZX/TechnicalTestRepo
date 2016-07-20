package com.koombeaTest.Entities;

import com.koombeaTest.Entities.Subscription;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-19T20:17:02")
@StaticMetamodel(Store.class)
public class Store_ { 

    public static volatile SingularAttribute<Store, String> descripcion;
    public static volatile ListAttribute<Store, Subscription> subscriptionList;
    public static volatile SingularAttribute<Store, Integer> id;
    public static volatile SingularAttribute<Store, String> nombre;
    public static volatile SingularAttribute<Store, Boolean> activo;

}