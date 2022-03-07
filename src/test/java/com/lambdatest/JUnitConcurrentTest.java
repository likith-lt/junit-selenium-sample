package com.lambdatest;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
 
@RunWith(Parallelized.class)
public class JUnitConcurrentTest {
    String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
    String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
    public String gridURL = "@hub.lambdatest.com/wd/hub";
 
     public String platformName;
     public String browserName;
     public String browserVersion;
     public String selenium_version;
     public RemoteWebDriver driver = null;
     public String status = "failed";
    
     @Parameterized.Parameters
     public static LinkedList<String[]> getEnvironments() throws Exception {
        LinkedList<String[]> env = new LinkedList<String[]>();
        env.add(new String[]{"Windows 11", "Chrome", "98.0", "4.0.0"});
        env.add(new String[]{"Windows 11","Firefox","97.0", "4.0.0"});
        env.add(new String[]{"Windows 11","Edge","97.0", "4.0.0"});
        return env;
    }
   
    public JUnitConcurrentTest(String platformName, String browserName, String browserVersion, String selenium_version) {
        this.platformName = platformName;
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.selenium_version = selenium_version;
     }
    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("version", browserVersion);
        capabilities.setCapability("build", "JUnit - Java [Parallel]");
        capabilities.setCapability("name", "JUnit Parallel demo test");
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("selenium_version", selenium_version);
        // capabilities.setCapability("network", true); // To enable network logs
        // capabilities.setCapability("visual", true); // To enable step by step screenshot
        // capabilities.setCapability("video", true); // To enable video recording
        // capabilities.setCapability("console", true); // To capture console logs
        try {
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + gridURL), capabilities);
        } catch (MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void testParallel() throws Exception {
       try {
        System.out.println("Loading Url");
        driver.get("https://stage-demo.lambdatest.com/");

            // Let's select the location
            driver.findElement(By.id("headlessui-listbox-button-1")).click();
            driver.findElement(By.id("Bali")).click();
            System.out.println("Location is selected as Bali.");
            // Let's select the number of guests
            driver.findElement(By.id("headlessui-listbox-button-5")).click();
            driver.findElement(By.id("2")).click();
            System.out.println("Number of guests are selected.");
            driver.findElement(By.xpath("//*[@id='search']")).click();
            Thread.sleep(3000);
            // Let's select one of the hotels for booking
            driver.findElement(By.id("reserve-now")).click();
            Thread.sleep(3000);
            driver.findElement(By.id("proceed")).click();
            Thread.sleep(3000);
            System.out.println("Booking is confirmed.");
            // Let's download the invoice
            boolean exec = driver.findElement(By.id("invoice")).isDisplayed();
            if(exec){
                status = "passed";
                driver.findElement(By.id("invoice")).click();
                System.out.println("Tests are run successfully!");
            }
            else
                status="failed";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }  
    @After
    public void tearDown() throws Exception {
       if (driver != null) {
            driver.executeScript("lambda-status=" + status);
            driver.quit();
        }
    }
}
