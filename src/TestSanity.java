import static org.junit.Assert.*;

import org.openqa.*;
import org.openqa.selenium.By;
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

public class TestSanity {

	private static WebDriver driver;
	
	public static Process server;
	
	public static String logs;
	
	/* 
	 * Each test will hit if it can or stay if it can't until
	 * it is in the right state to run the test. At which point
	 * it runs the test and will continue to the next one
	 */

	public static void main(String[] args) throws Exception {                    
	       JUnitCore.main("TestSanity");            
	}
	
	public static void start_server(String port, String debug, String rigged, String test)throws IOException, InterruptedException {
		server = Runtime.getRuntime().exec("node ../Poker/app.js " + port + " " + debug + " " + rigged + " " + test);
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
		Thread.sleep(3000);
	}
	
	public ArrayList<String> getCards(){
		WebElement card0 = driver.findElement(By.id("card-0"));
		WebElement card1 = driver.findElement(By.id("card-1"));
		WebElement card2 = driver.findElement(By.id("card-2"));
		WebElement card3 = driver.findElement(By.id("card-3"));
		WebElement card4 = driver.findElement(By.id("card-4"));
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
	public void testClickCards() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		WebElement card0 = driver.findElement(By.id("card-0"));
		WebElement card1 = driver.findElement(By.id("card-1"));
		WebElement card2 = driver.findElement(By.id("card-2"));
		WebElement card3 = driver.findElement(By.id("card-3"));
		WebElement card4 = driver.findElement(By.id("card-4"));
		assertEquals(card0.getAttribute("style"), "text-align: center;");
		assertEquals(card1.getAttribute("style"), "text-align: center;");
		assertEquals(card2.getAttribute("style"), "text-align: center;");
		assertEquals(card3.getAttribute("style"), "text-align: center;");
		assertEquals(card4.getAttribute("style"), "text-align: center;");
		card0.click();
		assertEquals(card0.getAttribute("style"), "text-align: center; background-color: red;");
		card0.click();
		assertEquals(card0.getAttribute("style"), "text-align: center;");
		card1.click();
		assertEquals(card1.getAttribute("style"), "text-align: center; background-color: red;");
		card1.click();
		assertEquals(card1.getAttribute("style"), "text-align: center;");
		card2.click();
		assertEquals(card2.getAttribute("style"), "text-align: center; background-color: red;");
		card2.click();
		assertEquals(card2.getAttribute("style"), "text-align: center;");
		card3.click();
		assertEquals(card3.getAttribute("style"), "text-align: center; background-color: red;");
		card3.click();
		assertEquals(card3.getAttribute("style"), "text-align: center;");
		card4.click();
		assertEquals(card4.getAttribute("style"), "text-align: center; background-color: red;");
		card4.click();
		assertEquals(card4.getAttribute("style"), "text-align: center;");	
	}
	
	@Test
	public void testExchangeCard0() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		ArrayList<String> hand1 = getCards();
		WebElement card = driver.findElement(By.id("card-0"));
		WebElement exchange = driver.findElement(By.id("exchange"));
		card.click();
		exchange.click();
		ArrayList<String> hand2 = getCards();
		assertNotEquals(hand1,hand2);
	}
	
	@Test
	public void testExchangeCard1() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		ArrayList<String> hand1 = getCards();
		WebElement card = driver.findElement(By.id("card-1"));
		WebElement exchange = driver.findElement(By.id("exchange"));
		card.click();
		exchange.click();
		ArrayList<String> hand2 = getCards();
		assertNotEquals(hand1,hand2);
	}
	
	@Test
	public void testExchangeCard2() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		ArrayList<String> hand1 = getCards();
		WebElement card = driver.findElement(By.id("card-2"));
		WebElement exchange = driver.findElement(By.id("exchange"));
		card.click();
		exchange.click();
		ArrayList<String> hand2 = getCards();
		assertNotEquals(hand1,hand2);
	}
	
	@Test
	public void testExchangeCard3() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		ArrayList<String> hand1 = getCards();
		WebElement card = driver.findElement(By.id("card-3"));
		WebElement exchange = driver.findElement(By.id("exchange"));
		card.click();
		exchange.click();
		ArrayList<String> hand2 = getCards();
		assertNotEquals(hand1,hand2);
	}
	
	@Test
	public void testExchangeCard4() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		ArrayList<String> hand1 = getCards();
		WebElement card4 = driver.findElement(By.id("card-4"));
		WebElement exchange = driver.findElement(By.id("exchange"));
		card4.click();
		exchange.click();
		ArrayList<String> hand2 = getCards();
		assertNotEquals(hand1,hand2);
	}

	@Test
	public void testExchangeAllCards() throws InterruptedException, IOException {
		start_server("8888", "true", "false", "../Poker/test_starting_hands/player0_flush.txt");
		ArrayList<String> hand1 = getCards();
		WebElement card0 = driver.findElement(By.id("card-0"));
		WebElement card1 = driver.findElement(By.id("card-1"));
		WebElement card2 = driver.findElement(By.id("card-2"));
		WebElement card3 = driver.findElement(By.id("card-3"));
		WebElement card4 = driver.findElement(By.id("card-4"));
		WebElement exchange = driver.findElement(By.id("exchange"));
		card0.click();
		card1.click();
		card2.click();
		card3.click();
		card4.click();
		exchange.click();
		ArrayList<String> hand2 = getCards();
		assertNotEquals(hand1,hand2);
	}

	
	@After
	public void teardown() throws InterruptedException {
		server.destroy();
		driver.quit();
		Thread.sleep(1000); // Let the server reset a second between tests
	}
		
}
