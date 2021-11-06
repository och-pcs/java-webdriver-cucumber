# java-webdriver-cucumber

Base framework for Java Webdriver automation with Cucumber

## Options

* `test.browser`: `chrome` <kbd>def</kbd> | `firefox` | `safari` | `edge` | `ie` - a browser to use
* `test.envtype`: `local` <kbd>def</kbd> | `grid` - local machine or [Selenium Grid](https://www.selenium.dev/documentation/grid/)
* `test.headless`: `false` <kbd>def</kbd> | `true` - do not use GUI when possible (on a \*NIX without X server, for example)
* `test.force_webdriver`: `true` <kbd>def</kbd> | `false` - do enforce using of a dedicated WebDriver.<br/>
  The reason (to not enforce):
  `io.github.bonigarcia.wdm.WebDriverManager` has an unreliable paltform detection mechanism.
  For example: it treats `aarch64` as `x86_64`

Example:
```sh
mvn test -Dtest.force_webdriver=false -Dtest.browser=firefox
```
