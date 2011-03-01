package net.exiva.threegeetest;

import danger.app.Application;
import danger.app.Event;

import danger.ui.AlertWindow;
import danger.ui.Menu;
import danger.ui.MenuItem;
import danger.ui.ScreenWindow;
import danger.ui.StaticTextBox;
import danger.ui.ListView;

import danger.util.DEBUG;

public class threegeetestView extends ScreenWindow implements Resources, Commands {
	public static ListView lvList;

	public threegeetestView() {}

	public static threegeetestView create() {
		threegeetestView me = (threegeetestView) Application.getCurrentApp().getResources().getScreen(ID_MAIN_SCREEN, null);
		return me;
	}

	public void onDecoded() {
		lvList = (ListView)this.getDescendantWithID(ID_LIST);
	}

	public final void adjustActionMenuState(Menu menu) {
		menu.removeAllItems();
		menu.addFromResource(Application.getCurrentApp().getResources(), ID_MAIN_MENU, this);
	}

	public void addNew(String in){
		lvList.addItem(in);
	}

	public boolean receiveEvent(Event e) {
		switch (e.type) {
			case EVENT_MAIL: {
				threegeetest.sendMail();
			return true;
			}
			case EVENT_CLEAR: {
				lvList.removeAllItems();
				invalidate();
				threegeetest.clear();
			}
			default:
			break;
		}
		return super.receiveEvent(e);
	}

	public void refresh() {
		invalidate();
	}
	
    public boolean eventWidgetUp(int inWidget, Event e) {
		switch (inWidget) {
			case Event.DEVICE_BUTTON_CANCEL:
			Application.getCurrentApp().returnToLauncher();
			return true;

			case Event.DEVICE_BUTTON_BACK:
			Application.getCurrentApp().returnToLauncher();
			return true;
		}
		return super.eventWidgetUp(inWidget, e);
	}
}