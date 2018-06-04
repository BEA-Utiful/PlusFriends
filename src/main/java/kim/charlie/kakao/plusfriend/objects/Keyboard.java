package kim.charlie.kakao.plusfriend.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Keyboard {
	private String type = null;
	private List<String> buttons = null;
	
	public Keyboard(String type) {
		this.type = type;
		
		buttons = new ArrayList<>();
	}
	
	public Map<String, Object> getKeyboard() {
		Map<String, Object> keyboard = new HashMap<>();
		
		keyboard.put("type", type);
		if (type.equals("buttons")) {
			keyboard.put("buttons", buttons);
		}
		
		return keyboard;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getButtons() {
		return buttons;
	}

	public void setButtons(List<String> buttons) {
		this.buttons = buttons;
	}
	
	public void addButton(String button) {
		this.buttons.add(button);
	}
	
	public int getButtonSize() {
		return this.buttons.size();
	}
}