package com.sachin.project2.restController;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import com.sachin.project2.domain.C_Job;
import com.sachin.project2.domain.C_Job_Application;
import com.sachin.project2.domain.C_User;

@RestController
public class C_JobController {
	
	@Autowired
	private C_Job c_job;
	@Autowired
	private C_JobDAO c_jobDAO;
	@Autowired
	private C_User c_user;
	@Autowired
	private C_UserDAO c_userDAO;
	@Autowired
	private C_Job_Application c_job_application;
	@Autowired
	HttpSession httpSession;
	
	// http://localhost:8081/CollaborationRestService/c_job/list
	@GetMapping("c_job/list")
	public ResponseEntity<List<C_Job>> getAllJobs()
	{
	return new ResponseEntity<List<C_Job>>(c_jobDAO.list(),HttpStatus.OK);	
	}
	
	// http://localhost:8081/CollaborationRestService/c_job/appliedUsers/{job_id}
		@GetMapping("c_job/appliedUsers/{job_id}")
		public ResponseEntity<List<C_Job_Application>> getAppliedUsers(@PathVariable int job_id)
		{
		return new ResponseEntity<List<C_Job_Application>>(c_jobDAO.list(job_id),HttpStatus.OK);	
		}
	
//	http://localhost:8081/CollaborationRestService/c_job/saveJob
	@PostMapping("c_job/saveJob")
	public ResponseEntity<C_Job> saveJob(@RequestBody C_Job c_job)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		C_Job j = c_jobDAO.get(c_job.getJob_id());
		if(j != null)
		{
			c_job = new C_Job();
			c_job.setMessage("C_Job already exist with Jobid: "+c_job.getJob_id());
			return new ResponseEntity<C_Job>(c_job, HttpStatus.CONFLICT);
		}
		
	
		if(c_jobDAO.save(c_job))
		{
			c_job.setMessage("C_Job Saved Successfully");
			return new ResponseEntity<C_Job>(c_job, HttpStatus.OK);
		}
		else
		{
			c_job = new C_Job();
			c_job.setMessage("Cannot post C_Job right now  please try again later..");
			return new ResponseEntity<C_Job>(c_job, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//http://localhost:8081/CollaborationRestService/getJobListByStatus/{jobstatus}
	@RequestMapping("/getJobListByStatus/{jobstatus}")
	public ResponseEntity<List<C_Job>> getJobListByStatus(@PathVariable char jobstatus)
	{
		List<C_Job> jobs = c_jobDAO.list(jobstatus);
		if(jobs.isEmpty())
		{
			jobs.add(c_job);
			c_job.setMessage("No job found with JobStatus: "+jobstatus);
			return new ResponseEntity<List<C_Job>>(jobs, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<C_Job>>(jobs, HttpStatus.OK);
	}
	
	//http://localhost:8081/CollaborationRestService/getJob/{jobid}
	@RequestMapping("/getJob/{jobid}")
	public ResponseEntity<C_Job> getJob(@PathVariable int jobid)
	{
		c_job = c_jobDAO.get(jobid);
		if(c_job == null)
		{
			c_job = new C_Job();
			c_job.setMessage("No c_job is found with JobID: "+jobid);
			return new ResponseEntity<C_Job>(c_job, HttpStatus.NOT_FOUND);
		}
		else
		{
			c_job.setMessage("This is the particular C_Job Details of JobID: "+jobid);
			return new ResponseEntity<C_Job>(c_job, HttpStatus.OK);
		}
	}
	//  http://localhost:8081/CollaborationRestService/update/{job_id}/{status}
	@GetMapping("/update/{job_id}/{status}")
	public ResponseEntity<C_Job> update(@PathVariable int job_id,@PathVariable char status)
	{
		c_job=c_jobDAO.get(job_id);
		if(c_job==null)
		{
			c_job.setMessage("no job found with this job id.");
			return new ResponseEntity<C_Job>(c_job,HttpStatus.NOT_FOUND);
		}
		
		c_job.setJob_status(status);
		if(c_jobDAO.update(c_job))
		{
			c_job.setMessage("status is successfully updated");
			return new ResponseEntity<C_Job>(c_job, HttpStatus.OK);
		}
		c_job.setMessage("status not updated");
		return new ResponseEntity<C_Job>(c_job, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
	
	
	
	//http://localhost:8081/CollaborationRestService/deleteJob/{jobid}	
		@DeleteMapping("/deleteJob/{job_id}")
		public ResponseEntity<C_Job> deleteJob(@PathVariable int job_id)
		{
			C_Job j = c_jobDAO.get(job_id);
			if(j == null)
			{
				c_job = new C_Job();
				c_job.setMessage("C_Job doesn't Exists with JobID: "+job_id);
				return new ResponseEntity<C_Job>(c_job, HttpStatus.CONFLICT);
			}
			
			List<C_Job_Application> appliedJobs = c_jobDAO.list(job_id);
			if(!appliedJobs.isEmpty())
			{
				c_job = new C_Job();
				c_job.setMessage("Users have applied for this c_job, we cannot delete this c_job..");
				return new ResponseEntity<C_Job>(c_job, HttpStatus.CONFLICT);
			}
			
			if(c_jobDAO.deleteJob(job_id))
			{
				c_job.setMessage("C_Job Deleted Successfully with Jobid: "+job_id);
				return new ResponseEntity<C_Job>(c_job, HttpStatus.OK);
			}
			else
			{
				c_job = new C_Job();
				c_job.setMessage("Cannot delete C_Job with Jobid: "+job_id+", please try again later..");
				return new ResponseEntity<C_Job>(c_job, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		
		
		
	// related to JOB APPLICATION
		

		
//		http://localhost:8081/CollaborationRestService/jobRegistration/{job_id}
		@PostMapping("/jobRegistration/{job_id}")
		public ResponseEntity<C_Job_Application> jobRegistration(@PathVariable int job_id)
		{
			String login_name=(String) httpSession.getAttribute("login_name");
			String emailid=(String) httpSession.getAttribute("useremail");
			c_job_application.setJob_id(job_id);
			c_job_application.setEmailid(emailid);
			C_Job jobb=	c_jobDAO.get(job_id);
			c_job_application.setJobapp_title(jobb.getJob_title());
			c_job_application.setLogin_name(login_name);
			// to check user exists or not
			if(c_userDAO.getUser(c_job_application.getLogin_name())==null)
			{
				c_job_application.setMessage("no user found with this login name");
				return new ResponseEntity<C_Job_Application>(c_job_application, HttpStatus.NOT_FOUND);
			}
			
			// if the c_Job does not exist, you can not apply
						if (c_jobDAO.get(c_job_application.getJob_id()) == null) {
							c_job_application.setMessage("no job found with this job id");
							return new ResponseEntity<C_Job_Application>(c_job_application, HttpStatus.NOT_FOUND);
						}
			
			
			if (!c_jobDAO.isJobOpened(c_job_application.getJob_id())) {
				c_job_application.setMessage("no job opened with this job id");
				return new ResponseEntity<C_Job_Application>(c_job_application, HttpStatus.NOT_FOUND);
			}
			// if you already applied, you can not apply again
			if (c_jobDAO.get(c_job_application.getLogin_name(), c_job_application.getJob_id())!=null) {
				c_job_application.setMessage("user already applied job with this emailid");
				return new ResponseEntity<C_Job_Application>(c_job_application, HttpStatus.NOT_ACCEPTABLE);
			}
			if(c_jobDAO.save(c_job_application))
			{
				c_job_application.setMessage("Registered Successfully..");
				return new ResponseEntity<C_Job_Application>(c_job_application, HttpStatus.OK);
			}
			else
			{
				c_job_application.setMessage("Cannot Register now please try again later..");
				return new ResponseEntity<C_Job_Application>(c_job_application, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// http://localhost:8081/CollaborationRestService/c_job/applicationList
		@GetMapping("c_job/applicationList")
		public ResponseEntity<List<C_Job_Application>> getAllJobsApplications()
		{
		return new ResponseEntity<List<C_Job_Application>>(c_jobDAO.applicationlist(),HttpStatus.OK);	
		}
		
		// http://localhost:8081/CollaborationRestService/c_job/approveApplication/
		@PutMapping("c_job/approveApplication/{jobApp_Id}")
		public ResponseEntity<C_Job_Application> approveApplications(@PathVariable int jobApp_Id)
		{
			
			C_Job_Application app=	c_jobDAO.getApplication(jobApp_Id);
			app.setJobApp_status('A');
			app.setReason("not mentioned");
			if(c_jobDAO.update(app))
			{
				return new ResponseEntity<C_Job_Application>(app,HttpStatus.OK);
			}
			return new ResponseEntity<C_Job_Application>(app,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// http://localhost:8081/CollaborationRestService/c_job/rejectApplication/
		@PutMapping("c_job/rejectApplication/{jobApp_Id}")
		public ResponseEntity<C_Job_Application> rejectApplications(@PathVariable int jobApp_Id)
		{
			
			C_Job_Application app=	c_jobDAO.getApplication(jobApp_Id);
			app.setJobApp_status('R');
			app.setReason("not mentioned");
			if(c_jobDAO.update(app))
			{
				return new ResponseEntity<C_Job_Application>(app,HttpStatus.OK);
			}
			return new ResponseEntity<C_Job_Application>(app,HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
