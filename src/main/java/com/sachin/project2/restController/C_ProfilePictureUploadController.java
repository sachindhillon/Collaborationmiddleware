package com.sachin.project2.restController;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sachin.project2.dao.C_ProfilePictureDAO;
import com.sachin.project2.dao.C_UserDAO;
import com.sachin.project2.domain.C_ProfilePicture;
import com.sachin.project2.domain.C_User;

@RestController
public class C_ProfilePictureUploadController {
	
	@Autowired
	private C_ProfilePictureDAO c_profilepictureDAO;
	
	@Autowired
	private C_ProfilePicture c_profilepicture;
	
	@Autowired
	private C_UserDAO c_userDAO;
	
	// http:localhost:8081/CollaborationRestService/profilePicUpload
	@RequestMapping(value="/profilePicUpload", method=RequestMethod.POST)
	public ResponseEntity<?> saveprofilePic(@RequestParam(value="file") CommonsMultipartFile fileUpload,HttpSession httpSession)
	{
		String login_name = (String) httpSession.getAttribute("login_name");
		C_User c_user = c_userDAO.getUser(login_name);
		if(c_user==null)
		{
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
		c_profilepicture.setProfile_pic(fileUpload.getBytes());
		
		c_profilepicture.setLogin_name(login_name);
		
		if(c_profilepictureDAO.save(c_profilepicture))
		{
			return new ResponseEntity<Void>(HttpStatus.OK);	
		}
		else
		{
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value="/getProfilePic/{login_name}", method=RequestMethod.GET)
	public @ResponseBody byte[] getProfilePicture(@PathVariable String login_name)
	{
		
		C_User c_user = c_userDAO.getUser(login_name);
		C_ProfilePicture c_profilepicture=c_profilepictureDAO.get(login_name);
		
		if(c_profilepicture==null)
		{
			return null;
		}
		else
		{
			byte[] image = c_profilepicture.getProfile_pic();
			return image;
		}
	}

	@RequestMapping(value="/getProfileFriends/{login_name}", method=RequestMethod.GET)
	public @ResponseBody byte[] getProfileFriends(@PathVariable String login_name)
	{
		
		C_User c_user = c_userDAO.get(login_name);
		System.out.println(login_name);
		System.out.println(c_user.getUser_email());
		String mail=c_user.getUser_email();
		C_ProfilePicture profile = c_profilepictureDAO.get(login_name);
		if(profile==null)
		{
			return null;
		}
		else
		{
			byte[] image = profile.getProfile_pic();
			return image;
		}
	}
	
	
}
