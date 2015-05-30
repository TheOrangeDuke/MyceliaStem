package com.myselia.stem.communication;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.myselia.javacommon.communication.MailService;
import com.myselia.javacommon.communication.distributors.DistributorType;
import com.myselia.javacommon.constants.opcode.ComponentType;

public class StemServer implements Runnable {

	public boolean SERVER_RUNNING = false;
	public int MAX_SERVER_THREAD_POOLS = 10;
	protected int port, backlog;

	protected PrintWriter output;
	protected BufferedReader input;

	protected ServerSocket serverSocket;
	protected Socket clientConnectionSocket;
	protected ExecutorService threadPool;
	
	private boolean isHTTP;

	public StemServer(int port, int backlog, boolean isHTTP) {
		System.out.println("Starting Stem Server with Port: " + port);
		this.isHTTP = isHTTP;
		this.serverSocket = null;
		this.clientConnectionSocket = null;
		this.port = port;
		this.backlog = backlog;
		this.SERVER_RUNNING = true;
		Thread data = new Thread(new MailService(DistributorType.FORWARDER, ComponentType.STEM));
    	data.start();
		threadPool = Executors.newFixedThreadPool(MAX_SERVER_THREAD_POOLS);
	}

	//Ticks as long as the server is running. Used to accept connections. Blocks until one is received.
	public void serverTick() throws ClassNotFoundException {
		if (serverSocket == null){
			openServerSocket(port);
		}

		while (SERVER_RUNNING) {
			clientConnectionSocket = null;
			try {
				clientConnectionSocket = this.serverSocket.accept();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Create a thread for a client if accepted containing an StemClientSession
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ACCEPTING CONNECTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			if (isHTTP)
				this.threadPool.execute(new Thread(new StemClientSession(clientConnectionSocket, assignInternalID(clientConnectionSocket), true)));
			else
				this.threadPool.execute(new Thread(new StemClientSession(clientConnectionSocket, assignInternalID(clientConnectionSocket), false)));

		}
		System.out.println("server no longer listening on port: " + port);
	}

	private int assignInternalID(Socket clientConnectionSocket) {
		return 0;
	}

	private void openServerSocket(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			System.err.println("FATAL: Cannot open Stem Server port on " + port);
		}
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				serverTick();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}