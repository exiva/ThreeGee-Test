package net.exiva.threegeetest;

import danger.contacts.*;
import danger.app.Alarm;
import danger.app.Application;
import danger.app.AppResources;
import danger.app.IPCMessage;
import danger.app.Registrar;
import danger.app.Event;
import danger.app.EventType;

import danger.net.HTTPConnection;
import danger.net.HTTPTransaction;

import danger.util.format.DateFormat;
import danger.util.DEBUG;
import java.util.List;

import java.util.Date;

import java.util.Vector;

public class threegeetest extends Application implements Resources, Commands {
	static public threegeetestView mWindow;
	static public Alarm mAlarm;
	public static Date date;
	public static String sDate;
	public static byte[] photoData;
	public static Vector vector;
	
	public threegeetest() {
		mWindow = threegeetestView.create();
		mWindow.show();
		//setup timer
		mAlarm = new Alarm(1200, this, 1, null);
		//init vector
		vector = new Vector();
	}

	public void launch() {
		//start timer
		mAlarm.activate();
		//do an initial test.
		update();
	}

	public void resume() {
		// List contacts = ContactsManager.getInstance().getContacts();
		// DEBUG.p("List: "+contacts.toString());
		//     	Object[] ol = contacts.toArray();
		// 
		//     	System.out.println("Array of Object has length " + ol.length);
		// // DEBUG.p("Object: "+ Object.toString());.
		// DEBUG.p(java.util.Arrays.asList(ol).toString());
		// String[] sl = (String[]) contacts.toArray(new String[ol.length]);
		//     	System.out.println("Array of String has length " + sl.length);
// ?		String[] array3 = new String[1];
		// contacts.toArray(array3);
		// DEBUG.p("Array: "+array3);
	}

	public void suspend() {
	}
	
	public void update() {
		date = new Date();
		sDate = DateFormat.withFormat("MM.dd%20hh:mm:ss", date);
		HTTPConnection.get("http://static.tmblr.us/hiptop/time.php?d="+sDate, null, (short) 0, 99);
	}

	public static void sendMail() {
		String result = "";
		for (int i=0; i<vector.size(); i++) {
			result = result + "\n" + vector.get(i);
		}
		IPCMessage ipc = new IPCMessage();
		ipc.addItem("action" , "send");
		ipc.addItem("to", "kevinar@microsoft.com");
		// ipc.addItem("to", "exiva@exiva.net");
		ipc.addItem("subject" , "3G Data Stall");
		ipc.addItem("body", result+"\n\nLegend: [1]: Left Device [2]: Arrived to Server [3]: Arrived to Device\nSent from ThreeGeeTest Application.");
		Registrar.sendMessage("Email", ipc, null);
	}

	public static void clear() {
		vector = new Vector();
	}
	
	public boolean receiveEvent(Event e) {
			switch (e.type) {
				//event for timer
				case Event.EVENT_ALARM :{
					if (e.data==1) {
						update();
						mAlarm.resetWake(1200);
					}
					return true;
				}
			}
		return (super.receiveEvent(e));
	}

	public void networkEvent(Object object) {
		if (object instanceof HTTPTransaction) {
		HTTPTransaction qt = (HTTPTransaction) object;
			if (qt.getSequenceID() == 99) {
				Date aDate = new Date();
				mWindow.addNew(qt.getString()+" [3]: "+DateFormat.withFormat("MM.dd hh:mm:ss", aDate));
				mWindow.refresh();
				vector.add(qt.getString()+" [3]: "+DateFormat.withFormat("MM.dd hh:mm:ss", aDate));
			}
		}
	}
}