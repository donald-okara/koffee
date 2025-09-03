/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee_demo.previews

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Mobile",
    group = "Devices",
    device = "spec:width=411dp,height=891dp,dpi=420", // typical phone
)
@Preview(
    name = "Desktop",
    group = "Devices",
    device = "spec:width=1280dp,height=800dp,dpi=240", // wide desktop-like
)
internal annotation class DevicePreviews
