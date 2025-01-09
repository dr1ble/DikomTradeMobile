package com.example.excelprocessor

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

fun extractImagesFromExcel(inputFile: String, outputDir: String) {
    val workbook = WorkbookFactory.create(File(inputFile)) as XSSFWorkbook
    val sheet = workbook.getSheetAt(0)

    // Получаем все изображения
    val pictureDataList = workbook.allPictures

    // Получаем имя входного файла без расширения
    val inputFileNameWithoutExtension = File(inputFile).nameWithoutExtension

    // Создаем папку для сохранения изображений с именем Excel файла, если её нет
    val outputDirectory = File(outputDir, inputFileNameWithoutExtension)
    if (!outputDirectory.exists()) {
        outputDirectory.mkdirs()
    }

    // Создаем папку для изображений, если её нет
    val imageDirectory = File(outputDirectory, "images")
    if (!imageDirectory.exists()) {
        imageDirectory.mkdirs()
    }

    // Индексы для артикула и других данных
    val rows = sheet.iterator()

    // Пропускаем строки до заголовков
    repeat(17) { if (rows.hasNext()) rows.next() } // Пропускаем 17 строк (до 18-й, включая заголовки)

    // Основной заголовок с артикулами
    val headerRow = rows.next()
    val headers = headerRow.map { it.toString().trim() }

    // Пропускаем строку с подзаголовками (19-я строка)
    if (rows.hasNext()) rows.next()

    // Начинаем обработку данных начиная с 20-й строки
    var usedImages = 0 // Счётчик использованных изображений
    var firstImageSkipped = false // Флаг для пропуска первого изображения

    // Обрабатываем строки данных
    while (rows.hasNext()) {
        val row = rows.next()
        val rowData = mutableMapOf<String, Any?>()

        // Если первая ячейка строки содержит слово "Элементы", пропускаем эту строку
        if (row.getCell(0)?.toString()?.trim() == "Элементы") {
            continue // Пропускаем строку с элементами
        }

        // Извлекаем данные по каждому столбцу
        headers.forEachIndexed { index, header ->
            val cell = row.getCell(index)
            if (cell != null) {
                rowData[header] = cell.toString().replace("\u00A0", "").trim()
            }
        }

        val artNumber = rowData["Артикул"]?.toString() ?: "unknown"

        // Пропускаем первое изображение
        if (!firstImageSkipped && pictureDataList.isNotEmpty()) {
            firstImageSkipped = true // Пропускаем первое изображение
            usedImages++ // Увеличиваем индекс, чтобы пропустить первое изображение
        }

        // Проверяем, есть ли еще изображения, и связываем их с товарами
        if (usedImages < pictureDataList.size) {
            val matchingPicture = pictureDataList[usedImages]

            // Получаем байты изображения
            val imageBytes: ByteArray = matchingPicture.data
            val fileExtension = matchingPicture.suggestFileExtension() // Расширение изображения
            val imageFileName = "$artNumber-$usedImages.$fileExtension" // Имя файла с артикулом и индексом изображения
            val imageFilePath = File(imageDirectory, imageFileName)

            // Записываем изображение в файл
            FileOutputStream(imageFilePath).use { outputStream ->
                outputStream.write(imageBytes)
            }

            println("Изображение с артикулом $artNumber сохранено: ${imageFilePath.absolutePath}")

            // Увеличиваем счётчик использованных изображений
            usedImages++
        }
    }

    workbook.close()
}

fun main() {
    val inputExcelFile = "excelprocessor/src/main/java/com/example/excelprocessor/excelData/verstaki.xlsx" // Путь к Excel файлу
    val outputDirectory = "excelprocessor/src/main/java/com/example/excelprocessor/outputImages" // Папка для сохранения изображений

    extractImagesFromExcel(inputExcelFile, outputDirectory)
}
