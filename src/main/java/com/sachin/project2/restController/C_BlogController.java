

package com.sachin.project2.restController;

import java.util.List;

import javax.persistence.metamodel.SetAttribute;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.project2.dao.C_BlogDAO;
import com.sachin.project2.dao.C_UserDAO;
import com.sachin.project2.domain.C_Blog;
import com.sachin.project2.domain.C_Blog_Comment;
import com.sachin.project2.domain.C_User;

@RestController
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
	@Autowired
	HttpSession httpSession;

//	http://localhost:8081/CollaborationRestService/c_blog/list
	@GetMapping("c_blog/list")
	public ResponseEntity<List<C_Blog>> getAllBlogs()
	{
		return new ResponseEntity<List<C_Blog>>(c_blogDAO.list(), HttpStatus.OK);
	}
	
	//http://localhost:8081/CollaborationRestService/c_blogComment/list
	@GetMapping("c_blogComment/list/{blog_id}")
	public ResponseEntity<List<C_Blog_Comment>> getAllBlogComments(@PathVariable int blog_id)
	{
		return new ResponseEntity<List<C_Blog_Comment>>(c_blogDAO.list(blog_id), HttpStatus.OK);
	}
	
//	http://localhost:8081/CollaborationRestService/c_blog/approvedBlogList
	@GetMapping("c_blog/approvedBlogList")
	public ResponseEntity<List<C_Blog>> getApprovedBlogs()
	{
		return new ResponseEntity<List<C_Blog>>(c_blogDAO.list('A'), HttpStatus.OK);
	}
	
	
// http://localhost:8081/CollaborationRestService/c_blog/add
	@PostMapping("c_blog/add")
	public ResponseEntity<C_Blog> addBlog(@RequestBody C_Blog c_blog,HttpSession httpSession)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		C_Blog j=c_blogDAO.get(c_blog.getBlog_id());
		if(j!=null)
		{
			c_blog=new C_Blog();
			c_blog.setMessage("blog already exist with this blog id");
			return new ResponseEntity<C_Blog>(c_blog, HttpStatus.CONFLICT);
		}
	c_blog.setLogin_name(login_name);
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
	// http://localhost:8081/CollaborationRestService/c_blog/incrementLike/{blog_id}
	@GetMapping("c_blog/incrementLike/{blog_id}")
	public ResponseEntity<C_Blog> incrementLike(@PathVariable int blog_id)
	{
		c_blog=c_blogDAO.get(blog_id);
		if(c_blog==null)
		{
			c_blog.setMessage("No blog found with this id");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.NOT_FOUND);
		}
		c_blog.setBlog_likes(c_blog.getBlog_likes()+1);
		if(c_blogDAO.update(c_blog))
		{
			c_blog.setMessage("blog like is updated successfully ");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.OK);
		}
		else
		{
			c_blog.setMessage("blog like is not updated ");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// http://localhost:8081/CollaborationRestService/c_blog/approveBlog/{blog_id}
	@GetMapping("c_blog/approveBlog/{blog_id}")
	public ResponseEntity<C_Blog> approveBlog(@PathVariable int blog_id)
	{
		c_blog=c_blogDAO.get(blog_id);
		if(c_blog==null)
		{
			c_blog.setMessage("No blog found with this id");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.NOT_FOUND);
		}
		c_blog.setBlog_status('A');
		if(c_blogDAO.update(c_blog))
		{
			c_blog.setMessage("blog status is updated successfully ");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.OK);
		}
		else
		{
			c_blog.setMessage("blog status is not updated ");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// http://localhost:8081/CollaborationRestService/c_blog/rejectBlog/{blog_id}
	@GetMapping("c_blog/rejectBlog/{blog_id}")
	public ResponseEntity<C_Blog> rejectBlog(@PathVariable int blog_id)
	{
		c_blog=c_blogDAO.get(blog_id);
		if(c_blog==null)
		{
			c_blog.setMessage("No blog found with this id");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.NOT_FOUND);
		}
		c_blog.setBlog_status('R');
		if(c_blogDAO.update(c_blog))
		{
			c_blog.setMessage("blog status is updated successfully ");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.OK);
		}
		else
		{
			c_blog.setMessage("blog status is not updated ");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// http://localhost:8081/CollaborationRestService/c_blog/deleteBlog/{blog_id}
	@GetMapping("c_blog/deleteBlog/{blog_id}")
	public ResponseEntity<C_Blog> deleteBlog(@PathVariable int blog_id)
	{
		if(c_blogDAO.delete(blog_id))
		{
			c_blog.setMessage("blog deleted successfully");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.OK);		
		}
		else
		{
			c_blog.setMessage("blog not deleted");
			return new ResponseEntity<C_Blog> (c_blog,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// http://localhost:8081/CollaborationRestService/c_blog/getBlog/{blog_id}
	@GetMapping("c_blog/getBlog/{blog_id}")
	public ResponseEntity<C_Blog> getBlog(@PathVariable int blog_id)
	{
		C_Blog c_blog=c_blogDAO.get(blog_id);
		
		
		httpSession.setAttribute("blogIdForComment", blog_id);
		if(c_blog==null)
		{
			return new ResponseEntity<C_Blog>(c_blog, HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<C_Blog>(c_blog, HttpStatus.OK);
		}
	
	}
	
	@PostMapping("c_blog/addBlogComment")
	public ResponseEntity<C_Blog_Comment> addBlogComment(@RequestBody C_Blog_Comment c_blog_comment,HttpSession httpSession)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		C_User c_user=c_userDAO.getUser(login_name);
		String username=(String) c_user.getUsername();
		int blogIdForComment=(Integer) httpSession.getAttribute("blogIdForComment");
		c_blog_comment.setLogin_name(login_name);
		c_blog_comment.setBlog_id(blogIdForComment);
		//c_blog_comment.setCommentUserName(login_name);
		if(c_blogDAO.save(c_blog_comment))
		{
			c_blog_comment.setMessage("comment added successfully"+c_blog_comment.getBlog_id());
			return new ResponseEntity<C_Blog_Comment>(c_blog_comment, HttpStatus.OK);
		}
		else
		{
			c_blog_comment=new C_Blog_Comment();
			c_blog_comment.setMessage("Cannot post comment right now with blogid:"+c_blog_comment.getBlog_id()+"please try again later");
			return new ResponseEntity<C_Blog_Comment>(c_blog_comment, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
