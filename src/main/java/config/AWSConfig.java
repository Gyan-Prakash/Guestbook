package config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;

@Component
@PropertySource("application.properties")

public class AWSConfig {
 
    
    private static String amazonDynamoDBEndpoint;
    private static String AmazonS3Endpoint;
 
    private static String amazonAWSAccessKey;
    //private String amazonAWSAccessKey;
   
    private static  String amazonAWSSecretKey;
    
    
    @Value("${secretkey}")
    public void setSecretKey(String secretkey) {
    	
    	amazonAWSSecretKey = secretkey;
    
    	
    }
    @Value("${accesskey}") 
    public void setAccessKey(String accesskey) {
    	
    	amazonAWSAccessKey = accesskey;
    
    	
    }
    @Value("${endpoint}")
    public void setRegion(String region) {
    	
    	amazonDynamoDBEndpoint = region;
    	AmazonS3Endpoint = region;
    	
    	
    }
 
    public AmazonDynamoDBClient amazonDynamoDB() {
    		AmazonDynamoDBClient amazonDynamoDB 
          = new AmazonDynamoDBClient(amazonAWSCredentials());
         
    		String endpoint = "dynamodb."+amazonDynamoDBEndpoint+".amazonaws.com";
    		amazonDynamoDB.setEndpoint(endpoint);
         
        return amazonDynamoDB;
    }
    
   
 
    public AmazonS3Client amazonS3() {
    	AmazonS3Client amazonS3 
      = new AmazonS3Client(amazonAWSCredentials());
    	String endpoint = "s3."+AmazonS3Endpoint+".amazonaws.com";
     
    	amazonS3.setEndpoint(endpoint);
    return amazonS3;
    }
    public String getS3Endpoint() {
    	System.out.println(AmazonS3Endpoint);
    	return this.AmazonS3Endpoint;
    }
   
    
	private AWSCredentials amazonAWSCredentials() {
    	return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
}
