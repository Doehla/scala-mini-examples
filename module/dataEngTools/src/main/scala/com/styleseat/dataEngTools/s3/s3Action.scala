package com.styleseat.dataEngTools.s3

import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{GetObjectRequest, PutObjectRequest}
import cats.effect._
import java.nio.file.Files
import java.io.File


object s3Action {

  private final val s3: S3Client =
    S3Client
      .builder()
      .build()

  private def composeDownloadFileRequest(bucket: String, key: String) =
    GetObjectRequest
      .builder()
      .bucket(bucket)
      .key(key)
      .build()

  private def composeUploadFileRequest(bucket: String, key: String) =
    PutObjectRequest
      .builder()
      .bucket(bucket)
      .key(key)
      .build()

  def downloadFile(
    obj: s3Object,
    destination: File,
    overwrite: Boolean
  ): IO[Unit] =
    actuallyDownloadFile(obj.bucket, obj.key, destination, overwrite)

  def downloadFile(
    obj: s3Object,
    destination: File
  ): IO[Unit] =
    actuallyDownloadFile(obj.bucket, obj.key, destination, overwrite = true)

  def downloadFile(
    bucket: String,
    key: String,
    destination: File,
    overwrite: Boolean
  ): IO[Unit] =
    actuallyDownloadFile(bucket, key, destination, overwrite)

  def downloadFile(
    bucket: String,
    key: String,
    destination: File
  ): IO[Unit] =
    actuallyDownloadFile(bucket, key, destination, overwrite = true)

  private def actuallyDownloadFile(
    bucket: String,
    key: String,
    destination: File,
    overwrite: Boolean
  ): IO[Unit] = {
    for {
      _ <- IO.blocking(Files.createDirectories(destination.toPath.toAbsolutePath.getParent))
      _ <- IO.blocking({
        if (Files.exists(destination.toPath)) {
          if (overwrite) Files.deleteIfExists(destination.toPath)
          else IO.raiseError(new UnsupportedOperationException("Target file already exists & set to not overwrite."))
        }
      })
      // TODO: This should be converted into some logging call...
      _ <- IO.println(s"downloading file: ${bucket}/${key} -> ${destination}")
      request = composeDownloadFileRequest(bucket, key)
      _ <- IO.blocking(s3.getObject(request, destination.toPath).deleteMarker())
    } yield ()
  }

  def uploadFile(
      obj: s3Object,
      source: File
  ): IO[Unit] =
    uploadFile(obj.bucket, obj.key, source)

  def uploadFile(
      bucket: String,
      key: String,
      source: File
  ): IO[Unit] = {
    IO.blocking(s3.putObject(composeUploadFileRequest(bucket, key), source.toPath))
  }
}
