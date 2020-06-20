package de.frooastside.engine.language;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class LanguageManager {
	
	private List<Language> languages = new ArrayList<Language>();
	
	public LanguageManager(File languagesDirectory) {
		File[] files = languagesDirectory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File directory, String name) {
				if(name.endsWith(".lang")) {
					return true;
				}else {
					return false;
				}
			}
		});
		for (int i = 0; i < files.length; i++) {
			languages.add(new Language(files[i]));
			System.out.println(languages.get(i).getLanguageName());
		}
	}
	
	public Language getLanguageByCode(String languageCode) {
		Language targetLanguage = null;
		for(Language language : languages) {
			if(language.getLanguageCode().equalsIgnoreCase(languageCode)) {
				targetLanguage = language;
			}
		}
		return targetLanguage;
	}
	
	public Language getLanguageByName(String languageName) {
		Language targetLanguage = null;
		for(Language language : languages) {
			if(language.getLanguageName().equalsIgnoreCase(languageName)) {
				targetLanguage = language;
			}
		}
		return targetLanguage;
	}

	public List<Language> getLanguages() {
		return languages;
	}

}
