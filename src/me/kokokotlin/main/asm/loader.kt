package me.kokokotlin.main.asm

import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path


fun loadBytesFromPath(path: Path) = Files.readAllBytes(path) ?: throw FileNotFoundException()