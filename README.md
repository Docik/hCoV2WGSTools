# hCoV2WGSTools

## 1. Fasta Aggregator tool

This tool aggregates sequences from several .fasta files into one.
Current version of the tool is designed to be launched in IntelliJ Idea IDE.

### Usage

Put your fasta files to ".\src\main\resources" directory, launch MainKt and observe results in "output.fasta" file of the same directory.

Optionally you can put "filter.txt" file to the same directory to filter out some samples. 
"filter.txt" should have the list of newline-separated sample ids to be included into the output file.

###Notes

Tool does not accept any launch params yet.
