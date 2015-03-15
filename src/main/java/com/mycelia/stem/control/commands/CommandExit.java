package com.mycelia.stem.control.commands;

import cms.control.methods.CMSCommand;

public class CommandExit extends AbstractCommand {
	private final static String command_signature = "exit";

	public void action(String arg) {
		String[] parameters = super.commandParam(arg);
		if (parameters.length > 1) {
			if(parameters[1].equals("def")){
				define();
			} else if(parameters[1].equals("now")){
				System.exit(0);
			} else if(parameters[1].equals("clean")){
				CMSCommand.cleanExit();
			} else {
				System.out.println("wrong parameters");
			}
		} else {
			System.out.println("command incomplete");
		}

	}
	
	@Override
	public void define() {
		System.out.println("kills the application");
		System.out.println("now : exits without warning or secondary check");
		System.out.println("clean : tries to exit cleanly (announces it to grid)");
	}
	
	@Override
	public String getCommandSignature(){
		return CommandExit.command_signature;
	}

}