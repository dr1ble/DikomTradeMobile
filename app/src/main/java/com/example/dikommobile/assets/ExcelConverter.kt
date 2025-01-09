import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File

fun excelToJson(inputFile: String, outputDir: String, mainHeaderRowNum: Int, subHeaderRowNum: Int, dataStartRow: Int) {
    val objectMapper = jacksonObjectMapper() // Создание объекта objectMapper

    val workbook = WorkbookFactory.create(File(inputFile))
    val sheet = workbook.getSheetAt(0)

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
    while (rows.hasNext()) {
        val row = rows.next()
        val rowData = mainHeaders.mapIndexed { index, header ->
            val englishHeader = when (header) {
                "Фото" -> "Photo"
                "Артикул" -> "Article"
                "Наименование" -> "Name"
                "Описание/комплектация" -> "Description"
                "Цена (руб)" -> "Price (RUB)"
                else -> header
            }

            if (index == heightIndex || index == widthIndex || index == depthIndex) {
                null // Эти данные будут обработаны позже
            } else {
                englishHeader to (row.getCell(index)?.toString()?.replace("\u00A0", "")?.replace("#NULL!", "")
                    ?.trim() ?: "")
            }
        }.filterNotNull().toMap().toMutableMap()

        // Добавляем габариты как отдельные строки с англоязычными ключами
        if (heightIndex != -1) rowData["Height"] =
            row.getCell(heightIndex)?.toString()?.replace("\u00A0", "")?.toIntOrNull().toString()
        if (widthIndex != -1) rowData["Width"] =
            row.getCell(widthIndex)?.toString()?.replace("\u00A0", "")?.toIntOrNull().toString()
        if (depthIndex != -1) rowData["Depth"] =
            row.getCell(depthIndex)?.toString()?.replace("\u00A0", "")?.toIntOrNull().toString()

        // Удаляем пустые или некорректные записи
        rowData.entries.removeIf { it.value == null || it.value.toString().isBlank() }
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
    val inputExcelFile = "app/src/main/java/com/example/dikommobile/assets/excelData/verstaki.xlsx" // Путь к Excel файлу
    val outputDirectory = "app/src/main/java/com/example/dikommobile/assets/outputData" // Папка для сохранения JSON

    val mainHeaderRowNum = 18 // Номер строки с основными заголовками
    val subHeaderRowNum = 19  // Номер строки с подзаголовками (В, Ш, Г)
    val dataStartRow = 20     // Номер строки, с которой начинаются данные

    excelToJson(inputExcelFile, outputDirectory, mainHeaderRowNum, subHeaderRowNum, dataStartRow)
}
