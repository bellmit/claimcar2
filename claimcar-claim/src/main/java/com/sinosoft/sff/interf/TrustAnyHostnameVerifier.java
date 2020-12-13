package com.sinosoft.sff.interf;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class TrustAnyHostnameVerifier implements HostnameVerifier {
	  public boolean verify(String hostname, SSLSession session) {
	   // 直接返回true
	   return true;
	  }
	 }