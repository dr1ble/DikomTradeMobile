package com.example.excelprocessor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.util.Base64

fun excelToJsonWithImages(inputFile: String, outputDir: String, mainHeaderRowNum: Int, subHeaderRowNum: Int, dataStartRow: Int) {
    val objectMapper = jacksonObjectMapper() // Создание объекта ObjectMapper

    val workbook = WorkbookFactory.create(File(inputFile)) as XSSFWorkbook
    val sheet = workbook.getSheetAt(0)
    val pictureDataList = workbook.allPictures

    val rows = sheet.iterator()
    val data = mutableListOf<Map<String, Any?>>()

    // Пропускаем строки до основной строки заголовков
    repeat(mainHeaderRowNum - 1) { if (rows.hasNext()) rows.next() }

    // Основные заголовки
    val mainHeaderRow = rows.next()
    val mainHeaders = mainHeaderRow.map { it.toString().replace("#NULL!", "").trim() }

    // Подзаголовки (например, "В", "Ш", "Г")
    val subHeaderRow = rows.next()
    val subHeaders = subHeaderRow.map { it.toString().replace("#NULL!", "").trim() }

    // Индексы для габаритов
    val heightIndex = subHeaders.indexOf("В")
    val widthIndex = subHeaders.indexOf("Ш")
    val depthIndex = subHeaders.indexOf("Г")

    // Пропускаем строки до данных
    repeat(dataStartRow - subHeaderRowNum - 1) { if (rows.hasNext()) rows.next() }

    // Обрабатываем строки данных
    var usedImages = 1 // Счётчик использованных изображений, начинаем с 1, чтобы пропустить первое изображение

    while (rows.hasNext()) {
        val row = rows.next()
        val rowData = mainHeaders.mapIndexed { index, header ->
            val englishHeader = when (header) {
                "Фото" -> "photo"
                "Артикул" -> "article"
                "Наименование" -> "name"
                "Описание/комплектация" -> "description"
                "Цена (руб)" -> "price"
                else -> header
            }

            if (index == heightIndex || index == widthIndex || index == depthIndex) {
                null // Эти данные будут обработаны позже
            } else {
                englishHeader to (row.getCell(index)?.toString()?.replace("\u00A0", "")?.replace("#NULL!", "")?.trim() ?: "")
            }
        }.filterNotNull().toMap().toMutableMap()

        // Добавляем габариты как отдельные строки с англоязычными ключами
        if (heightIndex != -1) rowData["height"] =
            row.getCell(heightIndex)?.toString()?.replace("\u00A0", "")?.toIntOrNull().toString()
        if (widthIndex != -1) rowData["width"] =
            row.getCell(widthIndex)?.toString()?.replace("\u00A0", "")?.toIntOrNull().toString()
        if (depthIndex != -1) rowData["depth"] =
            row.getCell(depthIndex)?.toString()?.replace("\u00A0", "")?.toIntOrNull().toString()

        // Удаляем пустые или некорректные записи
        rowData.entries.removeIf { it.value == null || it.value.toString().isBlank() }

        // Привязываем изображение к строке, если оно доступно
        if (usedImages < pictureDataList.size) {
            val matchingPicture = pictureDataList[usedImages] // Начинаем с второго изображения
            val imageBytes: ByteArray = matchingPicture.data
            val base64Image = Base64.getEncoder().encodeToString(imageBytes)
            rowData["photoBase64"] = base64Image

            // Увеличиваем счётчик использованных изображений
            usedImages++
        }

        data.add(rowData)
    }

    workbook.close()

    // Получаем имя выходного файла с добавлением расширения .json
    val outputJsonFile = "$outputDir/${File(inputFile).nameWithoutExtension}.json"

    // Конвертация в JSON
    val jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)

    // Сохранение JSON в файл
    File(outputJsonFile).writeText(jsonString)
    println("JSON файл успешно создан: $outputJsonFile")
}


fun main() {
    val inputExcelFile = "excelprocessor/src/main/java/com/example/excelprocessor/excelData/verstaki.xlsx" // Путь к Excel файлу
    val outputDirectory = "excelprocessor/src/main/java/com/example/excelprocessor/outputData" // Папка для сохранения JSON

    val mainHeaderRowNum = 18 // Номер строки с основными заголовками
    val subHeaderRowNum = 19  // Номер строки с подзаголовками (В, Ш, Г)
    val dataStartRow = 20     // Номер строки, с которой начинаются данные

    excelToJsonWithImages(inputExcelFile, outputDirectory, mainHeaderRowNum, subHeaderRowNum, dataStartRow)
}
