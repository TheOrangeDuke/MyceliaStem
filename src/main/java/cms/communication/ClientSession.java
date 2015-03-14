package cms.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.mycelia.common.communication.structures.Transmission;
import com.mycelia.common.communication.structures.TransmissionBuilder;

import cms.databank.OverLord;
import cms.databank.structures.Node;
import cms.databank.structures.NodeState;
import cms.display.graphing.GraphingHistogram;
import cms.display.info.AddressPanel;
import cms.helpers.ThreadHelper;

public class ClientSession extends ThreadHelper {

	//Socket object to handle client transmission on transport layer
	protected Socket clientConnectionSocket = null;
	//Socket object to handle server transmission on transport layer
	protected String serverTransmission = null;
	//The client's IP address
	private String ipAddress;
	//This flag is true once the setup phase of the session is complete 
	private boolean SETUP = false;
	//This flag is true if the node is already known to the CMS by IP
	private boolean HAS_CONNECTED = false;
	//This is the id of the session which is the same as the node id in datastore
	private int sessionID = -1;
	//Input stream buffer
	private BufferedReader input;
	//Output stream buffer
	private PrintWriter output;
	
	//The string currently being processed by the server (\r\n terminated from client)
	protected Gson gson = new Gson();
	protected String inputS;
	
	public ClientSession(Socket clientConnectionSocket) {
		this.clientConnectionSocket = clientConnectionSocket;
		this.ipAddress = clientConnectionSocket.getInetAddress().getHostAddress();
	}

	public void run() {
		try {
			TransmissionBuilder tb = new TransmissionBuilder();

			input =  new BufferedReader(new InputStreamReader(
					clientConnectionSocket.getInputStream()));
			output =  new PrintWriter(
					clientConnectionSocket.getOutputStream(), true);
			
			if (!SETUP) {
				//Once connected perform initial setup (assign node ID, etc)
				String infoParams;
				
				if (OverLord.getIPInClusterPosition(ipAddress) != -1) {
					//This node has already connected before, give it its old position
					sessionID = OverLord.getIPInClusterPosition(ipAddress);
					HAS_CONNECTED = true;
				} else {
					sessionID = OverLord.nextNodeID();
				}
				
				tb.newTransmission(100, "cms:0", "node:" + Integer.toString(sessionID));
				tb.newAtom("id", "int", Integer.toString(sessionID));
				Transmission initTrans = tb.getTransmission();
				String initParams = gson.toJson(initTrans);
				/*
				infoParams = Integer.toString(sessionID);
				*/
				output.println(initParams);
				SETUP = true;
			}
			
			Node curNode;
			if (!HAS_CONNECTED) {
				OverLord.nodeCore.add(new Node());
				curNode = OverLord.nodeCore.get(OverLord.nodeCore.size() - 1);
			} else {
				curNode = OverLord.nodeCore.get(sessionID);
			}

			curNode.setType(0);
			curNode.setId(sessionID);
			curNode.setIp(ipAddress);
			curNode.setState(NodeState.AVAILABLE);
			
			//UPDATE THE GRAPHICS TO MATCH THE ADDITION OF A NEW NODE
			AddressPanel.updateButtonList();
			GraphingHistogram.updateBarCount();
			//
			
			Transmission trans = new Transmission();
			while (SETUP && ((inputS = input.readLine() ) != null)) {
				AddressPanel.nodeButton(sessionID).setBlink(true);
				trans = gson.fromJson(inputS, Transmission.class);
				if (trans.get_header().get_to().equals("cms:server")) {
					OverLord.insertData(trans);
				}

			}

			// If the program gets here the connection has been lost, do some clean up stuff
			curNode.setState(NodeState.ABSENT);
			//cleanUp();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public synchronized void cleanUp() {
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OverLord.removeNode(sessionID);
	}
}