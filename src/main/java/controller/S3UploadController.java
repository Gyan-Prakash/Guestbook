package controller;


import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import config.AWSConfig;
import model.S3UploadModel;
import services.S3Services;

@Controller
public class S3UploadController {
	
	private AmazonS3 s3;
	private String bucketName="meghaproject";
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/uploadform",method = RequestMethod.POST)
	public String imageUpload(@RequestParam("imageFile") MultipartFile imageFile,Model model) throws IllegalStateException, IOException
	{
		AWSConfig ConfigObject = new AWSConfig();
		s3 = ConfigObject.amazonS3();
		
		String uploadsDir = "/uploads/";
        String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
        if(! new File(realPathtoUploads).exists())
        {
            new File(realPathtoUploads).mkdir();
        }

        
        

        String orgName = imageFile.getOriginalFilename();
        String filePath = realPathtoUploads + orgName;
        File dest = new File(filePath);
        imageFile.transferTo(dest);
        
   
    

		S3Services s3Services = new S3Services();
		
		s3Services.s3ImageDownload(orgName,filePath);
		String endpoint = ConfigObject.getS3Endpoint();
		String url = "https://s3-"+endpoint+".amazonaws.com"+"/"+bucketName+"/"+orgName;
		System.out.println("url"+url);
		S3UploadModel s3Model = new S3UploadModel();
		s3Model.setFilePath(url);
		model.addAttribute("S3UploadModel",s3Model);
		return "S3Processing";	
	}

}
