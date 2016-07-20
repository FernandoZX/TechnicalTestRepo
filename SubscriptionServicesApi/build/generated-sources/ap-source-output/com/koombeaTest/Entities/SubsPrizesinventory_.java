package com.koombeaTest.Entities;

import com.koombeaTest.Entities.Prizes;
import com.koombeaTest.Entities.Store;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-07-19T20:17:02")
@StaticMetamodel(SubsPrizesinventory.class)
public class SubsPrizesinventory_ { 

    public static volatile SingularAttribute<SubsPrizesinventory, Prizes> idPrize;
    public static volatile SingularAttribute<SubsPrizesinventory, Integer> id;
    public static volatile SingularAttribute<SubsPrizesinventory, Integer> stock;
    public static volatile SingularAttribute<SubsPrizesinventory, Store> idStore;
    public static volatile SingularAttribute<SubsPrizesinventory, Boolean> activo;

}