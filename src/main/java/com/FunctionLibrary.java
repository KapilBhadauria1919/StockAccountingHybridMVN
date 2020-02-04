package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class FunctionLibrary {
	
public static WebDriver startBrowser(WebDriver driver) throws Exception{
		
		if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver", "D:\\Batch81\\StockAccounting_Hybrid\\drivers\\chromedriver.exe");
			driver=new ChromeDriver();
		}else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", "D:\\Batch81\\StockAccounting_Hybrid\\drivers\\geckodriver.exe");
			driver=new FirefoxDriver();
		}else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("ie")){
			System.setProperty("webdriver.InternetExplorer.driver", "D:\\Batch81\\StockAccounting_Hybrid\\drivers\\IEDriverServer.exe");
			driver=new InternetExplorerDriver();
		}else{
			System.out.println("invalid browser");
		}
		
		return driver;
	}
	
	
	public static void openApplication(WebDriver driver) throws Exception{
			driver.get(PropertyFileUtil.getValueForKey("Url"));
			driver.manage().window().maximize();
	}
	
	public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testdata){
		   if(locatortype.equalsIgnoreCase("id")){
			   driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		   }else if (locatortype.equalsIgnoreCase("name")){
			   driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
			   
		   }else if(locatortype.equalsIgnoreCase("xpath")){
			   driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);  
		   }
		   else{
				System.out.println("Locator not matching for typeAction method");
			}
	}
	
	public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String testdata){
		
		WebDriverWait mywait=new WebDriverWait(driver,Integer.parseInt(testdata));
		
		if(locatortype.equalsIgnoreCase("id")){
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
		}else if (locatortype.equalsIgnoreCase("name")){	
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
		}else if(locatortype.equalsIgnoreCase("xpath")){
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
		}
		else
		{
			System.out.println("unable to locate for waitForElement method");
		}
		
	}
	
	public static void clickAction(WebDriver driver,String locatortype,String locatorvalue){
		if(locatortype.equalsIgnoreCase("id")){
			driver.findElement(By.id(locatorvalue)).click();
		}else if (locatortype.equalsIgnoreCase("name")){	
			driver.findElement(By.name(locatorvalue)).click();
		}else if(locatortype.equalsIgnoreCase("xpath")){
			driver.findElement(By.xpath(locatorvalue)).click();
		}
		else{
			System.out.println("Unable to locate for ClickAction method");	
		}
	}
	
	public static void closeBrowser(WebDriver driver){
		 driver.close();
	}
	
	public static String generateDate(){
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_ss");
		return sdf.format(date);
	}
	
	public static void CaptureData(WebDriver driver, String locatortype,String locatorvalue) throws Throwable{
		
		String supplierdata="";
		if(locatortype.equalsIgnoreCase("id"))
		{
			supplierdata = driver.findElement(By.id(locatortype)).getAttribute("Value");
		}
		
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			supplierdata = driver.findElement(By.xpath(locatortype)).getAttribute("Value");
		}
		
		FileWriter fw = new FileWriter("D:\\KapilBatch81\\StockAccounting_Hybrid\\CaptureData\\suppnumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(supplierdata);
		bw.flush();
		bw.close();
		
	}
	
	public static void tableValidation(WebDriver driver, String column) throws Throwable
	{
		
		FileReader fr=new FileReader("D:\\KapilBatch81\\StockAccounting_Hybrid\\CaptureData\\suppnumber.txt");
		BufferedReader br=new BufferedReader(fr);
		
		String Exp_data=br.readLine();
		
		if(driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-box"))).isDisplayed()){
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-box"))).clear();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-box"))).sendKeys(Exp_data);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("click-search"))).click();
			
			
		}else{
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Click-searchpanel"))).click();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-box"))).clear();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-box"))).sendKeys(Exp_data);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("click-search"))).click();
		}
		
		WebElement table=driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("sup-table")));
		
		List<WebElement>rows=table.findElements(By.tagName("tr"));
		
		for(int i=1;i<rows.size()-1;i++){
			
			String act_data=driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr["+i+"]/td["+column+"]/div/span/span")).getText();
			
			Assert.assertEquals(act_data, Exp_data);
			
			System.out.println(act_data+"   "+Exp_data);
			break;		
		}
		
	}


}
