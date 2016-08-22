package com.pramati.crawler.test;

import org.junit.Assert;
import org.junit.Test;

import com.pramati.crawler.service.CrawlerService;
import com.pramati.crawler.service.impl.CrawlerServiceImpl;

public class PramatiCrawlerTest {
	
	private static final String URL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
	private static final String YEAR = "2015";

	@Test
	public void checkDownloadedEmailCount() {

		CrawlerService crawlerService = new CrawlerServiceImpl();
		crawlerService.downloadEmails(URL, YEAR);
		// assert statements
		Assert.assertTrue("Downloaded emails must be greater than zero", crawlerService.downloadEmails(URL, YEAR).size()>0);
	}
	
	@Test
	public void checkDownloadedEmailData() {

		CrawlerService crawlerService = new CrawlerServiceImpl();
		crawlerService.downloadEmails(URL, YEAR);
		// assert statements
		Assert.assertTrue("Downloaded Email Must have from field", !crawlerService.downloadEmails(URL, YEAR).get(100).getFrom().isEmpty());
	}
	
	@Test
	public void checkDownloadedEmailDate() {

		CrawlerService crawlerService = new CrawlerServiceImpl();
		crawlerService.downloadEmails(URL, YEAR);
		// assert statements
		Assert.assertTrue("Downloaded Email Must have from field", crawlerService.downloadEmails(URL, YEAR).get(100).getDate().contains(YEAR));
	}

}
