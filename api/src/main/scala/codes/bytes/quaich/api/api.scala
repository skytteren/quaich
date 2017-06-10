package codes.bytes.quaich


package object api extends CoreLambdaApi

trait CoreLambdaApi {

  trait ResponseMagnet {
    type Result
    def apply(): Result
  }


  def complete(magnet: ResponseMagnet): magnet.Result = magnet()
}
