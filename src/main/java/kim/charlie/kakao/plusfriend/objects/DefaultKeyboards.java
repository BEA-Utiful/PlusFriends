package kim.charlie.kakao.plusfriend.objects;

public enum DefaultKeyboards {
	SEARCH_MOVIE("영화 검색", "영화 제목을 입력해주세요.", new Keyboard("text"));
	
	private String option = null;
	private String response = null;
	private Keyboard keyboard = null;
	
	DefaultKeyboards(String option, String response, Keyboard keyboard) {
		this.option = option;
		this.response = response;
		this.keyboard = keyboard;
	}
	
	public String getOption() {
		return option;
	}
	
	public String getResponse() {
		return response;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
}