package com.cst438;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Enrollment;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndStudent {
    public static final String CHROME_DRIVER_FILE_LOCATION 
                          = "C:/chromedriver_win32/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final String ALIAS_NAME = "test";
    public static final int SLEEP_DURATION = 1000; // 1 second.
 
    @Autowired
    StudentRepository studentRepository;
    
    @Test
    public void playGame() throws Exception {
 
        // set the driver location and start driver
        //@formatter:off
        //
        // browser    property name                 Java Driver Class
        // -------  ------------------------    ----------------------
        // Edge     webdriver.edge.driver         EdgeDriver
        // FireFox     webdriver.firefox.driver     FirefoxDriver
        // IE         webdriver.ie.driver         InternetExplorerDriver
        // Chrome   webdriver.chrome.driver     ChromeDriver
        //
        //@formatter:on
 
        //TODO update the property name for your browser 
        System.setProperty("webdriver.chrome.driver",
                     CHROME_DRIVER_FILE_LOCATION);
        //TODO update the class ChromeDriver()  for your browser
        WebDriver driver = new ChromeDriver();
        
        try {
            WebElement we;
            
            driver.get(URL);
            // must have a short wait to allow time for the page to download 
            Thread.sleep(SLEEP_DURATION);
 
            // get the add student button
            we=driver.findElement(By.id("adds"));
            we.click();
            Thread.sleep(SLEEP_DURATION);
            
            we=driver.findElement(By.id("adds2"));
            we.click();
            Thread.sleep(SLEEP_DURATION);
            
            we=driver.findElement(By.id("t1"));
            we.sendKeys("999");
            
            we=driver.findElement(By.id("Add"));
            we.click();
            Thread.sleep(SLEEP_DURATION);
            
            Student e = studentRepository.findById(999).orElse(null);
			assertNotNull(e, "Student not found in database.");

           
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
            
        } finally {
            driver.quit();
        }
    }
}
        
        
