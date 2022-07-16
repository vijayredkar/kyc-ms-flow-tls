package com.kyc.aggregate.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kyc.aggregate.exception.KycAggregateException;
import com.kyc.aggregate.repo.model.KycResponse;

@Service
public class KycAggregateService {
	
	Log LOGGER = LogFactory.getLog(KycAggregateService.class);

	@Autowired
    private RestTemplate restTemplate; // w/ SSLContext embedded
	
	public String processKycSSL() throws KycAggregateException  
	{
		KycResponse kycResponse = new KycResponse();
		
    	String response1 = invokeService("https://localhost/kyc/v1/simpleTrafficTLS");// MS-MS call w/ https
    	
		return response1;
	}

	private String invokeService(String url) 
	{	
		LOGGER.info("---- url : "+ url);
		ResponseEntity<String> response1 = restTemplate.getForEntity(url, String.class);		
		LOGGER.info("##### response "+response1);
		
		return response1.getBody();
	}	
	
}
