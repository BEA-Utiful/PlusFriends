package kim.charlie.kakao.plusfriend.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kim.charlie.kakao.plusfriend.models.RecentMessageDao;
import kim.charlie.kakao.plusfriend.objects.Friend;

@RestController
public class FriendController {
	@Autowired
	private RecentMessageDao recentMessageDao;
	
	@RequestMapping(value = "/friend", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public void addFriend(@RequestBody Friend friend) throws SQLException {
		recentMessageDao.add(friend.getUser_key());
	}
	
	@RequestMapping(value = "/friend/{user_key}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
	public void deleteFriend(@PathVariable String user_key) throws SQLException {
		recentMessageDao.delete(user_key);
	}
	
	@RequestMapping(value = "/chat_room/{user_key}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
	public void leaveRoom(@PathVariable String user_key) throws SQLException {
		recentMessageDao.leave(user_key);
	}
}
