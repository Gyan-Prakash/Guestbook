
      
		function CheckPassword(inputtxt)   
		{   
			

			if(inputtxt.length<7)
			{
				alert("password must between 7 to 14 charchters");
				document.getElementById('pass').value="";
			}
			
		}

      	 function ValidateEmail(mail)   
			{  
					
					 if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail))  
					  {  
					    return (true)  
					  }  
					  else{
					    alert("You have entered an invalid email address!");
					    document.getElementById('ema').value="";  

					    return (false)  
			}              }

