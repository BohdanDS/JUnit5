package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Queries;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.attachment;
import static io.qameta.allure.Allure.step;

public class SearchTest extends TestBase {


    static Queries iPhone = Queries.IPHONE;
    static Queries macBook = Queries.MAC;
    static Queries airPods = Queries.AIRPODS;

    @BeforeEach
    void setUp() {

        step("Открываем главную страницу", () -> open("https://www.apple.com/"));

        step("Нажимаем на иконку поиска", () -> $("#globalnav-menubutton-link-search").click());
    }

    @ValueSource(strings = {"Iphone 15", "AirPods", "Macbook"})
    @ParameterizedTest
    @Tags({
            @Tag("CRITICAL"),
            @Tag("WEB"),
    })
    void verifySearchReturnsNotEmptyResultsTest(String searchQuery) {

        SelenideLogger.addListener("allure", new AllureSelenide());

        step("В поле поиска вводим значение: " + searchQuery, () -> {
            $("[placeholder='Search apple.com']").setValue(searchQuery).pressEnter();
            attachment("Source", webdriver().driver().source());
        });
        step("Проверяем что отображаются ответы", () -> {
            $$("#exploreCurated").shouldBe(sizeGreaterThan(0));
        });

    }

    @CsvFileSource(resources = "/test_data/verifySearchReturnsCorrectResultTest.csv", delimiter = '|')
    @ParameterizedTest(name = "Проверка первого результата {1} по запросу {0}")
    @Tags({
            @Tag("SMOKE"),
            @Tag("WEB"),
    })
    void verifyFirstSearchResultTest(String searchQuery, String searchResult) {

        SelenideLogger.addListener("allure", new AllureSelenide());

        $("[placeholder='Search apple.com']").setValue(searchQuery).pressEnter();
        $(".rf-serp-explore-curated-position-1 h2").shouldHave(text(searchResult));
    }

    static Stream<Arguments> searchQueriesAndResults() {
        return Stream.of(
                Arguments.of(
                        iPhone.getDescription(),
                        List.of("iPhone 15 Pro and iPhone 15 Pro Max", "iPhone 15 and iPhone 15 Plus")
                ),
                Arguments.of(
                        macBook.getDescription(),
                        List.of("MacBook Pro", "MacBook Air", "Compare Mac models")
                ),
                Arguments.of(
                        airPods.getDescription(),
                        List.of("AirPods", "AirPods (3rd generation)", "AirPods Pro (2nd generation)")
                )
        );
    }

    @MethodSource("searchQueriesAndResults")
    @Tags({
            @Tag("CRITICAL"),
            @Tag("WEB"),
    })
    @ParameterizedTest(name = "Поиск по запросу {0} и сравнение результатов {1}")
    void verifySearchResultsTest(String searchQuery, List<String> searchResults) {

        SelenideLogger.addListener("allure", new AllureSelenide());

        $("[placeholder='Search apple.com']").setValue(searchQuery).pressEnter();
        $$("#exploreCurated h2").filter(visible).shouldHave(texts(searchResults));

    }
}
