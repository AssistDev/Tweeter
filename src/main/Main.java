package main;

public class Main {

	public static void main(String[] args) {
		Tweeter tweeter = new Tweeter("username", "password");
		
		boolean successfulLogin = false;
		
		try {
			successfulLogin = tweeter.login();
		} catch (Exception e) { }
		
		if(successfulLogin) {
			System.out.println("Logged in.");
			
			boolean successfulTweet = false;
			
			try {
				successfulTweet = tweeter.tweet("Test tweet.");
			} catch (Exception e) { }
			
			if(successfulTweet) {
				System.out.println("Tweeted");
			}
		}
	}
}
