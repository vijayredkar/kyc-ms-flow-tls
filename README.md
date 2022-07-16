# kyc-ms-flow-tls
MS to MS interaction end to end with TLS traffic


1. created 2 microservices secured with SSL/TLS configuration
2. only TLS traffic processing is enforced
3. flow :   Browser --> kyc-aggregator-ssl-client --> kyc-aggregator-ssl-processor
4. testing :
https://localhost:8045/kyc/v1/simpleTrafficTLS
https://localhost/kyc/v1/simpleTrafficTLS
https://localhost:8045/swagger-ui.html
5. generate crypto keypair cmd : 
keytool -genkeypair -alias kyckeypair -keyalg RSA -keysize 2048  -validity 365 -keystore kyc-keystore.p12  -ext san=dns:localhost
6.RestTemplate w/ SSLContext : RestTemplateClientSSLConfig
6. TLS configurations:
server.ssl.enabled=true
server.ssl.key-store-type=PKCS12
server.ssl.key-store=classpath:kyc-keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-alias=kyckeypair
server.ssl.key-password=changeit
server.ssl.protocol=TLS
server.ssl.enabled-protocols=TLSv1.2


