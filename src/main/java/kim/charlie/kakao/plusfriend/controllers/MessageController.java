package kim.charlie.kakao.plusfriend.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
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

import javax.net.ssl.HttpsURLConnection;

@RestController
public class MessageController {
	private static final String TFREECA_HOST = "http://www.tfreeca22.com/";
	private static final String TFREECA_URL = TFREECA_HOST + "board.php?b_id=tmovie&mode=list&sc=";
	
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
				return downloadMovies(message);
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
	
	private Map<String, Object> downloadMovies(UserRequestMessage message) throws SQLException {
		String link = TFREECA_HOST + recentMessageDao.getSearchLink(message.getUser_key(), message.getContent());
		String parameters = link.split("[?]")[1];
		Map<String, String> parametersMap = new HashMap<>();

		for (String parameter : parameters.split("&")) {
			String[] keyAndValue = parameter.split("=");

			parametersMap.put(keyAndValue[0], keyAndValue[1]);
		}

		String downloadUrl = "https://charlie.kim/tfreeca/download.php?b_id=" + parametersMap.get("b_id") + "&id=" + parametersMap.get("id");
		System.out.println(downloadUrl);

		downloadFile(downloadUrl, "/Users/Charlie/Downloads");

/*
		Document document;
		
		try {
			document = Jsoup.connect(link).get();
			
			Elements elements = document.select("td.view_t4 a[href]");
			for (Element element : elements) {
				if (element.text().contains(".torrent")) {
					System.out.println(element.text());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		Message responseMessage = new Message();
		return responseMessage.getMessage(message.getContent() + "의 다운로드를 시작합니다.");
	}

	private void downloadFile(String fileUrl, String destination) {
	    try {
            URL url = new URL(fileUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            int responseCode = httpsURLConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String filename = "";
                String disposition = httpsURLConnection.getHeaderField("Content-Disposition");
                String contentType = httpsURLConnection.getContentType();
                int contentLength = httpsURLConnection.getContentLength();

                if (disposition != null) {
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        filename = disposition.substring(index + 10, disposition.length() - 1);
                    }
                } else {
                    filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
                }

                InputStream inputStream = httpsURLConnection.getInputStream();
                String filePath = destination + File.separator + filename;

                FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                inputStream.close();
            }

            httpsURLConnection.disconnect();
        } catch (IOException e) {
	        e.printStackTrace();
        }
    }
}