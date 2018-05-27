package com.sachin.project2.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sachin.project2.dao.C_BlogDAO;
import com.sachin.project2.dao.C_UserDAO;
import com.sachin.project2.domain.C_Blog;
import com.sachin.project2.domain.C_Blog_Comment;
import com.sachin.project2.domain.C_User;

public class C_BlogController {
	@Autowired
	private C_BlogDAO c_blogDAO;
	@Autowired
	private C_Blog c_blog;
	@Autowired
	private C_Blog_Comment c_blog_comment;
	@Autowired
	private C_User c_user;
	@Autowired
	private C_UserDAO c_userDAO;

	@GetMapping("c_blog/list")
	public ResponseEntity<List<C_Blog>> getAllBlogs()
	{
		return new ResponseEntity<List<C_Blog>>(c_blogDAO.list(), HttpStatus.OK);
	}

	@PostMapping("c_blog/add")
	public ResponseEntity<C_Blog> addBlog(@RequestBody C_Blog c_blog)
	{
		C_Blog j=c_blogDAO.get(c_blog.getBlog_id());
		if(j!=null)
		{
			c_blog=new C_Blog();
			c_blog.setMessage("blog already exist with this blog id");
			return new ResponseEntity<C_Blog>(c_blog, HttpStatus.CONFLICT);
		}
		if(c_blogDAO.save(c_blog))
		{
			c_blog.setMessage("blog added successfully"+c_blog.getBlog_id());
			return new ResponseEntity<C_Blog>(c_blog, HttpStatus.OK);
		}
		else
		{
			c_blog=new C_Blog();
			c_blog.setMessage("Cannot post blog right now with blogid:"+c_blog.getBlog_id()+"please try again later");
			return new ResponseEntity<C_Blog>(c_blog, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
