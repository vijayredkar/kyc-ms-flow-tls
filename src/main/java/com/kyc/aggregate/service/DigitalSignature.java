package com.kyc.aggregate.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lombok.extern.log4j.Log4j;


@Log4j
public class DigitalSignature {
	
	
	//get a keystore  w/ public private key pair
	//
	//create a file with text
	//MsgDgst MD5 hash the file
	//Cipher encrypt RSA the hash
	//create a digi sig
	//save digi sig in a file
	
	// keytool -genkeypair -alias kyckeypair -keyalg RSA -keysize 2048  -validity 365 -keystore kyc-keystore.p12  -ext san=dns:localhost
	static String keyStoreFile = "src/main/resources/kyc-keystore.p12";
	static String certName = "kyckeypair";
	static String ksPassword = "changeit";
	static String fileName     = "src/main/resources/contractAgreement.txt";	
	static byte[] senderSentEncyptedHashByteArr = null;
	
	public static void main(String[] args) 
	{
		
		
		
		createDigitalSignature(keyStoreFile, certName, ksPassword, fileName);
		verifyDigitalSignature(keyStoreFile, certName, ksPassword, fileName, senderSentEncyptedHashByteArr);
		
	}

	private static void createDigitalSignature(String keyStoreFile, String certName, String ksPassword, String fileName) {
	try 
	{
		
		//extract sender's private Key from the keyStore
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(keyStoreFile), ksPassword.toCharArray());
		PrivateKey senderPrivateKey =  (PrivateKey) ks.getKey(certName, ksPassword.toCharArray());
		
		
		
		//construct hash of msg data		
		byte[]  msBytesArr = Files.readAllBytes(Path.of(fileName));
		MessageDigest md =  MessageDigest.getInstance("SHA-256");		
		byte[] hashOfData  = md.digest(msBytesArr);
	
		
		
		//RSA encrypt the hash
		Cipher cph = Cipher.getInstance("RSA");
		
		cph.init(Cipher.ENCRYPT_MODE, senderPrivateKey); //only sender has this private key. Implies only sender can send msg w/ this encryption. Implies sender cannot in the future deny sending this msg
		byte[] byteHashOfData  = cph.doFinal(hashOfData);
		
		
		//save digi sig in a file
		Files.write(Paths.get("sendersDigitalSignature"), byteHashOfData);
		
		senderSentEncyptedHashByteArr = byteHashOfData; 
		
		
		
		System.out.println("---- msBytesArr " + msBytesArr);
		System.out.println("---- hashOfData " + hashOfData);
		System.out.println("---- senderPrivateKey : " + senderPrivateKey);		
		log.info("---- msBytesArr" + msBytesArr);
		log.info("---- hashOfData" + hashOfData);
		
		
	}
	catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | IOException | 
		   NoSuchPaddingException | KeyStoreException | CertificateException | UnrecoverableKeyException | InvalidKeyException e) 
	{
		e.printStackTrace();
	}
 }
	
	private static void verifyDigitalSignature(String keyStoreFile, String certName, String ksPassword, String fileName, byte[] senderSentEncyptedHashByteArr) 
	{
	
		//get the keystore file
		//extract senders public key
		//hash raw msg data
		//Decrypt sender sent hash data
		//compare raw hash w/ decryptd hash
	try 
	{	
		//get senders publicKey from Keystore
		KeyStore ks = KeyStore.getInstance("PKCS12");		
		ks.load(new FileInputStream(keyStoreFile), ksPassword.toCharArray());
		Certificate certificate  = ks.getCertificate(certName);
		PublicKey senderPublicKey = (PublicKey) certificate.getPublicKey();
		
		
		//Hash the raw i/p msg data		
		byte[] fileDataBytesArr = Files.readAllBytes((Path.of(fileName)));		
		MessageDigest md =  MessageDigest.getInstance("SHA-256");
		byte[] fileDataHashByteArr  = md.digest(fileDataBytesArr);
		
		
		
		//Decrypt sender sent encrypted hash for compare
		Cipher cph = Cipher.getInstance("RSA");
		//cph.init(Cipher.DECRYPT_MODE, certificate);
		cph.init(Cipher.DECRYPT_MODE, senderPublicKey);
		byte[] senderSentDecryptedHashByteArr  = cph.doFinal(senderSentEncyptedHashByteArr);
		
		
		
		
		//check if data tampered
		boolean isMatch  = Arrays.equals(fileDataHashByteArr, senderSentDecryptedHashByteArr);
		
		
		System.out.println("---- senderPublicKey " + senderPublicKey);
		System.out.println("---- isMatch " + isMatch);
		
		//Files.write(Path.of("src/main/resources/receivedFile.txt"), fileDataBytesArr);
		
		
		
		
	}
	catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | IOException | 
		   NoSuchPaddingException | KeyStoreException | CertificateException | InvalidKeyException e) 
	{
		e.printStackTrace();
	}
		
		
		
	}
	
	

}
