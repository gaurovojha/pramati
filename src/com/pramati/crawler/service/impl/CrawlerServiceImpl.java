package com.pramati.crawler.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.crawler.data.EmailData;
import com.pramati.crawler.service.CrawlerService;

public class CrawlerServiceImpl implements CrawlerService {

	@Override
	public List<EmailData> downloadEmails(String url, String year) {
		// TODO Auto-generated method stub
		
		List<String> yearList = processPage(url,year);
		List<String> emailList = null;
		List<EmailData> emailData = null;
		
		if(yearList!=null && !yearList.isEmpty()){
			emailList = emailUrlExtract(yearList,url);
		}
		
		if(emailList!=null && !emailList.isEmpty()){
			//emailData = extractEmailData(emailList);
			emailData = extractEmailDataWithThreads(emailList);
		}
		
		return emailData;
	}

	
	//Process given URL and finds years list
	public List<String> processPage(String url, String year) {

		// Document doc =
		// Jsoup.connect("http://mail-archives.apache.org/mod_mbox/maven-users/").get();
		List<String> yearList = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				if (link.attr("href").startsWith(year) && link.attr("href").endsWith("date")) {

					String text = link.toString().replace("<a href=", "").replace("/date\">Date</a>", "/ajax/thread?0")
							.replace("\"", "/");

					System.out.println("Link " + text);
					yearList.add(text);
					// System.out.println("the list "+l);

					// processPage(link.attr("abs:href"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get all links and recursively call the processPage method
		
		return yearList;
	}

	//Process given year and calculate email list urls
	public  List<String> emailUrlExtract(List<String> yearList, String url){
		List<String> mailList = new ArrayList<String>();
		//String url = "http://mail-archives.apache.org/mod_mbox/camel-users";
		// Document doc=null;

		for (int i = 0; i < yearList.size(); i++) {
			String a = (String) yearList.get(i);
			String mailExtract = url + a;
			System.out.println("the mail browse url" + mailExtract);

			Document doc;
			try {
				doc = Jsoup.connect(mailExtract).get();
				Elements questions = doc.select("message");
				for (Element link : questions) {
					if (!link.attr("id").isEmpty()) {

						String id = link.attr("id");

						String contentUrl = mailExtract.replace("thread?0", id);

						mailList.add(contentUrl);

						// processPage(link.attr("abs:href"));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mailList;
	}

	
	/*This method extracts data from the emails
	 * EmailList has been divided into 4 parts and each part is given to an individual therad
	 * To make the execution fast 
	 */
	
	public List<EmailData> extractEmailDataWithThreads(List<String> emailList){
		List<EmailData> emails = new ArrayList<EmailData>();
		
		final Thread thread1 = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	for(int i =0 ;i<emailList.size()/4 ; i++){
	        		System.out.println("Thread1 executing");
	        		Document doc;
	    			try {
	    				doc = Jsoup.connect(emailList.get(i)).get();
	    				EmailData email = new EmailData();
	    				emails.add(populateData(email,doc));
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	        	}
	        }
	    });
		
		final Thread thread2 = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	for(int i =emailList.size()/4 ;i<emailList.size()/2 ; i++){
	        		System.out.println("Thread2 executing");
	        		Document doc;
	    			try {
	    				doc = Jsoup.connect(emailList.get(i)).get();
	    				EmailData email = new EmailData();
	    				emails.add(populateData(email,doc));
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	        	}
	        }
	    });
		
		final Thread thread3 = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	for(int i =emailList.size()/2 ;i<emailList.size()*3/4 ; i++){
	        		System.out.println("Thread3 executing");
	        		Document doc;
	    			try {
	    				doc = Jsoup.connect(emailList.get(i)).get();
	    				EmailData email = new EmailData();
	    				emails.add(populateData(email,doc));
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	        	}
	        }
	    });
		
		final Thread thread4 = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	for(int i =emailList.size()*3/4 ;i<emailList.size() ; i++){
	        		System.out.println("Thread4 executing");
	        		Document doc;
	    			try {
	    				doc = Jsoup.connect(emailList.get(i)).get();
	    				EmailData email = new EmailData();
	    				emails.add(populateData(email,doc));
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	        	}
	        }
	    });
		
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		return emails;
	}
	
	//Populating data into email object
	public EmailData populateData(EmailData email, Document doc){
		email.setFrom(Jsoup.parse(doc.getElementsByTag("from").text()).text());
		email.setSubject(Jsoup.parse(doc.getElementsByTag("subject").text()).text());
		email.setDate(Jsoup.parse(doc.getElementsByTag("date").text()).text());
		email.setContent(Jsoup.parse(doc.getElementsByTag("contents").text()).text());
		return email;
	}
	
	//print / perform action on downloaded data
	public void printDownloadedEmail(EmailData email){
		System.out.println(email.getFrom());
		System.out.println(email.getSubject());
		System.out.println(email.getDate());
		System.out.println(email.getContent());
	}
	
	//This method extracts data from the emails
		private List<EmailData> extractEmailData(List<String> emailList) {
			
			List<EmailData> emails = new ArrayList<EmailData>();
			for(String emailLink : emailList){
				Document doc;
				try {
					doc = Jsoup.connect(emailLink).get();
					EmailData email = new EmailData();
					email.setFrom(Jsoup.parse(doc.getElementsByTag("from").text()).text());
					email.setSubject(Jsoup.parse(doc.getElementsByTag("subject").text()).text());
					email.setDate(Jsoup.parse(doc.getElementsByTag("date").text()).text());
					email.setContent(Jsoup.parse(doc.getElementsByTag("contents").text()).text());
					emails.add(email);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return emails;
		}

}
