package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class RegistrationTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void HappyPath() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void SubmitRequest() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(7);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    void ErrorDeliveryTheSelectedCityNotAvailable() {
        String planningDate = generateDate(3);
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=city] input").setValue("Ель");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void ErrorOrderForSelectedDateNotPossible() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(1);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void ErrorNameEN() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("John Jonson");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void PhoneErrorNoPlus() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void PhoneError10numbers() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+7123456789");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void ErrorCityEmpty() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void ErrorDateEmpty() {
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void ErrorNameEmpty() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void ErrorPhoneEmpty() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void ErrorNoFlagWithoutCheckbox() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Казань");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $(".button").click();
        $("[data-test-id='agreement'].input_invalid").shouldBe(visible);
    }

    @Test
    void ChoseCityFromList() {
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3);
        $("[data-test-id=city] input").setValue("Ка");
        $$(".popup .menu-item__control").findBy(text("Казань")).click();
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(BACK_SPACE);
        $("[data-test-id=date] input").setValue(String.valueOf(planningDate));
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
    }

    @AfterEach
    void closeWebBrowser() {
        closeWebDriver();
    }
}
