package com.pramati.crawler.service;

import java.util.List;

import com.pramati.crawler.data.EmailData;

public interface CrawlerService {
	
	List<EmailData> downloadEmails(String url , String year);
	
	void printDownloadedEmail(EmailData email);

}
