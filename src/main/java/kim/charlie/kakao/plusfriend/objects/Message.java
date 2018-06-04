package kim.charlie.kakao.plusfriend.objects;

import java.util.HashMap;
import java.util.Map;

public class Message {
	// TODO Photo, MessageButton class should be added
	
	public Message() {
		
	}
	
	private Map<String, Object> getMessage(Object object) {
		Map<String, Object> message = new HashMap<>();
		
		message.put("message", object);
		
		return message;
	}
	
	public Map<String, Object> getMessage(String text) {
		Map<String, String> textMessage = new HashMap<>();
		textMessage.put("text", text);
		
		return getMessage(textMessage);
	}
	
	public Map<String, Object> getMessage(String text, Keyboard keyboard) {
		if (keyboard == null || keyboard.getButtonSize() == 0) {
			return getMessage(text);
		}
		
		Map<String, Object> message = getMessage(text);
		message.put("keyboard", keyboard.getKeyboard());
		
		return message;
	}
}