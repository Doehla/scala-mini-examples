package com.styleseat.dataEngTools

import cats.effect.{IO, Resource}

import java.io.{BufferedInputStream, File, FileInputStream}
import java.util.zip.GZIPInputStream
import scala.io.{BufferedSource, Source}

object FileHelper {

  // TODO: File operations are IO operations... this needs to be updated for IO

  def getExtension(f: File): String =
    f
      .getName
      .split("\\.")
      .drop(1)
      .mkString(".")

  def readFileToStream(f: File): Resource[IO, BufferedSource] = {
    val sourceStream = getExtension(f) match {
      case "gz" => new GZIPInputStream(new BufferedInputStream(new FileInputStream(f)))
      case _ => new FileInputStream(f)
    }
    Resource.make{
      IO.blocking(Source.fromInputStream(sourceStream))
    }{
      inputStream => IO.blocking(inputStream.close()).handleErrorWith(_ => IO.unit)
    }
  }

  def gzipFileInputStream(s: String): BufferedSource =
    Source.fromInputStream(
      new GZIPInputStream(new BufferedInputStream(new FileInputStream(s)))
    )

}
