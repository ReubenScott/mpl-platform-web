package com.kindustry.framework.xsmart;

public class XSmartException extends RuntimeException {
  private static final long serialVersionUID = 2680526523884418916L;

  public XSmartException() {
  }

  public XSmartException(String message, Throwable cause) {
    super(message, cause);
  }

  public XSmartException(String message) {
    super(message);
  }

  public XSmartException(Throwable cause) {
    super(cause);
  }
}