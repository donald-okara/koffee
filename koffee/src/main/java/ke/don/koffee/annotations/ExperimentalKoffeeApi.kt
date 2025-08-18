package ke.don.koffee.annotations

/**
 * Marks declarations that are part of the experimental Koffee API.
 *
 * Using these APIs requires an explicit opt-in via `@OptIn(ExperimentalKoffeeApi::class)`.
 *
 * **Warning:** These APIs are subject to change or removal in future versions without notice.
 * They are provided for early access and feedback, and should not be used in production code
 * without careful consideration of potential breaking changes.
 */
@RequiresOptIn(
    message = "This API is experimental and requires explicit opt-in.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
annotation class ExperimentalKoffeeApi