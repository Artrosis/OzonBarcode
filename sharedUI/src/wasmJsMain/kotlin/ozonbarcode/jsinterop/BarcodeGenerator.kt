@file:OptIn(ExperimentalWasmJsInterop::class)

package ozonbarcode.jsinterop

import kotlin.js.JsName

/**
 * Типобезопасная обёртка для JsBarcode библиотеки.
 * Предоставляет декларативный API для генерации штрихкодов.
 */
@OptIn(ExperimentalWasmJsInterop::class)
@JsName("JsBarcode")

external fun jsBarcode(
    element: JsAny,
    value: String,
    options: JsBarcodeOptions? = definedExternally
): JsAny

fun createBarcodeOptions(): JsBarcodeOptions = js("({})")

/**
 * Типобезопасная обёртка для JsBarcode с builder паттерном.
 */
object BarcodeGenerator {
    /**
     * Генерирует Code128 штрихкод.
     */
    fun generateCode128(
        element: JsAny,
        value: String,
        color: String = "#000000",
        backgroundColor: String = "#ffffff",
        width: Int = 2,
        height: Int = 100,
        displayValue: Boolean = true,
        fontOptions: String = "",
        fontSize: Int = 20,
        margin: Int = 10,
        text: String? = null
    ) {
        jsBarcode(
            element,
            value,
            createBarcodeOptions().apply {
            format = "CODE128"
            lineColor = color
            background = backgroundColor
            this.width = width.toDouble()
            this.height = height.toDouble()
            this.displayValue = displayValue
            this.fontOptions = fontOptions
            this.fontSize = fontSize.toDouble()
            this.margin = margin.toDouble()
            if (text != null) this.text = text
        })
    }
}

/**
 * Форматы штрихкодов поддерживаемые JsBarcode.
 */
@Suppress("unused")
enum class BarcodeFormat(val value: String) {
    CODE128("CODE128"),
    EAN13("EAN13"),
    EAN8("EAN8"),
    UPC("UPC"),
    UPC_E("UPC"),
    ITF14("ITF14"),
    MSI("MSI"),
    MSI10("MSI10"),
    MSI11("MSI11"),
    MSI1010("MSI1010"),
    MSI1110("MSI1110"),
    CODE39("CODE39"),
    CODE93("CODE93"),
    CODE11("CODE11"),
    CODABAR("CODABAR"),
    PHARMACODE("PHARMACODE")
}

/**
 * Опции для JsBarcode библиотеки.
 * Полная типобезопасная обёртка над JavaScript объектом.
 */
@Suppress("unused")
@JsName("JsBarcodeOptions")
external interface JsBarcodeOptions {
    /**
     * Формат штрихкода (CODE128, EAN13, UPC и т.д.)
     */
    var format: String?

    /**
     * Цвет линий штрихкода
     */
    var lineColor: String?

    /**
     * Цвет фона
     */
    var background: String?

    /**
     * Ширина линий (в пикселях)
     */
    var width: Double?

    /**
     * Высота штрихкода (в пикселях)
     */
    var height: Double?

    /**
     * Отображать ли текст под штрихкодом
     */
    var displayValue: Boolean?

    /**
     * Текст для отображения (по умолчанию используется value)
     */
    var text: String?

    /**
     * Размер шрифта (в пикселях)
     */
    var fontSize: Double?

    /**
     * Отступы вокруг штрихкода
     */
    var margin: Double?

    /**
     * Опции шрифта (например, "bold")
     */
    var fontOptions: String?

    /**
     * Шрифт для отображения текста
     */
    var font: String?

    /**
     * Выравнивание текста (center, left, right)
     */
    var textAlign: String?

    /**
     * Позиция текста (top, bottom, center)
     */
    var textPosition: String?

    /**
     * Смещение текста по Y
     */
    var textMargin: Double?

    /**
     * Заголовок над штрихкодом
     */
    var title: String?

    /**
     * Отступы (margin) для каждой стороны
     */
    var margins: JsBarcodeMargins?
}

/**
 * Опции отступов для штрихкода.
 */
@Suppress("unused")
@JsName("JsBarcodeMargins")
external interface JsBarcodeMargins {
    var top: Double?
    var right: Double?
    var bottom: Double?
    var left: Double?
}