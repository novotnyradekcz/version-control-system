package svcs

import java.io.File
import java.security.MessageDigest

fun main(args: Array<String>) {
    // code from stage 1

    val commands = mapOf(
        "config" to "Get and set a username.",
        "add" to "Add a file to the index.",
        "log" to "Show commit logs.",
        "commit" to "Save changes.",
        "checkout" to "Restore a file."
    )

    // create separator
    val separator = File.separator
    // create directory vcs
    val vcs = File("vcs")
    if (!vcs.exists()) vcs.mkdir()
    // create file config.txt
    val config = File("vcs${separator}config.txt")
    if (!config.exists()) config.createNewFile()
    // create file index.txt
    val index = File("vcs${separator}index.txt")
    if (!index.exists()) index.createNewFile()
    // create file log.txt
    val log = File("vcs${separator}log.txt")
    if (!log.exists()) log.createNewFile()
    // create folder commits
    val commits = File("vcs${separator}commits")
    if (!commits.exists()) commits.mkdir()
    // check input
    when {
        args.isEmpty() || args[0] == "--help" -> {
            println("These are SVCS commands:")
            for (command in commands) println("%-10s %s".format(command.key, command.value))
        }
        args[0] == "log" -> {
            val logContent = log.readLines()
            if (logContent.isNotEmpty()) {
                for (commit in logContent) {
                    println(commit)
                }
            } else {
                println("No commits yet.")
            }
        }
        args.size == 1 && args[0] == "commit" -> {
            println("Message was not passed.")
        }
        args.size == 2 && args[0] == "commit" -> {
            // New version of commit command:
            // read the added file names
            val indexContent = index.readLines()
            val currentFiles = byteArrayOf()
            // load all the file contents into a byte array
            for (file in indexContent) {
                val currentFile = File(file)
                val fileContent = currentFile.readBytes()
                currentFiles.plus(fileContent)
            }
            // calculate hash of current files
            val currentDigest = MessageDigest.getInstance("SHA-256")
            currentDigest.update(currentFiles)
            val currentHash = currentDigest.digest().toString()
            // converting the byte array in to HexString format
            val hexString = StringBuffer()
            for (i in currentHash.indices) {
                hexString.append(Integer.toHexString(0xFF and currentHash[i].code))
            }
            // compare current hash to hash from log
            val logHashes = log.readText()
            if (hexString[0] in logHashes && hexString[1] in logHashes && hexString[2] in logHashes && hexString[3] in logHashes &&
                hexString[4] in logHashes && hexString[5] in logHashes && hexString[6] in logHashes && hexString[7] in logHashes &&
                hexString[8] in logHashes && hexString[9] in logHashes) {
                println("Nothing to commit.")
            } else {
                // if changes were made, create commit folder
                val commitFolder = File("vcs${separator}commits${separator}${hexString}")
                commitFolder.mkdir()
                for (file in indexContent) {
                    // create committed files in commit folder and copy their content
                    val committedFile = File("vcs${separator}commits${separator}${hexString}${separator}${file}")
                    committedFile.createNewFile()
                    val commitFile = File(file)
                    committedFile.writeBytes(commitFile.readBytes())
                }
                // and write it down in the log (copy current log, write new entry and then append copied log)
                val tempLog = log.readText()
                log.writeText("commit $hexString")
                log.appendText("\nAuthor: ${config.readText()}")
                log.appendText("\n${args[1]}")
                log.appendText("\n$tempLog")
                println("Changes are committed.")
            }
        }
        args.size == 1 && args[0] == "config" -> {
            val configContent = config.readText()
            if (configContent.isNotEmpty()) {
                println("The username is ${configContent}.")
            } else {
                println("Please, tell me who you are.")
            }
        }
        args.size == 2 && args[0] == "config" -> {
            config.writeText(args[1])
            println("The username is ${args[1]}.")
        }
        args.size == 1 && args[0] == "add" -> {
            val indexContent = index.readLines()
            if (indexContent.isNotEmpty()) {
                println("Tracked files:")
                for (file in indexContent) println(file)
            } else {
                println("Add a file to the index.")
            }
        }
        args.size == 2 && args[0] == "add" -> {
            val newFile = File(args[1])
            if (newFile.exists()) {
                index.appendText("${args[1]}\n")
                println("The file '${args[1]}' is tracked.")
            } else {
                println("Can't find '${args[1]}'.")
            }
        }
        args.size == 1 && args[0] == "checkout" -> {
            println("Commit id was not passed.")
        }
        args.size == 2 && args[0] == "checkout" -> {
            val logHashes = log.readText()
            if (args[1] in logHashes) {
                val indexContent = index.readLines()
                for (file in indexContent) {
                    // copy files from commit folder to main folder
                    val committedFile = File("vcs${separator}commits${separator}${args[1]}${separator}${file}")
                    val commitFile = File(file)
                    commitFile.writeBytes(committedFile.readBytes())
                }
                println("Switched to commit ${args[1]}.")
            } else {
                println("Commit does not exist.")
            }
        }
        commands.containsKey(args[0]) -> println(commands[args[0]])
        else -> println("'${args[0]}' is not a SVCS command.")
    }
}