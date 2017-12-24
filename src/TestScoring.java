import static org.junit.Assert.*;

import org.openqa.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.JUnitCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Runtime;

public class TestScoring {

	private static WebDriver driver;
	
	public static Process server;
	
	public static String logs;
	
	/* 
	 * Each test will hit if it can or stay if it can't until
	 * it is in the right state to run the test. At which point
	 * it runs the test and will continue to the next one
	 */

	public static void main(String[] args) throws Exception {                    
	       JUnitCore.main("TestScoring");            
	}
	
	public static void start_server(String test) throws IOException, InterruptedException {
		String port = "8888";
		server = Runtime.getRuntime().exec("node ../Poker/app.js " + port + " true false " + test + " true");
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
	
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Test
	public void testPlayer0Flush() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player0_flush.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 0 with a score of 5"),true);				
	}
	
	@Test
	public void testPlayer0StraightFlushTieBreak1() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player0_straightflushtiebreak1.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 0 with a score of 8"),true);				
	}
	
	@Test
	public void testPlayer0StraightFlushTieBreak2() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player0_straightflushtiebreak2.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 0 with a score of 8"),true);				
	}
	
	@Test
	public void testPlayer1FourOfAKind() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player1_fourofakind.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 1(AI) with a score of 7"),true);				
	}
	
	
	@Test
	public void testPlayer1RoyalFlush() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player1_royalflush.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 1(AI) with a score of 9"),true);				
	}
	
	@Test
	public void testPlayer1StraightFlushTieBreak3() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player1_straightflushtiebreak3.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 1(AI) with a score of 8"),true);				
	}
	
	@Test
	public void testPlayer2FullHouse() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player2_fullhouse.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 2(AI) with a score of 6"),true);				
	}
	
	@Test
	public void testPlayer2Straight() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player2_straight.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 2(AI) with a score of 4"),true);				
	}
	
	@Test
	public void testPlayer2ThreeOfAKind() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player2_threeofakind.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Player 2(AI) with a score of 3"),true);				
	}
	
	@Test
	public void testPlayer3Pair() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player3_pair.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Dealer with a score of 1"),true);				
	}
	
	@Test
	public void testPlayer3StraightFlush() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player3_straightflush.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Dealer with a score of 8"),true);				
	}
	
	@Test
	public void testPlayer3TwoPair() throws InterruptedException, IOException {
		start_server("../Poker/test_starting_hands/player3_twopair.txt");
		driver.findElement(By.id("stay")).click();
		logs = driver.findElement(By.id("logs")).getAttribute("value");
		assertEquals(logs.contains("Winner is Dealer with a score of 2"),true);				
	}
	
	@After
	public void teardown() throws InterruptedException {
		server.destroy();
		driver.quit();
		Thread.sleep(1000); // Let the server reset a second between tests
	}
		
}
