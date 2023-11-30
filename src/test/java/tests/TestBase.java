package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {


    @BeforeAll
    static void beforeFillForm(){
        Configuration.browserSize = "1920x1080";
//        Configuration.pageLoadStrategy = "eager";
        Configuration.baseUrl= "https://www.onliner.by/";
        Configuration.holdBrowserOpen = false;
    }
}
