package com.styleseat.dataEngTools.s3

import java.nio.file.Paths
import scala.util.matching.Regex


sealed trait s3Object {
  val bucket: String
  val key: String

  /** Provide a helpful utility for getting
   * the file extension for the s3Action item.
   */
  final lazy val fileExtension: String = {
    Paths
      .get(key)
      .getFileName
      .toString
      .split("\\.")
      .drop(1)
      .mkString(".")
  }

  /** define map()
   *
   * This is essential for allowing this to work in the for statements.
   *
   * @param f function for mapping
   * @param value the value of the item for use
   * @tparam A type for return result
   * @return f applied on the value
   */
  def map[A](f: s3Object => A, value: s3Object): A = f(value)
}


/** Allow for the s3 object to be referenced
 * by directly defining the values used for
 * interacting with s3.
 *
 * @param bucket s3 bucket name
 * @param key s3 key for the item in the bucket
 */
case class s3File(
  bucket: String,
  key: String
) extends s3Object


/** Allow for the s3 object to be referenced
 * by providing the standard s3 uri value
 *
 * @param value uri for the item in s3
 */
case class s3Uri(
  value: String
) extends s3Object {
  /** Uri values follow this specified pattern
   * the location of the bucket and key are
   * specified and leveraged for data extraction
   */
  private val pattern = new Regex(raw"^s3://(.*?)/(.*)", "bucket", "key")

  require(pattern.matches(value))

  override val bucket: String = pattern
    .findFirstMatchIn(value)
    .get
    .group("bucket")

  override val key: String = pattern
    .findFirstMatchIn(value)
    .get
    .group("key")

}


/** Allow for the s3 object to be referenced
 * by providing the standard s3 url value
 *
 * @param value url for the item in s3
 */
case class s3Url(
  value: String
) extends s3Object {
  /** Url values follow this specified pattern
   * the location of the bucket and key are
   * specified and leveraged for data extraction
   */
  private val pattern = new Regex(raw"^s3\.console\.aws\.amazon\.com/s3/object/(.*?)/(.*)", "bucket", "key")

  require(pattern.matches(value))

  override val bucket: String = pattern
    .findFirstMatchIn(value)
    .get
    .group("bucket")

  override val key: String = pattern
    .findFirstMatchIn(value)
    .get
    .group("key")

}
