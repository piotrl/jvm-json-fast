package net.piotrl.jvm.jsonassist.example;

import java.time.LocalDate;

public class Book {
	public int noPages;
	public String author;
	public LocalDate publishedOn;
	public String title;

	public int getNoPages() {
		return noPages;
	}

	public void setNoPages(int noPages) {
		this.noPages = noPages;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDate getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(LocalDate publishedOn) {
		this.publishedOn = publishedOn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
