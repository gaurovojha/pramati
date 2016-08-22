package com.pramati.crawler.main;

import java.util.List;

import com.pramati.crawler.data.EmailData;
import com.pramati.crawler.service.CrawlerService;
import com.pramati.crawler.service.impl.CrawlerServiceImpl;

public class PramatiCrawler {
	
	private static final String URL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
	private static final String YEAR = "2015";

	public static void main(String[] args) {

		CrawlerService crawlerService = new CrawlerServiceImpl();
		
		//downloading email data
		List<EmailData> emailData = crawlerService.downloadEmails(URL, YEAR);

		//printing random email content.
		crawlerService.printDownloadedEmail(emailData.get(100));

	}

}
