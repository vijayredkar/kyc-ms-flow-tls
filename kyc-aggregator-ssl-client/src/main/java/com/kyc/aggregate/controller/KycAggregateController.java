package com.kyc.aggregate.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyc.aggregate.repo.model.KycRequest;
import com.kyc.aggregate.repo.model.KycResponse;
import com.kyc.aggregate.service.KycAggregateService;

@RestController
@RequestMapping(value = "/kyc/v1/")
public class KycAggregateController {

	Log LOGGER = LogFactory.getLog(KycAggregateController.class);
	
	@Autowired
	KycAggregateService kycAggrSvc;
	
	@Autowired
	ObjectMapper mapper;
		
	@GetMapping(path = "/simpleTrafficTLS")
	public ResponseEntity<String> testSimpleTLSInvoke(@RequestParam(required = false, name = "type") String type ) throws JsonProcessingException
	{				
		LOGGER.info("---- Kyc Aggrregator Mgt initiated testSimpleTLSInvoke :" + type);
		String response = null;
		
		try 
		{	
			String response1 = kycAggrSvc.processKycSSL();
			LOGGER.info("---- Kyc Aggrregator Mgt simpleTrafficTLS succeeded");
			response = response1;
		}
		catch (Exception e) 
		{	
			LOGGER.error("**** Kyc Aggrregator Mgt simpleTrafficTLS failed : "  + e);
			response = "Kyc Aggrregator Mgt simpleTrafficTLS failed";
			
			return new ResponseEntity<String>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}


