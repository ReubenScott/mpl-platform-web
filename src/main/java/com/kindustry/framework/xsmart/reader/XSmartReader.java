package com.kindustry.framework.xsmart.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.kindustry.framework.xsmart.XSmartField;

public abstract interface XSmartReader {
  public abstract Map<String, List<XSmartField>> read(InputStream paramInputStream) throws IOException;
}