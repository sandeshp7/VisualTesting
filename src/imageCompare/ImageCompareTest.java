package imageCompare;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class ImageCompareTest {
	
	//Set to true if running the tests to capture the baseline images
	private static boolean isBaseline = false;
	
	public static void main(String[] args) {
		
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.google.co.uk/");
		
		String imageFileName = "TestImage";
		String userDirectory = System.getProperty("user.dir");
		
		if(isBaseline) {
			imageFileName = "BaseLineImage";
		}
		//This takes the screenshot
		
		try {
			Thread.sleep(3000);
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			//By default the image with be a png. We need to convert it to jpeg
			BufferedImage bufferedImage = ImageIO.read(scrFile);

            // create a blank, RGB, same width and height, and a white background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

            // write to jpeg file
            ImageIO.write(newBufferedImage, "jpg", new File(userDirectory + "/Screenshots/"+ imageFileName +".jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			driver.quit();
		}
		
		if(!isBaseline) {
			ImageCompare ic = new ImageCompare(userDirectory + "/Screenshots/" + "BaseLineImage.jpg", userDirectory + "/Screenshots/" + "TestImage.jpg");
			Boolean result = ic.compareImages();
			if(result) {
				System.out.println("Test Pass - No Differences");
			} else {
				System.out.println("Test Fail - There are some differences. Refer changes.jpg in the Changes folder");
			}
		}
	}
}
