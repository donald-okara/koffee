package ke.don.experimental_annotations

import kotlin.RequiresOptIn

@RequiresOptIn(
    message = "This API is experimental and requires explicit opt-in.",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
annotation class ExperimentalKoffeeApi