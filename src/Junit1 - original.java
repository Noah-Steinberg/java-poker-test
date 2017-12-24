import static org.junit.Assert.*;

import org.openqa.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.junit.*;
import org.junit.runner.JUnitCore;

public class Junit1 {

	private static WebDriver driver;
	
	/* 
	 * Each test will hit if it can or stay if it can't until
	 * it is in the right state to run the test. At which point
	 * it runs the test and will continue to the next one
	 */

	public static void main(String[] args) throws Exception {                    
	       JUnitCore.main("Junit1");            
	}
	
	public static boolean myTurn() {
		return driver.findElement(By.xpath("//*[@id='my-hand']/md-toolbar")).getCssValue("background-color").equals("rgba(64, 196, 255, 1)");
	}
	
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/");
	}
	
	@After
	public void teardown() throws InterruptedException {
		driver.quit();
		Thread.sleep(1000); // Let the server reset a second between tests
	}
	
	@Test
	public void sevenCard() throws InterruptedException {		
		// This test is bound to be freakishly long.
		// Go until 7 cards and not bust and see if winner
		while(true) {
			// If I have seven cards, exit the loop to do tests
			boolean iambust = !driver.findElement(By.cssSelector("#bust")).getAttribute("class").equals("ng-hide");
			if (!iambust)
				if (driver.findElements(By.cssSelector("#card-6")).size() > 0) {
					break;
			}
			// Otherwise, try pressing hit, then stay. 
			if (driver.findElement(By.cssSelector("#hit")).isEnabled())
				driver.findElement(By.cssSelector("#hit")).click();
			else
				driver.findElement(By.cssSelector("#stay")).click();
		}
		
		assertTrue(!driver.findElement(By.xpath("//*[@id='my-hand']/md-toolbar/div/h1/strong[1]")).getAttribute("class").equals("ng-hide"));
		
	}
	
	@Test
	public void splitIfFirstRoundPair() throws InterruptedException {		
		while(true) {
			// If it's under 21, break out and continue the test. 
			
			boolean onlyTwoCards = driver.findElements(By.cssSelector("#card-2")).size() == 0;
			
			if (onlyTwoCards) {
				String card1Rank = driver.findElement(By.cssSelector("#card-0 > p > span")).getText();
				String card2Rank = driver.findElement(By.cssSelector("#card-1 > p > span")).getText();
				
				if (card1Rank.equals(card2Rank))
					break;
				else {
					driver.findElement(By.cssSelector("#hit")).click();
					driver.findElement(By.cssSelector("#stay")).click();
					Thread.sleep(1000);
				}
			}
			else {
				// Otherwise, try pressing hit, then stay. 
				driver.findElement(By.cssSelector("#hit")).click();
				driver.findElement(By.cssSelector("#stay")).click();
			Thread.sleep(1000);
			}
		}
		
		while (!myTurn()) {} // zzz
		
		assertTrue("I should be able to split.", driver.findElement(By.cssSelector("#split")).isEnabled());
	}
	
	@Test
	public void testOnlyStayOn21() throws InterruptedException {		
		while(true) {
			String value = driver.findElement(By.cssSelector("#handValue")).getText();
			// If it's under 21, break out and continue the test. 
			if (Integer.parseInt(value) == 21)
				break;
			// Otherwise, try pressing hit 
			driver.findElement(By.cssSelector("#hit")).click();
		}
		
		while (!myTurn()) {} // zzz
		
		// Definitely my turn now. And I have a score of 21 and haven't stayed.
		String value = driver.findElement(By.cssSelector("#handValue")).getText();
		// Hit and split should be disabled.
		assertFalse("Hit should be disabled.", driver.findElement(By.cssSelector("#hit")).isEnabled());
		assertFalse("Split should be disabled.", driver.findElement(By.cssSelector("#split")).isEnabled());
		assertFalse("Stay should be enabled.", !driver.findElement(By.cssSelector("#stay")).isEnabled());
		
		// Click stay
		driver.findElement(By.cssSelector("#stay")).click();
		String value2= driver.findElement(By.cssSelector("#handValue")).getText();

		// They should be the same if I received a card.
		assertEquals("I should have the same value before + after.", value, value2);
	}
	
	@Test
	public void declareBustHitOver21() throws InterruptedException {		
		while(true) {
			String value = driver.findElement(By.cssSelector("#handValue")).getText();
			// If it's under 21, break out and continue the test. 
			if (Integer.parseInt(value) > 21)
				break;
			// Otherwise, try pressing hit, then stay. 
			driver.findElement(By.cssSelector("#hit")).click();
			driver.findElement(By.cssSelector("#stay")).click();
		}
		
		// I should be bust!
		boolean iambust = !driver.findElement(By.cssSelector("#bust")).getAttribute("class").equals("ng-hide");
		assertTrue("I should be bust right now. ", iambust);
	}
	
	@Test
	public void testHitIfUnder21() throws InterruptedException {		
		while(true) {
			String value = driver.findElement(By.cssSelector("#handValue")).getText();
			// If it's under 21, break out and continue the test. 
			if (Integer.parseInt(value) < 21)
				break;
			// Otherwise, try pressing hit, then stay. 
			driver.findElement(By.cssSelector("#hit")).click();
			driver.findElement(By.cssSelector("#stay")).click();
		}
		
		while (!myTurn()) {} // zzz
		
		// Definitely my turn now.
		String value = driver.findElement(By.cssSelector("#handValue")).getText();
		driver.findElement(By.cssSelector("#hit")).click();
		String value2= driver.findElement(By.cssSelector("#handValue")).getText();

		// They should be the same if I received a card.
		assertFalse("I shouldn't have the same value in my hand after hitting. ", value.equals(value2));
	}
	
	@Test
	public void iMayStayUnder21() throws InterruptedException {		
		while(true) {
			String value = driver.findElement(By.cssSelector("#handValue")).getText();
			// If it's under 21, break out and continue the test. 
			if (Integer.parseInt(value) < 21)
				break;
			// Otherwise, try pressing hit, then stay. 
			driver.findElement(By.cssSelector("#hit")).click();
			driver.findElement(By.cssSelector("#stay")).click();
		}
		
		while (!myTurn()) {} // zzz
		
		// Definitely my turn now.
		String value = driver.findElement(By.cssSelector("#handValue")).getText();
		assertTrue(driver.findElement(By.cssSelector("#stay")).isEnabled());
		driver.findElement(By.cssSelector("#stay")).click();
		String value2= driver.findElement(By.cssSelector("#handValue")).getText();

		// They should be the same if I received a card.
		assertEquals("I shouldn't have the same value in my hand after hitting. ", value, value2);
	}
	
	@Test
	public void allHaveOneHidden() {		
		// The term "Hidden" should appear 3 times; for the 3 hidden cards of the opposition
		// Checks for paragraphs with text Hidden which don't have the ng-hide class (i.e. they are not on screen)
		assertEquals(3, driver.findElements(By.xpath("//p[contains(text(),'Hidden') and not(contains(@class,'ng-hide'))]")).size());
	}
	
	@Test
	public void testReceivedTwoCards() {		
		// Check if I have two cards. The player's cards are ID'd card-#, starting at 0. 0 and 1 will exist, 2 shouldn't.
		assertEquals(0, driver.findElements(By.cssSelector("#card-2")).size());
		assertEquals(1, driver.findElements(By.cssSelector("#card-0")).size());
		assertEquals(1, driver.findElements(By.cssSelector("#card-1")).size());
	}
		
}
