package ru.vorobeij

import java.io.File
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

/**
 * List all cleanup scripts with specific assembleDebug gradle task
 */
fun main(args: Array<String>) {

    val parser = ArgParser("cleaner")
    val projectPath by parser.option(ArgType.String, fullName = "projectPath", description = "project root").required()
    val taskPrefix by parser.option(ArgType.String, fullName = "taskPrefix", description = "task prefix, ex assembleDebug or assemble")
        .required()
    parser.parse(args)

    val script = loadScriptFile(projectPath, taskPrefix)
    File("cleanup.sh").writeText(script)
}

