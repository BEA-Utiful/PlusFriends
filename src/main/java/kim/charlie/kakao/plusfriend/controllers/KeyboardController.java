package kim.charlie.kakao.plusfriend.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kim.charlie.kakao.plusfriend.objects.Keyboard;
import kim.charlie.kakao.plusfriend.objects.DefaultKeyboards;

@RestController
public class KeyboardController {
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public Map<String, Object> keyboard() {
		Keyboard keyboard = new Keyboard("buttons");
		for (DefaultKeyboards option : DefaultKeyboards.values()) {
			keyboard.addButton(option.getOption());
		}
		
		return keyboard.getKeyboard();
	}
}