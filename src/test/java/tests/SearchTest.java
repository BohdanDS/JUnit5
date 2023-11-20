package tests;

import data.Queries;
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

public class SearchTest extends TestBase {

    @BeforeEach
    void setUp() {
        open("https://www.apple.com/");

        $("#globalnav-menubutton-link-search").click();
    }

    @ValueSource(strings = {"Iphone 15", "AirPods", "Macbook"})
    @ParameterizedTest
    @Tags({
            @Tag("CRITICAL"),
            @Tag("WEB"),
    })
    void verifySearchReturnsNotEmptyResultsTest(String searchQuery) {

        $("[placeholder='Search apple.com']").setValue(searchQuery).pressEnter();
        $$("#exploreCurated").shouldBe(sizeGreaterThan(0));
    }

    @CsvFileSource(resources = "/test_data/verifySearchReturnsCorrectResultTest.csv", delimiter = '|')
    @ParameterizedTest(name="Проверка первого результата {1} по запросу {0}")
    @Tags({
            @Tag("SMOKE"),
            @Tag("WEB"),
    })
    void verifyFirstSearchResultTest(String searchQuery, String searchResult){
        $("[placeholder='Search apple.com']").setValue(searchQuery).pressEnter();
        $(".rf-serp-explore-curated-position-1 h2").shouldHave(text(searchResult));
    }

    static Stream<Arguments> verifySearchResultTest() {
        return Stream.of(
                Arguments.of(
                        Queries.IPHONE,
                        List.of("iPhone 15 Pro and iPhone 15 Pro Max", "iPhone 15 and iPhone 15 Plus")
                ),
                Arguments.of(
                        Queries.MAC,
                        List.of("MacBook Pro", "MacBook Air", "Compare Mac models")
                ),
                Arguments.of(
                        Queries.AIRPODS,
                        List.of("AirPods", "AirPods (3rd generation)", "AirPods Pro (2nd generation)")
                )
        );
    }
    @MethodSource
    @Tags({
            @Tag("CRITICAL"),
            @Tag("WEB"),
    })
    @ParameterizedTest(name = "Поиск по запросу {0} и сравнение результатов {1}")
    void verifySearchResultsTest(Queries queries, List<String> searchResults) {
        $("[placeholder='Search apple.com']").setValue(queries.description).pressEnter();
        $$("#exploreCurated h2").filter(visible).shouldHave(texts(searchResults));

    }
}
