package com.elpais.elem;

import org.openqa.selenium.By;

public class ElementLocator {
	private final String name;
	private final String type;
	private final String value;

	public ElementLocator(String name, String type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public By toBy() {
		switch (type.toLowerCase()) {
			case "id":
				return By.id(value);
			case "name":
				return By.name(value);
			case "css":
			case "cssselector":
				return By.cssSelector(value);
			case "xpath":
				return By.xpath(value);
			case "classname":
				return By.className(value);
			case "tag":
			case "tagname":
				return By.tagName(value);
			case "linktext":
				return By.linkText(value);
			case "partiallinktext":
				return By.partialLinkText(value);
			default:
				throw new IllegalArgumentException("Unsupported locator type: " + type + " for element: " + name);
		}
	}
}


