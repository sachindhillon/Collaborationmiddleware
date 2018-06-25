package com.sachin.project2.restController;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.project2.dao.C_ForumDAO;
import com.sachin.project2.dao.C_UserDAO;
import com.sachin.project2.domain.C_Blog;
import com.sachin.project2.domain.C_Blog_Comment;
import com.sachin.project2.domain.C_Forum;
import com.sachin.project2.domain.C_Forum_Discussion;
import com.sachin.project2.domain.C_User;
@RestController
public class C_ForumController {
	@Autowired
	private C_Forum c_forum;

	@Autowired
	private C_ForumDAO c_forumDAO;

	@Autowired
	private C_User c_user;
	@Autowired
	private C_UserDAO c_userDAO;
	@Autowired
	HttpSession httpSession;

//	http://localhost:8081/CollaborationRestService/c_forum/list
	@GetMapping("c_forum/list")
	public ResponseEntity<List<C_Forum>> getAllForums()
	{
		return new ResponseEntity<List<C_Forum>>(c_forumDAO.list(), HttpStatus.OK);
	}
	
	
	
//	http://localhost:8081/CollaborationRestService/c_forum/approvedForumList
	@GetMapping("c_forum/approvedForumList")
	public ResponseEntity<List<C_Forum>> getApprovedForums()
	{
		return new ResponseEntity<List<C_Forum>>(c_forumDAO.list('A'), HttpStatus.OK);
	}
	
	
// http://localhost:8081/CollaborationRestService/c_forum/add
	@PostMapping("c_forum/add")
	public ResponseEntity<C_Forum> addForum(@RequestBody C_Forum c_forum)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		C_Forum j=c_forumDAO.get(c_forum.getForum_id());
		if(j!=null)
		{
			c_forum=new C_Forum();
			c_forum.setMessage("forum already exist with this forum id");
			return new ResponseEntity<C_Forum>(c_forum, HttpStatus.CONFLICT);
		}
		
		c_forum.setLogin_name(login_name);
		if(c_forumDAO.save(c_forum))
		{
			c_forum.setMessage("forum added successfully"+c_forum.getForum_id());
			return new ResponseEntity<C_Forum>(c_forum, HttpStatus.OK);
		}
		else
		{
			c_forum=new C_Forum();
			c_forum.setMessage("Cannot post forum right now with forumid:"+c_forum.getForum_id()+"please try again later");
			return new ResponseEntity<C_Forum>(c_forum, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// http://localhost:8081/CollaborationRestService/c_forum/incrementLike/{forum_id}
	@GetMapping("c_forum/incrementLike/{forum_id}")
	public ResponseEntity<C_Forum> incrementLike(@PathVariable int forum_id)
	{
		c_forum=c_forumDAO.get(forum_id);
		if(c_forum==null)
		{
			c_forum.setMessage("No forum found with this id");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.NOT_FOUND);
		}
		c_forum.setForum_likes(c_forum.getForum_likes()+1);
		if(c_forumDAO.update(c_forum))
		{
			c_forum.setMessage("forum like is updated successfully ");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.OK);
		}
		else
		{
			c_forum.setMessage("forum like is not updated ");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// http://localhost:8081/CollaborationRestService/c_forum/approveForum/{forum_id}
	@GetMapping("c_forum/approveForum/{forum_id}")
	public ResponseEntity<C_Forum> approveForum(@PathVariable int forum_id)
	{
		c_forum=c_forumDAO.get(forum_id);
		if(c_forum==null)
		{
			c_forum.setMessage("No forum found with this id");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.NOT_FOUND);
		}
		c_forum.setForum_status('A');
		if(c_forumDAO.update(c_forum))
		{
			c_forum.setMessage("forum status is updated successfully ");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.OK);
		}
		else
		{
			c_forum.setMessage("forum status is not updated ");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// http://localhost:8081/CollaborationRestService/c_forum/rejectForum/{forum_id}
	@GetMapping("c_forum/rejectForum/{forum_id}")
	public ResponseEntity<C_Forum> rejectForum(@PathVariable int forum_id)
	{
		c_forum=c_forumDAO.get(forum_id);
		if(c_forum==null)
		{
			c_forum.setMessage("No forum found with this id");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.NOT_FOUND);
		}
		c_forum.setForum_status('R');
		if(c_forumDAO.update(c_forum))
		{
			c_forum.setMessage("forum status is updated successfully ");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.OK);
		}
		else
		{
			c_forum.setMessage("forum status is not updated ");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// http://localhost:8081/CollaborationRestService/c_forum/deleteForum/{forum_id}
	@GetMapping("c_forum/deleteForum/{forum_id}")
	public ResponseEntity<C_Forum> deleteForum(@PathVariable int forum_id)
	{
		if(c_forumDAO.delete(forum_id))
		{
			c_forum.setMessage("forum deleted successfully");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.OK);		
		}
		else
		{
			c_forum.setMessage("forum not deleted");
			return new ResponseEntity<C_Forum> (c_forum,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//http://localhost:8081/CollaborationRestService/c_forum/getForum/{forum_id}
	@GetMapping("c_forum/getForum/{forum_id}")
	public ResponseEntity<C_Forum> getForum(@PathVariable int forum_id)
	{
		C_Forum c_forum=c_forumDAO.get(forum_id);
		httpSession.setAttribute("forumIdForDiscussion",forum_id);
		if(c_forum==null)
		{
			return new ResponseEntity<C_Forum>(c_forum, HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<C_Forum>(c_forum, HttpStatus.OK);
		}
		
	}
	
	//http://localhost:8081/CollaborationRestService/c_forum/addForumDiscussion
	@PostMapping("c_forum/addForumDiscussion")
	public ResponseEntity<C_Forum_Discussion> addForumDiscussion(@RequestBody C_Forum_Discussion c_forum_discussion,HttpSession httpSession)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		C_User c_user=c_userDAO.getUser(login_name);
	
		int blogIdForComment=(Integer) httpSession.getAttribute("forumIdForDiscussion");
		c_forum_discussion.setLogin_name(login_name);
		c_forum_discussion.setForum_id(blogIdForComment);
		//c_blog_comment.setCommentUserName(login_name);
		if(c_forumDAO.save(c_forum_discussion))
		{
			c_forum_discussion.setMessage("comment added successfully"+c_forum_discussion.getForum_id());
			return new ResponseEntity<C_Forum_Discussion>(c_forum_discussion, HttpStatus.OK);
		}
		else
		{
			c_forum_discussion=new C_Forum_Discussion();
			c_forum_discussion.setMessage("Cannot post comment right now with blogid:"+c_forum_discussion.getForum_id()+"please try again later");
			return new ResponseEntity<C_Forum_Discussion>(c_forum_discussion, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//http://localhost:8081/CollaborationRestService/c_forum_discussion/list
		@GetMapping("c_forum_discussion/list/{forum_id}")
		public ResponseEntity<List<C_Forum_Discussion>> getAllForumDiscussions(@PathVariable int forum_id)
		{
			return new ResponseEntity<List<C_Forum_Discussion>>(c_forumDAO.list(forum_id), HttpStatus.OK);
		}
		
	
}
