package org.telegram.telegrambots.googleSearch;

import lombok.Data;

@Data
public class GResult {
	String title;
	String link;
	Image image;
}