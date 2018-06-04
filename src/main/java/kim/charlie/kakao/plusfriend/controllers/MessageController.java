package kim.charlie.kakao.plusfriend.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kim.charlie.kakao.plusfriend.objects.DefaultKeyboards;
import kim.charlie.kakao.plusfriend.objects.Keyboard;
import kim.charlie.kakao.plusfriend.objects.Message;
import kim.charlie.kakao.plusfriend.objects.UserRequestMessage;

@RestController
public class MessageController {
	private static final String TFREECA_URL = "http://www.tfreeca22.com/board.php?b_id=tmovie&mode=list&sc=";
	
	@RequestMapping(value = "/message", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> messageFromUser(@RequestBody UserRequestMessage message) {
		if (message.getType().equals("text")) {
			for (DefaultKeyboards option : DefaultKeyboards.values()) {
				if (message.getContent().equals(option.getOption())) {
					Message responseMessage = new Message();
					return responseMessage.getMessage(option.getResponse(), option.getKeyboard());
				}
			}
			
			return searchMovies(message.getContent());
		}
		
		Message responseMessage = new Message();
		return responseMessage.getMessage("지원하지 않는 기능입니다.");
	}
	
	private Map<String, Object> searchMovies(String title) {
		Document document;
		Keyboard keyboard = new Keyboard("buttons");
		
		try {
			document = Jsoup.connect(TFREECA_URL + URLEncoder.encode(title, "UTF-8")).get();
			
			Elements links = document.select("a[href]");
			for (Element link : links) {
				System.out.println("\nlink : " + link.attr("href"));
				System.out.println("text : " + link.text());
				
				keyboard.addButton(link.text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Message responseMessage = new Message();
		return responseMessage.getMessage("영화(" + title + ")의 검색 결과입니다.", keyboard);
	}
	
	private Map<String, Object> searchMovies2(String title) {
		Document document;
		int count = 0;
		Keyboard keyboard = new Keyboard("buttons");
		
		try {
			document = Jsoup.connect("https://www.google.co.kr/search?q=" + URLEncoder.encode(title, "UTF-8")).get();
			
			Elements links = document.select("div#search a[href]");
			for (Element link : links) {
				String linkHref = link.attr("href");
				
				if (link.text().equals("저장된 페이지") || link.text().trim().length() == 0) {
					continue;
				}
				
				if (linkHref.contains("tfreeca22") && linkHref.contains("tmovie")) {
					System.out.println("\nlink : " + link.attr("href"));
					System.out.println("text : " + link.text());
					
					keyboard.addButton(link.text());
					count += 1;
					
					if (count > 2) {
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Message responseMessage = new Message();
		return responseMessage.getMessage("영화(" + title + ")의 검색 결과입니다.", keyboard);
	}
}