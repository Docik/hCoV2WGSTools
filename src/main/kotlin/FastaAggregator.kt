import java.io.File
import java.io.PrintWriter
import java.util.*

class FastaAggregator {

    companion object {
        const val INPUT_DIRECTORY = ".\\src\\main\\resources"
        const val OUTPUT_FILE_NAME = ".\\src\\main\\resources\\output.fasta"
        const val FILTER_FILE_NAME = ".\\src\\main\\resources\\filter.txt"
    }

    private val filter = mutableListOf<String>()

    fun aggregate() {
        populateFilter()

        File(OUTPUT_FILE_NAME).printWriter().use { out ->
            File(INPUT_DIRECTORY).walk().forEach { file ->
                if (isFastaFile(file) && file != File(OUTPUT_FILE_NAME)) {
                    writeFileData(file, out)
                }
            }
        }

        consoleReport()
    }

    private fun consoleReport() {
        if (filter.isNotEmpty()) {
            println("\nFasta files, not found for samples:")
            filter.forEach { println(it) }
        }
    }

    private fun populateFilter() {
        val filterFile = File(FILTER_FILE_NAME)
        if (filterFile.exists()) {
            filterFile.forEachLine { line ->
                if (line.isNotEmpty()) {
                    filter.add(line.trim())
                }
            }
        }
    }

    private fun isFastaFile(file: File): Boolean {
        return file.toString().substringAfterLast(".") == "fasta"
    }

    private fun writeFileData(file: File, out: PrintWriter) {
        file.useLines { lines ->
            var id = ""
            lines.forEach { line->
                if (line.startsWith(">")) {
                    id = line.substring(line.indexOf('_') + 1)
                    if (filter.isNotEmpty() && !filter.contains(id)) {
                        println("Fasta file ignored by filter: $file")
                        return
                    }
                    val year = Calendar.getInstance().get(Calendar.YEAR)
                    out.println(getSequenceId(id, year))
                } else {
                    out.println(line)
                    filter.remove(id)
                }
            }
        }
    }

    private fun getSequenceId(id: String, year: Int): String {
        return ">hCoV-19/Estonia/$id/$year"
    }
}
