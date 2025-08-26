package com.santos.ravenapi.search.enums;

public enum PublisherEndpointEnum {

	DC("https://dc.fandom.com/api.php"), MARVEL("https://marvel.fandom.com/api.php");

	private String url;

	PublisherEndpointEnum(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
