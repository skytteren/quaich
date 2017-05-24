package codes.bytes.quaich

package object api extends CoreLambdaApi

trait CoreLambdaApi {

  import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}

  trait ResponseMagnet {
    type Result
    def apply(): Result
  }


  def complete(magnet: ResponseMagnet): magnet.Result = magnet()

  class LambdaContext(ctx: Context) {
    def identity: Option[CognitoIdentity] = Option(ctx.getIdentity)

    def clientContext: Option[ClientContext] = Option(ctx.getClientContext)

    lazy val logger: LambdaLogger = ctx.getLogger

    def log(msg: => String) = logger.log(msg)

    def memoryLimitInMB: Int = ctx.getMemoryLimitInMB

    def remainingTimeInMillis: Int = ctx.getRemainingTimeInMillis

    def awsRequestId: String = ctx.getAwsRequestId

    def functionName: String = ctx.getFunctionName

    def logGroupName: Option[String] = Option(ctx.getLogGroupName)

    def logStreamName: Option[String] = Option(ctx.getLogStreamName)
  }

}
