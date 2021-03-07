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
                    println("Fasta file found: $file")
                    writeFileData(file, out)
                }
            }
        }
    }

    private fun populateFilter() {
        val filterFile = File(FILTER_FILE_NAME)
        if (filterFile.exists()) {
            filterFile.forEachLine { line -> filter.add(line) }
        }
    }

    private fun isFastaFile(file: File): Boolean {
        return file.toString().substringAfterLast(".") == "fasta"
    }

    private fun writeFileData(file: File, out: PrintWriter) {
        file.useLines { lines ->
            lines.forEach { line->
                if (line.startsWith(">")) {
                    val id = line.substringAfterLast("_")
                    if (filter.isNotEmpty() && !filter.contains(id)) {
                        println("Fasta file ignored by filter: $file")
                        return
                    }
                    val year = Calendar.getInstance().get(Calendar.YEAR)
                    out.println(getSequenceId(id, year))
                } else {
                    out.println(line)
                }
            }
        }
    }

    private fun getSequenceId(id: String, year: Int): String {
        return ">hCoV-19/Estonia/$id/$year"
    }
}
