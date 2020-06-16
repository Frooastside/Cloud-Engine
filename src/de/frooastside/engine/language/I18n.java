package de.frooastside.engine.language;

import java.io.File;

public class I18n {
	
	private static I18n instance;
	private LanguageManager languageManager;
	private Language language;
	
	public I18n(File languagesDirectory, String defaultLanguage) {
		languageManager = new LanguageManager(languagesDirectory);
		language = languageManager.getLanguageByCode(defaultLanguage);
	}
	
	public static void init(File languagesDirectory, String defaultLanguage) {
		instance = new I18n(languagesDirectory, defaultLanguage);
	}
	
	public static String get(String key, Object... args) {
		String result = key;
		if(instance.language.getTranslatedStrings().containsKey(key)) {
			result = String.format(instance.language.getTranslatedStrings().get(key), args);
		}
		return result;
	}

}
