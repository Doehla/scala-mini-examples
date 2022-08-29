package com.styleseat.dataEngTools.s3

import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import cats.effect._
import java.nio.file.Files
import java.io.File


object s3Action {

  private val s3: S3Client =
    S3Client
      .builder()
      .build()

  private def composeFetchFileRequest(bucket: String, key: String) =
    GetObjectRequest
      .builder()
      .bucket(bucket)
      .key(key)
      .build()

  def downloadFile(
    obj: s3Object,
    destination: File
  ): IO[File] = {
    downloadFile(obj.bucket, obj.key, destination)
  }

  def downloadFile(
    bucket: String,
    key: String,
    destination: File
  ): IO[File] = {
    for {
      _ <- IO.blocking(Files.createDirectories(destination.toPath.toAbsolutePath.getParent))
      _ <- IO.blocking(Files.deleteIfExists(destination.toPath))
      _ <- IO.println(s"downloading file: ${bucket}/${key} -> ${destination}")
      _ <- IO.blocking(s3.getObject(composeFetchFileRequest(bucket, key), destination.toPath).deleteMarker())
    } yield destination
  }

}
