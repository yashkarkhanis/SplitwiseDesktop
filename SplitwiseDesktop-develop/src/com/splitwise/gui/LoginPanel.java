package com.splitwise.gui;



import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.splitwise.gui.MainFrame;
import com.splitwise.splitwisesdk.SplitwiseSDK;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import com.splitwise.SplitwiseGUI;

public class LoginPanel extends JFXPanel implements ChangeListener{
	private static LoginPanel instance;
	private String url;
	private WebView webView;
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private LoginPanel() {
		Platform.runLater(() -> {
		webView = new WebView();
		webView.getEngine().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
		webView.getEngine().getLoadWorker().stateProperty().addListener(this);
		setScene(new Scene(webView));
		});
	}
	
	public static LoginPanel getInstance() {
		if(instance == null) {
			instance = new LoginPanel();
		}
		return instance;
	}
	
	public void load(String url) {
		Platform.runLater(() -> {
			this.url = url;
			webView.getEngine().load(url);
		});
	}

	@Override
	public void changed(ObservableValue observable, Object oldValue, Object newValue) {
		//System.out.println(oldValue + " " + newValue + " " + location);
		String location = webView.getEngine().getLocation();
        if(newValue == Worker.State.FAILED && location.startsWith("http://localhost")) {
        	// Get Oauth Verifier
        	String oauth_verifier = location.split("oauth_verifier=")[1];
        	SplitwiseSDK.getInstance().setOauthVerifier(oauth_verifier);
        	
        	// Get Access Token
        	System.out.println("Following are your access Token. You don't need to login again. Just use following tokens.\n"
        			+ "Copy oauth token and oauth_token_secret and Paste it at \n"
        			+ "splitwisesdk.SplitwiseSDK.java in main method, in place of oauth_access_token and oauth_access_token_secret.");
        	SplitwiseSDK.getInstance().getAccessToken();
        	
        	SplitwiseGUI.getInstance().grantLogin();
        }
        if (newValue == Worker.State.SUCCEEDED && location.equals("https://secure.splitwise.com/login")) {
        	setSize(this.getParent().getSize());
        	this.setVisible(true);
            LOGGER.info("Showing LoginPanel" + this.getLocation() + " Size: " + this.getSize() +  "Parent Size" + this.getParent().getSize());
            this.getParent().repaint();
        }
	
	}

	
}
