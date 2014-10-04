package main;

import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Tweeter {

	private WebClient webClient;;

	private String username;
	private String password;

	private boolean loggedIn = false;

	public Tweeter(String username, String password) {
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setCssEnabled(true);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getCookieManager().setCookiesEnabled(true);

		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		new Shutit(webClient);

		this.username = username;
		this.password = password;
	}

	public boolean login() throws Exception {
		HtmlPage page = webClient.getPage("https://mobile.twitter.com/login");

		final HtmlForm form = page.getForms().get(0);
		final HtmlTextInput userField = form.getInputByName("username");
		final HtmlPasswordInput passField = form.getInputByName("password");

		userField.setValueAttribute(this.username);
		passField.setValueAttribute(this.password);

		final HtmlSubmitInput button = form.getInputByValue("Sign in");
		final HtmlPage resultPage = button.click();

		webClient.closeAllWindows();

		String title = resultPage.getUrl().toExternalForm();

		loggedIn = true;
		return title.equals("https://mobile.twitter.com/");
	}

	public boolean tweet(String text) throws Exception {
		if (!loggedIn || text.length() > 140 || text.length() < 1) {
			return false;
		}

		HtmlPage page = webClient.getPage("https://mobile.twitter.com/compose/tweet");

		final HtmlForm form = page.getForms().get(0);
		final HtmlTextArea userField = form.getTextAreaByName("tweet[text]");

		userField.setText(text);

		final HtmlSubmitInput button = form.getInputByValue("Tweet");
		final HtmlPage resultPage = button.click();

		String title = resultPage.getUrl().toExternalForm();
		return title.equals("https://mobile.twitter.com/");
	}
}
