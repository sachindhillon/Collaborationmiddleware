package com.sachin.project2.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.project2.dao.C_JobDAO;
import com.sachin.project2.dao.C_UserDAO;
import com.sachin.project2.domain.C_Job_Application;
import com.sachin.project2.domain.C_User;

@RestController
public class C_UserController {
	@Autowired
	private C_User c_user;
	@Autowired
	private C_UserDAO c_userDAO;
	
	@Autowired
	private C_JobDAO c_jobDAO;
	
	@Autowired
	private C_Job_Application c_jobApplication;
	
	// http://localhost:8081/CollaborationRestService/
	@GetMapping("/")
	public String serverTest()
	{
		return "server is working fine";
	}
	
	// http://localhost:8081/CollaborationRestService/c_user/list

	@GetMapping("c_user/list")
		public ResponseEntity<List<C_User>> getAllUsers() {
			List<C_User> users = c_userDAO.userList();
			if (users.size() == 0) {
				c_user.setMessage("No users existed in the app");
				
				
			}

			return new ResponseEntity<List<C_User>>(users, HttpStatus.OK);
		}

//	http://localhost:8081/CollaborationRestService/getUser/{email}
	@RequestMapping("/getUser/{email}")
	public ResponseEntity<C_User> getUser(@PathVariable String email)
	{
		C_User c_user = c_userDAO.getUser(email);
		if(c_user == null)
		{
			c_user = new C_User();
			c_user.setMessage("No c_user exists with this email..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.NOT_FOUND);
		}
		else
		{
			c_user.setMessage("C_User Found...");
			return new ResponseEntity<C_User>(c_user, HttpStatus.OK);
		}
	}
	
	
//	http://localhost:8081/CollaborationRestService/validate
	@PostMapping("/validate")
	public ResponseEntity<C_User> validate(@RequestBody C_User c_user)
	{
		c_user = c_userDAO.validateUser(c_user.getUser_email(),c_user.getUser_password());
		if(c_user == null)
		{
			c_user = new C_User();
			c_user.setMessage("Invalid Credentials, Please try again...");
			return new ResponseEntity<C_User>(c_user, HttpStatus.UNAUTHORIZED);
		}
		else
		{
			c_user.setMessage("You Successfully logged in..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.OK);
		}
	}
	
	
//	http://localhost:8081/CollaborationRestService/registerUser
	@PostMapping("/registerUser")
	public ResponseEntity<C_User> registerUser(@RequestBody C_User c_user)
	{
		if(c_userDAO.getUser(c_user.getUser_email()) != null)
		{
			c_user.setMessage("Email already exists.");
			return new ResponseEntity<C_User>(c_user, HttpStatus.CONFLICT);
		}
			
		if(c_userDAO.save(c_user))
		{
			c_user.setMessage("C_User saved Successfully.....");
			return new ResponseEntity<C_User>(c_user, HttpStatus.OK);
		}
		else
		{
			c_user.setMessage("Internal Server Error..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	http://localhost:8081/CollaborationRestService/deleteUser
	@DeleteMapping("/deleteUser/{email}")
	public ResponseEntity<C_User> deleteUser(@PathVariable String email)
	{
		if(c_userDAO.getUser(email) == null)
		{
			C_User c_user = new C_User();
			c_user.setMessage("No c_user exists with this email..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.NOT_FOUND);
		}
		 
		if(c_jobDAO.jobApplicationList(email).size() != 0)
		{
			c_user.setMessage("Couldn't Deleted C_User as this C_User Has Applied for a Job..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.CONFLICT);
		}
		
		if(c_userDAO.delete(email))
		{
			c_user.setMessage("C_User Deleted Successfully");
			return new ResponseEntity<C_User>(c_user, HttpStatus.OK);
		}
		else
		{
			c_user.setMessage("Cannot Delete C_User right now, please try again after some time..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	http://localhost:8081/CollaborationRestService/updateUser
	@PutMapping("/updateUser")
	public ResponseEntity<C_User> updateUser(@RequestBody C_User c_user)
	{
		if(c_userDAO.getUser(c_user.getUser_email()) == null)
		{
			c_user.setMessage("No c_user exists with this email..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.NOT_FOUND);
		}
				
		if(c_userDAO.update(c_user))
		{
			
			c_user.setMessage("C_User updated Successfully..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.OK);
		}
		else
		{
			c_user.setMessage("C_User Not updated Successfully..");
			return new ResponseEntity<C_User>(c_user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	http://localhost:8081/CollaborationRestService/userJobList
	@RequestMapping("/userJobList/{email}")
	public ResponseEntity<List<C_Job_Application>> appliedJobList(@PathVariable String email)
	{
		List<C_Job_Application> userJobList = c_jobDAO.jobApplicationList(email);
		if(userJobList.isEmpty())
		{
			 c_jobApplication.setMessage("You have not applied for any job yet..");
			 userJobList.add(c_jobApplication);
			 return new ResponseEntity<List<C_Job_Application>>(userJobList, HttpStatus.NOT_FOUND);
		}
		else
		{
			 //c_jobApplication.setMessage("C_User applied this job..");
			 return new ResponseEntity<List<C_Job_Application>>(userJobList, HttpStatus.OK);
		}
	}

}
