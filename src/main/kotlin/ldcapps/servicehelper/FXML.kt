package ldcapps.servicehelper

enum class FXML(val path: String, val minWidth: Double? = null, val minHeight: Double? = null) {
    Main("Main"),
    ToolSelector("Tools/Main"),
    Print("Print"),
    CreateAct("CreateAct"),
    Blank("Blank"),
    Login("Login"),
    CarData("CarData", 275.0, 335.0),
    OO("OO", 1100.0, 700.0),
    OOCollapsed("OO", 600.0, 360.0),
}