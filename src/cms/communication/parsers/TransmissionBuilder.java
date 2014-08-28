package cms.communication.parsers;

import java.math.BigInteger;
import java.util.ArrayList;

import cms.communication.structures.Atom;
import cms.communication.structures.Transmission;

public class TransmissionBuilder {
	private static Transmission transmission;
	private static int count;
	private static ArrayList<Atom> list;
	
	private TransmissionBuilder(){
		count = 0;
	}
	
	public static void addAtom(String atom_type, String atom_class, String content){
		Atom atom = new Atom(atom_type, atom_class);
		atom.addContent(content);
		list.add(atom);
	}
	
	public static void newTransmission(String from, String to){
		transmission = new Transmission(Integer.toString(count), from, to);
	}
	
	public static Transmission getTransmission(){
		Transmission ret = transmission;
		transmission = null;
		count++;
		return ret;
	}

}
