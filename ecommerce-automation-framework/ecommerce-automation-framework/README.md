# E-Commerce Hybrid Automation Framework

A Selenium WebDriver + TestNG hybrid automation framework built from scratch to test the
**Demoblaze** e-commerce demo application (login, product search, cart, checkout).

"Hybrid" here means: **Page Object Model + Page Factory** for UI structure, **data-driven
testing** via Apache POI/Excel for test inputs, and **keyword-style reusable action methods**
in `BasePage` — combined into one framework rather than using only one pattern.

## Tech Stack

| Layer | Tool |
|---|---|
| Language | Java 11 |
| Browser automation | Selenium WebDriver 4 |
| Test orchestration | TestNG |
| Build tool | Maven |
| Test data | Apache POI (Excel) |
| Reporting | ExtentReports |
| Logging | Log4j2 |
| Driver management | WebDriverManager (Bonigarcia) |
| Version control | Git / GitHub |

## Project Structure

```
ecommerce-automation-framework/
├── pom.xml
├── testng.xml                     # full regression suite (parallel="classes")
├── testng-smoke.xml                # quick smoke suite (functional group only)
├── src/
│   ├── main/java/
│   │   ├── pages/                  # Page Object Model classes (Page Factory)
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── HomePage.java
│   │   │   ├── ProductPage.java
│   │   │   ├── CartPage.java
│   │   │   └── CheckoutPage.java
│   │   └── utils/
│   │       ├── ConfigReader.java
│   │       ├── DriverManager.java  # ThreadLocal<WebDriver> for parallel runs
│   │       ├── ExcelUtils.java     # Apache POI reader
│   │       ├── ExtentReportManager.java
│   │       └── ScreenshotUtils.java
│   ├── main/resources/
│   │   ├── config.properties
│   │   └── log4j2.xml
│   └── test/java/
│       ├── tests/
│       │   ├── BaseTest.java       # @BeforeMethod/@AfterMethod lifecycle
│       │   ├── LoginTest.java
│       │   ├── ProductSearchTest.java
│       │   ├── CartTest.java
│       │   └── CheckoutTest.java
│       └── testdata/
│           └── DataProviderUtils.java
└── src/test/resources/testdata/
    └── TestData.xlsx               # LoginData, SearchData, CheckoutData sheets
```

## Framework Design

- **Page Object Model + Page Factory** — every page is a class; `@FindBy` locators are
  lazily initialized by `PageFactory.initElements()` in `BasePage`'s constructor, so element
  lookup happens on first use, not at page-object construction.
- **Data-driven testing** — `ExcelUtils` reads sheets into `Object[][]`, consumed by
  `@DataProvider` methods in `DataProviderUtils`. The same `@Test` method runs once per Excel
  row, so adding a new test case is often just adding a new spreadsheet row.
- **Parallel execution** — `testng.xml` runs test classes in parallel (`parallel="classes"
  thread-count="4"`). `DriverManager` uses a `ThreadLocal<WebDriver>` so each thread owns an
  isolated browser session.
- **Groups** — every test is tagged `functional` and/or `regression`, so a quick smoke check
  (`testng-smoke.xml`) and a full regression run (`testng.xml`) can both draw from the same
  test classes without duplicating code.
- **Reporting & logging** — `ExtentReportManager` produces `reports/ExtentReport.html`;
  failures auto-attach a screenshot via `ScreenshotUtils`. `Log4j2` writes a rolling file log
  under `logs/` for debugging failed runs.

## Setup

**Prerequisites:** JDK 11+, Maven 3.6+, Chrome or Firefox installed.

```bash
git clone <your-repo-url>
cd ecommerce-automation-framework
mvn clean install -DskipTests
```

## Running Tests

```bash
# Full regression suite (parallel, all groups tagged "regression")
mvn test

# Smoke suite only (functional group, sequential)
mvn test -DsuiteXmlFile=testng-smoke.xml

# Run a single test class
mvn test -Dtest=LoginTest
```

## Reports & Logs

- HTML report: `reports/ExtentReport.html` (open in any browser after a run)
- Failure screenshots: `reports/screenshots/`
- Execution logs: `logs/automation.log`

## Configuration

All environment settings live in `src/main/resources/config.properties` — base URL, browser
choice, timeouts, and file paths. Switching from Chrome to Firefox or toggling headless mode
is a one-line change there, no code edits needed.

## Notes

This framework targets [Demoblaze](https://www.demoblaze.com), a public e-commerce demo site,
which is why locators are pinned to its DOM. Login credentials in `TestData.xlsx` are
placeholder values — replace with a real registered demo account (or your own AUT's test
accounts) before running against a live target.
