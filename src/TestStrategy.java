import static org.junit.Assert.*;

import org.openqa.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.*;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.JUnitCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.Collections;

public class TestStrategy {

	private static WebDriver driver;
	
	public static Process server;
	
	public static String logs;
	
	/* 
	 * Each test will hit if it can or stay if it can't until
	 * it is in the right state to run the test. At which point
	 * it runs the test and will continue to the next one
	 */

	public static void main(String[] args) throws Exception {                    
	       JUnitCore.main("TestStrategy");            
	}
	
	public static void start_server(String port, String debug, String rigged, String test, String strat)throws IOException, InterruptedException {
		server = Runtime.getRuntime().exec("node ../Poker/app.js " + port + " " + debug + " " + rigged + " " + test + " false true");
		Thread.sleep(500);
		boolean hasCrashed = true;
		try{
			server.exitValue();
		}
		catch(IllegalThreadStateException e){
			hasCrashed = false;
		}
		if(hasCrashed){
			fail("Port: " + port + " in use. Change port or close the process using it.");
		}
		driver.get("http://localhost:" + port + "/");
		Thread.sleep(1000);
		Alert enterStrat = driver.switchTo().alert();
		enterStrat.sendKeys(strat);
		enterStrat.accept();
		Thread.sleep(2000);
	}
	
	public static void enterCards(ArrayList<String> player0, ArrayList<String> player1, ArrayList<String> player2, ArrayList<String> player3) throws InterruptedException{
		Alert enterCards;
		while(true){
			Thread.sleep(200);
			try{
				enterCards = driver.switchTo().alert();
			}
			catch(NoAlertPresentException e){
				break;
			}
			String text = enterCards.getText();
			if(text.contains("0")){
				if(player0.size()==0){
					enterCards.sendKeys("A,H");
					enterCards.accept();
				}
				else{
					enterCards.sendKeys(player0.remove(0));
					enterCards.accept();
				}
			}
			else if(text.contains("1")){
				if(player1.size()==0){
					enterCards.sendKeys("A,H");
					enterCards.accept();
				}
				else{
					enterCards.sendKeys(player1.remove(0));
					enterCards.accept();
				}
			}
			else if(text.contains("2")){
				if(player2.size()==0){
					enterCards.sendKeys("A,H");
					enterCards.accept();
				}
				else{
					enterCards.sendKeys(player2.remove(0));
					enterCards.accept();
				}
			}
			else if(text.contains("3")){
				if(player3.size()==0){
					enterCards.sendKeys("A,H");
					enterCards.accept();
				}
				else{
					enterCards.sendKeys(player3.remove(0));
					enterCards.accept();
				}
			}
		}
	}
	
	public ArrayList<String> getCards(String player){
		WebElement card0 = driver.findElement(By.id(player+"-card-0"));
		WebElement card1 = driver.findElement(By.id(player+"-card-1"));
		WebElement card2 = driver.findElement(By.id(player+"-card-2"));
		WebElement card3 = driver.findElement(By.id(player+"-card-3"));
		WebElement card4 = driver.findElement(By.id(player+"-card-4"));
		ArrayList<String> hand = new ArrayList<String>();
		hand.add(card0.getText());
		hand.add(card1.getText());
		hand.add(card2.getText());
		hand.add(card3.getText());
		hand.add(card4.getText());
		Collections.sort(hand, String.CASE_INSENSITIVE_ORDER);
		return hand;
	}
	
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Test
	public void testPlayer1Strategy1Case1() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player1_strategy1case1.txt", "1,1");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("3H");
		expectedHand.add("4D");
		expectedHand.add("5S");
		expectedHand.add("6S");
		enterCards(new ArrayList<String>(),  new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		ArrayList<String> endHand = getCards("player0");
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);
	}
	
	@Test
	public void testPlayer1Strategy1Case2() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player1_strategy1case2.txt", "1,1");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("2D");
		expectedHand.add("AH");
		expectedHand.add("AS");
		expectedHand.add("AD");
		ArrayList<String> toAdd = new ArrayList<String>();
		toAdd.add("AH");
		toAdd.add("AS");
		toAdd.add("AD");
		enterCards(new ArrayList<String>(), toAdd, new ArrayList<String>(), new ArrayList<String>());
		ArrayList<String> endHand = getCards("player0");
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);	
	}
	
	@Test
	public void testPlayer2Strategy1Case1() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player2_strategy1case1.txt", "1,1");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("3H");
		expectedHand.add("4D");
		expectedHand.add("5S");
		expectedHand.add("6S");
		enterCards(new ArrayList<String>(),  new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		ArrayList<String> endHand = getCards("player1");
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);
	}
	
	@Test
	public void testPlayer2Strategy1Case2() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player2_strategy1case2.txt", "1,1");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("2D");
		expectedHand.add("AH");
		expectedHand.add("AS");
		expectedHand.add("AD");
		ArrayList<String> toAdd = new ArrayList<String>();
		toAdd.add("AH");
		toAdd.add("AS");
		toAdd.add("AD");
		enterCards(new ArrayList<String>(), new ArrayList<String>(), toAdd, new ArrayList<String>());
		ArrayList<String> endHand = getCards("player1");
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);
	}
	
	@Test
	public void testPlayer1Strategy2() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player1_strategy2.txt", "2,1");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("3H");
		expectedHand.add("4D");
		expectedHand.add("5S");
		expectedHand.add("6S");
		enterCards(new ArrayList<String>(),  new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		ArrayList<String> endHand = getCards("player0");
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);
	}
	
	@Test
	public void testPlayer2Strategy2Case2() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player2_strategy2case2.txt", "1,2");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("3H");
		expectedHand.add("6D");
		expectedHand.add("5S");
		expectedHand.add("6S");
		enterCards(new ArrayList<String>(),  new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		ArrayList<String> endHand = getCards("player1");
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertNotEquals(expectedHand, endHand);
	}
	
	@Test
	public void testPlayer2Strategy2Case3() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player2_strategy2case3.txt", "1,2");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("3H");
		expectedHand.add("4D");
		expectedHand.add("5S");
		expectedHand.add("6S");
		enterCards(new ArrayList<String>(),  new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		ArrayList<String> endHand = getCards("player1");
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);
	}

	@Test
	public void testDealerStrategy() throws InterruptedException, IOException {
		start_server("8888", "true", "true", "../Poker/test_starting_hands/player3_strategy.txt", "1,1");
		ArrayList<String> expectedHand = new ArrayList<String>();
		expectedHand.add("2S");
		expectedHand.add("3H");
		expectedHand.add("4D");
		expectedHand.add("5S");
		expectedHand.add("6S");
		enterCards(new ArrayList<String>(),  new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
		WebElement stay = driver.findElement(By.id("stay"));
		stay.click();
		ArrayList<String> endHand = getCards("dealer");
		Collections.sort(expectedHand, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(endHand, String.CASE_INSENSITIVE_ORDER);
		assertEquals(expectedHand, endHand);
	}
	
	
	
	@After
	public void teardown() throws InterruptedException {
		server.destroy();
		driver.quit();
		Thread.sleep(1000); // Let the server reset a second between tests
	}
		
}
