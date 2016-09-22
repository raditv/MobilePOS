package com.odoo.pos.controllers;

import java.util.List;

import android.content.ContentValues;

import com.odoo.pos.helpers.DatabaseOperation;
import com.odoo.pos.helpers.DatabaseContents;

/**
 * Saves and loads language preference from databaseOperation.
 * 
 * @author odoo Team
 *
 */
public class LanguageController {
	
	private static final String DEFAULT_LANGUAGE = "id";
	private static DatabaseOperation databaseOperation;
	private static LanguageController instance;
	
	private LanguageController() {
		
	}
	
	public static LanguageController getInstance() {
		if (instance == null)
			instance = new LanguageController();
		return instance;
	}
	
	/**
	 * Sets databaseOperation for use in this class.
	 * @param db databaseOperation.
	 */
	public static void setDatabaseOperation(DatabaseOperation db) {
		databaseOperation = db;
	}
	
	/**
	 * Sets language for use in application.
	 * @param localeString local string of country.
	 */
	public void setLanguage(String localeString) {
		databaseOperation.execute("UPDATE " + DatabaseContents.LANGUAGE + " SET language = '" + localeString + "'");
	}
	
	/**
	 * Returns current language. 
	 * @return current language.
	 */
	public String getLanguage() {
		List<Object> contents = databaseOperation.select("SELECT * FROM " + DatabaseContents.LANGUAGE);

		if (contents.isEmpty()) {
			ContentValues defualtLang = new ContentValues();
			defualtLang.put("language", DEFAULT_LANGUAGE);
			databaseOperation.insert( DatabaseContents.LANGUAGE.toString(), defualtLang);
	
			return DEFAULT_LANGUAGE;
		}
		ContentValues content = (ContentValues) contents.get(0);
		return content.getAsString("language");	
	}

}
