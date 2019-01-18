package com.kindustry.invest.bond.dao.impl;

import com.kindustry.framework.dao.imp.BaseDaoImpl;
import com.kindustry.framework.orm.BaseEntity;
import com.kindustry.invest.bond.dao.BondDao;
import com.kindustry.invest.bond.model.Bond;

public class BondDaoImpl extends BaseDaoImpl<BaseEntity> implements BondDao {

  public void addBond(Bond bond) {
    add(bond);
  }

  @Override
  public boolean wipeBonds() {
    wipe(Bond.class);
    return true;
  }
}
