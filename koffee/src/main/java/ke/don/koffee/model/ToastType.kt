/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ke.don.koffee.model

/**
 * Represents the different types of toasts that can be displayed.
 *
 * Each type typically corresponds to a different visual style or icon.
 * - **Info**: For informational messages.
 * - **Success**: For messages indicating a successful operation.
 * - **Warning**: For messages warning the user about a potential issue.
 * - **Error**: For messages indicating an error has occurred.
 * - **Neutral**: For messages that don't fit into the other categories, often with a default or neutral styling.
 */
enum class ToastType { Info, Success, Warning, Error, Neutral }
