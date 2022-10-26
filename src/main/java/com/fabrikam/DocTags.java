package com.fabrikam;

public enum DocTags {
	ID(".I", "id"),
	TITLE(".T", "title"),
	AUTHOR(".A", "author"),
	BIBLIOGRAPHY(".B", "bibliography"),
	TEXT(".W", "text");

	final String tag;
	final String name;

	DocTags (String tag, String name) {
		this.tag = tag;
		this.name = name;
	}
}
