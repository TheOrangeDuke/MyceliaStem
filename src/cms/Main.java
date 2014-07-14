package cms;

import cms.controller.LogSystem;
import cms.model.DataFactory;
import cms.view.ProgramWindow;

public class Main {
	public static void main(String[] args) {
		
		
		Thread data = new Thread(new Runnable(){
			public void run() {
				DataFactory.build();	
			}
		});
		
		
		Thread display = new Thread(new Runnable(){
			public void run() {
				ProgramWindow.init();
				LogSystem.log(true, false, "Log System Started");
				System.out.println("Welcome to the CMS v0.5 alpha");	
			}
		});
		
		Thread communicator = new Thread(new Runnable(){
			public void run() {
				
				
			}
		});
		
		data.start();
		display.start();
		communicator.start();
		
	}
}
