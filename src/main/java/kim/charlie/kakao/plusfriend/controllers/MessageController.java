package kim.charlie.kakao.plusfriend.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kim.charlie.kakao.plusfriend.models.RecentMessageDao;
import kim.charlie.kakao.plusfriend.objects.DefaultKeyboards;
import kim.charlie.kakao.plusfriend.objects.Keyboard;
import kim.charlie.kakao.plusfriend.objects.Message;
import kim.charlie.kakao.plusfriend.objects.UserRequestMessage;

@RestController
public class MessageController {
	private static final String TFREECA_URL = "http://www.tfreeca22.com/board.php?b_id=tmovie&mode=list&sc=";
	
	@Autowired
	private RecentMessageDao recentMessageDao;
	
	@RequestMapping(value = "/message", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> messageFromUser(@RequestBody UserRequestMessage message) throws SQLException {
		if (message.getType().equals("text")) {
			for (DefaultKeyboards option : DefaultKeyboards.values()) {
				if (message.getContent().equals(option.getOption())) {
					recentMessageDao.deleteAll(message.getUser_key());
					
					Message responseMessage = new Message();
					return responseMessage.getMessage(option.getResponse(), option.getKeyboard());
				}
			}
			
			if (recentMessageDao.getCount(message.getUser_key()) == 0) {
				return searchMovies(message);				
			} else {
				return downloadMovies(message.getContent());
			}
		}
		
		Message responseMessage = new Message();
		return responseMessage.getMessage("지원하지 않는 기능입니다.");
	}
	
	private Map<String, Object> searchMovies(UserRequestMessage message) throws SQLException {
		String title = message.getContent();
		
		Document document;
		Keyboard keyboard = new Keyboard("buttons");
		
		try {
			document = Jsoup.connect(TFREECA_URL + URLEncoder.encode(title, "UTF-8")).get();
			
			Elements links = document.select("table.b_list a[class^=\"stitle\"]");
			for (Element link : links) {
				System.out.println("\nlink : " + link.attr("href"));
				System.out.println("text : " + link.text());
				
				if (link.text().length() < 128 && link.attr("href").length() < 128) {
					recentMessageDao.addSearch(message.getUser_key(), link.text(), link.attr("href"));
				}
				
				keyboard.addButton(link.text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Message responseMessage = new Message();
		return responseMessage.getMessage(keyboard.getButtonSize() == 0 ? "검색 결과가 없습니다. 다시 시도해 주세요." : "영화(" + title + ")의 검색 결과입니다.", keyboard);
	}
	
	private Map<String, Object> downloadMovies(String search_text) {
		Message responseMessage = new Message();
		return responseMessage.getMessage(search_text + "의 다운로드를 시작합니다.");
	}
}