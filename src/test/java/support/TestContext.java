// Created by Viacheslav (Slava) Skryabin 04/01/2011
package support;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void initialize() {
        initialize(
            System.getProperty("test.browser", "chrome"),
            System.getProperty("test.envtype", "local"),
            Boolean.parseBoolean(System.getProperty("test.headless")),
            Boolean.parseBoolean(System.getProperty("test.force_webdriver", "true"))
        );
    }

    public static void teardown() {
        driver.quit();
    }

    /**
     * @param browser          a browser to use
     * @param testEnv          use <code>'local'</code> for test on local machine or <code>'grid'</code> to run it on
     *                         <a href="https://www.selenium.dev/documentation/grid/">Selenium Grid</a>
     * @param isHeadless       do not use GUI when possible (on a *NIX without X server, for example)
     * @param forceWebdriver   do enforce using of a dedicated WebDriver.<br/>
     *                         The reason (to not enforce):
     *                         {@link io.github.bonigarcia.wdm.WebDriverManager} has an unreliable paltform detection mechanism.
     *                         For example: it treats <code>aarch64</code> as <code>x86_64</code>
     */
    public static void initialize(String browser, String testEnv, boolean isHeadless, boolean forceWebdriver) {
        Dimension size = new Dimension(1920, 1080);
        Point position = new Point(0, 0);
        if (testEnv.equals("local")) {
            switch (browser) {
                case "chrome":
                    if (forceWebdriver)
                        WebDriverManager.chromedriver().setup();
                    Map<String, Object> chromePreferences = new HashMap<>();
                    chromePreferences.put("profile.default_content_settings.geolocation", 2);
                    chromePreferences.put("profile.default_content_settings.popups", 0);
                    chromePreferences.put("download.prompt_for_download", false);
                    chromePreferences.put("download.directory_upgrade", true);
                    chromePreferences.put("download.default_directory", System.getProperty("user.dir") + "/src/test/resources/downloads");
                    chromePreferences.put("safebrowsing.enabled", false);
                    chromePreferences.put("plugins.always_open_pdf_externally", true);
                    chromePreferences.put("plugins.plugins_disabled", new ArrayList<String>(){{ add("Chrome PDF Viewer"); }});
                    chromePreferences.put("credentials_enable_service", false);
                    chromePreferences.put("password_manager_enabled", false);
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.setExperimentalOption("prefs", chromePreferences);
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
                    if (isHeadless) {
                        chromeOptions.setHeadless(true);
                        chromeOptions.addArguments("--window-size=" + size.getWidth() + "," + size.getWidth());
                        chromeOptions.addArguments("--disable-gpu");
                    }
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    if (forceWebdriver)
                        WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) {
                        FirefoxBinary firefoxBinary = new FirefoxBinary();
                        firefoxBinary.addCommandLineOptions("--headless");
                        firefoxOptions.setBinary(firefoxBinary);
                    }
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "safari":
                    driver = new SafariDriver();
                    driver.manage().window().setPosition(position);
                    driver.manage().window().setSize(size);
                    break;
                case "edge":
                    if (forceWebdriver)
                        WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                case "ie":
                    if (forceWebdriver)
                        WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                default:
                    throw new RuntimeException("Driver is not implemented for: " + browser);
            }
        } else if (testEnv.equals("grid")){
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(browser);
            capabilities.setPlatform(Platform.ANY);
            try {
                URL hubUrl = new URL("http://localhost:4444/wd/hub");
                driver = new RemoteWebDriver(hubUrl, capabilities);
                ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Unsupported test environment: " + testEnv);
        }
    }
}
