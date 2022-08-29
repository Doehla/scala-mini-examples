package com.styleseat.dataEngTools

import java.io.{BufferedInputStream, FileInputStream}
import java.util.zip.GZIPInputStream
import scala.io.{BufferedSource, Source}

object FileHelper {

  def gzipFileInputStream(s: String): BufferedSource =
    Source.fromInputStream(
      new GZIPInputStream(new BufferedInputStream(new FileInputStream(s)))
    )

}
