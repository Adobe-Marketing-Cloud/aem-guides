package com.adobe.aem.sample.simple.models;

import java.util.List;

public interface DisplayContent {
	/***
	 * Traverses the docs content and collects all 'text' properties to be outputted on the screen.
	 * @return
	 */
	List<String> getDocsContent();

}
