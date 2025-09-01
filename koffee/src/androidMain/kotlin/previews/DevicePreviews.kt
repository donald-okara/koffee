package previews

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Mobile",
    group = "Devices",
    device = "spec:width=411dp,height=891dp,dpi=420" // typical phone
)
@Preview(
    name = "Desktop",
    group = "Devices",
    device = "spec:width=1280dp,height=800dp,dpi=240" // wide desktop-like
)
internal annotation class DevicePreviews
