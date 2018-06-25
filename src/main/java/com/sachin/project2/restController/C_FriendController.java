package com.sachin.project2.restController;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.project2.dao.C_FriendDAO;
import com.sachin.project2.dao.C_UserDAO;
import com.sachin.project2.domain.C_Friend;
import com.sachin.project2.domain.C_User;

@RestController
public class C_FriendController {
	
	@Autowired
	private C_FriendDAO c_friendDAO;
	
	@Autowired
	private C_Friend c_friend;
	
	@Autowired
	private C_UserDAO c_userDAO;
	
	@Autowired
	private C_User c_user;
	
//	http://localhost:8081/CollaborationRestService/userFriendList
	@RequestMapping("/userFriendList")
	public ResponseEntity<List<C_Friend>> userFriendList(HttpSession httpSession)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		return new ResponseEntity<List<C_Friend>>(c_friendDAO.friendsList(login_name),HttpStatus.OK);
	}

	
//	http://localhost:8081/CollaborationRestService/userSuggestedFriendList
	@RequestMapping("/userSuggestedFriendList")
	public ResponseEntity<List<C_User>> userSuggestedFriendList(HttpSession httpSession)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		return new ResponseEntity<List<C_User>>(c_friendDAO.suggestedFriendList(login_name),HttpStatus.OK);
	}
	
	
//	http://localhost:8081/CollaborationRestService/userPendingFriendRequestList
	@RequestMapping("/userPendingFriendRequestList")
	public ResponseEntity<List<C_Friend>> userPendingFriendRequestList(HttpSession httpSession)
	{
		String login_name=(String) httpSession.getAttribute("login_name");
		return new ResponseEntity<List<C_Friend>>(c_friendDAO.pendingFriendRequest(login_name),HttpStatus.OK);
	}
	
	
	
	
//	http://localhost:8081/CollaborationRestService/sendFriendRequest/{friend_email}/{friend_name}
	@RequestMapping(value="/sendFriendRequest/{friend_name}",method=RequestMethod.POST)
	public ResponseEntity<C_Friend> sendFriendRequest(@PathVariable String friend_name, HttpSession httpSession)
	{
		
		String login_name=(String) httpSession.getAttribute("login_name");
		//C_User c_user=c_userDAO.getUser(login_name);
		
	
		c_friend.setFriend_name(friend_name);
		
		c_friend.setLogin_name(login_name);
		if(c_friendDAO.sendFriendRequest(c_friend))
		{
			c_friend.setMessage("friend request sent");
			return new ResponseEntity<C_Friend>(c_friend, HttpStatus.OK);
		}
		else
		{
			c_friend.setMessage("friend request not sent duue to some problem");
			return new ResponseEntity<C_Friend>(c_friend, HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
//	http://localhost:8081/CollaborationRestService/acceptFriendRequest
	@RequestMapping(value="/acceptFriendRequest/{friend_id}",method=RequestMethod.POST)
	public ResponseEntity<C_Friend> acceptFriendRequest(@PathVariable int friend_id)
	{
		if(c_friendDAO.acceptFriendRequest(friend_id))
		{
		return new ResponseEntity<C_Friend>(c_friend, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<C_Friend>(c_friend, HttpStatus.NOT_FOUND);
		}
	}
//	http://localhost:8081/CollaborationRestService/deleteFriendRequest
	@RequestMapping(value="/deleteFriendRequest/{friend_id}",method=RequestMethod.POST)
	public ResponseEntity<C_Friend> deleteFriendRequest(@PathVariable int friend_id)
	{
		if(c_friendDAO.deleteFriendRequest(friend_id))
	
		{
	return new ResponseEntity<C_Friend>(c_friend, HttpStatus.OK);
		}
		else
		{
		return new ResponseEntity<C_Friend>(c_friend, HttpStatus.NOT_FOUND);
		}
}
}
