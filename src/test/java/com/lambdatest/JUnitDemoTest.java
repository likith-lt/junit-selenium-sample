package com.lambdatest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class JUnitDemoTest {
    String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
    String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
    public static RemoteWebDriver driver = null;
    public String gridURL = "@hub.lambdatest.com/wd/hub";
    public String status = "failed";
    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "98.0");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("build", "JUnit - Java");
        ltOptions.put("name", "JUnit demo test");
        ltOptions.put("platformName", "Windows 11");
        ltOptions.put("selenium_version","4.0.0");
        // ltOptions.put("network", true); // To enable network logs
        // ltOptions.put("visual", true); // To enable step by step screenshot
        // ltOptions.put("video", true); // To enable video recording
        // ltOptions.put("console", true); // To capture console logs
        capabilities.setCapability("LT:Options", ltOptions);

        try {
            driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + gridURL), capabilities);
        } catch (MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
  
    @Test
    public void testSimple() throws Exception {
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

