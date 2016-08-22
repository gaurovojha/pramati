package com.pramati.crawler.service.impl;

public class testwiththread {
	
	final Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            result2 = expensiveMethod("param2");
        }
    });

}
