package com.kindustry.invest.finance;


/**
 * 资产负债表(亦称财务状况表)
 * @author reuben
 *
 */
public class BalanceSheet {

//单位:百万美元 
  private String code;  // 代码
  private String name;  // 名称

  private Float cash; //现金
  private Float cashEquivalents; //现金及现金等价物   Cash And Cash Equivalents
  private Float shortTermInvestment; //短期投资   Short-term investment
  private Float cashAndshortTermInvestment; //现金及短期投资   Cash and short-term investments
  private Float accountsReceivablesTradenet; //应收账款净额  Accounts Receivables - Trade,Net
//其他应收账款 
  private Float totalReceivablesNet;//应收账款总计(净额)  Total Receivables,Net
  private Float totalInventory; //  库存总额  Total Inventory
  private Float prepaidExpenses;  //  预付费用   Prepaid Expenses
  private Integer  OtherCurrentAssetsTotal;    //  其他流动资产合计  Other Current Assets,Total
  private Float TotalCurrentAssets;  //  流动资产总额   Total Current Assets
  
  private String propertyTotal;  //  物业/厂房/设备总计(毛额)  Property/Plant/Equipment,Total - Gross
  private String accumulatedDepreciation;  //  累计折旧总额  Accumulated Depreciation,Total
  private String goodwill;  //  商誉净额  Goodwill,Net
  private String intangibles;  //  无形资产净额  Intangibles,Net
  private Float longTermInvestment;  //  长期投资 Long Term Investment
  private Float noteReceivableLongTerm;  //  应收票据-长期   Note Receivable -Long term
  private Float otherLongTermAssetsTotal;  //  其他长期资产总额  Other Long Term Assets, Total
  private Float totalAssets;  //  资产总额  Total Assets

  /**********************************************************************************************/
  private Float accountsPayable ; //  应付帐款  Accounts Payable 
  private Float accruedExpenses ; //  应付费用  Accrued Expenses
  private Float notesPayableShortTermDebt ; //  应付票据/短期债务  Notes Payable/Short Term Debt
  private Float currentPortofLTDebtCapitalLeases ; //  长期债务中本期到期部分/资本租赁 Current Port. of LT Debt/Capital Leases
  private Float otherCurrentliabilities ; //  其他流动债务总额  Other Current liabilities , Total
  private Float totalCurrentLiabilities ; //  流动债务总额  Total Current Liabilities 
  private Float longTermDebt ; //  长期债务  Long Term Debt
//  资本租赁债务 
  private Float totalLongTermDebt ; //  长期债务总额  Total Long Term Debt
  private Float totalDebt ;//  债务总额   Total Debt = long-term debt plus interest-bearing short-term debt.
  private Float deferredIncomeTax ;//  递延所得税   Deferred income Tax
  private Float minorityInterest ;//  少数股东权益  Minority Interest
  private Float otherLiabilitiesTotal ;//  其他债务总额  Other Liabilities, Total
  private Float totalLiabilities ;//  负债总额  Total Liabilities
  

  /**********************************************************************************************/
   /**  股东权益    **/
//  private Float totalLiabilities ;//  可赎回优先股总计  
//  不可赎回优先股净值 
  private Float commonStockTotal ; //  普通股总计  Common Stock,Total
  private Float additionalPaidInCapital ; //  额外实收资本      Additional Paid-in Capital
  private Float retainedEarnings ; //  留存盈余(累计亏损) Retained Earnings (Accumulated Deficit)
  private Float treasuryStock ; //  库藏股 - 普通股   Treasury Stock - Common
  private Float unrealizedGain ;  //  未实现收益   Unrealized Gain (Loss)
  private Float  otherEquityTotal ; //  其他权益总额  Other Equity,Total
  private Float  totalEquity ;//  *权益总额  Total Equity
  private Float  totalLiabilitesShareholdersEquity ;//  总负债及股东权益   Total Liabilites & Shareholders' Equity
  /**********************************************************************************************/
  
//  已发行股票 - 首次发行普通股 
  private Float totalCommonSharesOutstanding ;//  已发行普通股总计   Total Common Shares Outstanding
  //每股有形账面价值   Tangible Book Value per Share,Common eq


 
}
