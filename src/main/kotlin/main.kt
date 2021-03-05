import java.io.File
import java.util.*
import java.util.Calendar.YEAR

const val OUTPUT_FILE_NAME = ".\\src\\main\\resources\\output.fasta"
const val INPUT_DIRECTORY = ".\\src\\main\\resources"

fun main(args: Array<String>) {
    File(OUTPUT_FILE_NAME).printWriter().use { out ->
        File(INPUT_DIRECTORY).walk().forEach { file ->
            if (isFastaFile(file) && file != File(OUTPUT_FILE_NAME)) {
                println("Fasta file found: $file")
                file.forEachLine { line ->
                    if (line.startsWith(">")) {
                        // Convert and write
                        val id = line.substringAfterLast("_")
                        val year = Calendar.getInstance().get(YEAR)
                        out.println(getSequenceId(id, year))
                    } else {
                        // Write
                        out.println(line)
                    }
                }
            }
        }
    }
}

private fun isFastaFile(file: File): Boolean {
    return file.toString().substringAfterLast(".") == "fasta"
}

private fun getSequenceId(id: String, year: Int): String {
    return ">hCoV-19/Estonia/$id/$year"
}