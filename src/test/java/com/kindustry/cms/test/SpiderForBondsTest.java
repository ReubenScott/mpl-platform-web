package com.kindustry.cms.test;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.kindustry.invest.bond.dao.BondDao;
import com.kindustry.invest.bond.dao.impl.BondDaoImpl;
import com.kindustry.invest.bond.model.Bond;
import com.kindustry.network.spider.core.item.SpiderForBonds;

public class SpiderForBondsTest {

  @Test
  public void testGetBonds() {
    SpiderForBonds splider = new SpiderForBonds();
    List<Bond> bonds = splider.getBonds();
    BondDao bondDao = new BondDaoImpl();
    bondDao.wipeBonds();
    for (Bond bond : bonds) {
      bondDao.addBond(bond);
    }

  }

//  public void batchAddExamlog(List examlogList) throws SQLException {
//    SqlMapClient smc = this.getSqlMapClient();
//    try {
//      smc.startTransaction();
//      smc.startBatch();
//      for (Iterator iter = examlogList.iterator(); iter.hasNext();) {
//        Examlog log = (Examlog) iter.next();
//        smc.update("insertExamlog", log);
//      }
//      smc.executeBatch();
//    } finally {
//      smc.commitTransaction();
//      smc.endTransaction();
//    }
//  }

}
