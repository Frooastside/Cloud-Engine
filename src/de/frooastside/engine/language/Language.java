package de.frooastside.engine.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Language {
	
	private Map<String, String> translatedStrings;
	private String languageCode;
	private String languageName;
	
	public Language(File file) {
		translatedStrings = new HashMap<String, String>();
		loadFromFile(file);
	}
	
	private void loadFromFile(File file) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				String[] args = line.split("=");
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("language.code")) {
						languageCode = args[1];
					}else if(args[0].equalsIgnoreCase("language.name")) {
						languageName = args[1];
					}else {
						translatedStrings.put(args[0], args[1]);
					}
				}
			}
			bufferedReader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public String getLanguageName() {
		return languageName;
	}

	public Map<String, String> getTranslatedStrings() {
		return translatedStrings;
	}

}
